package model.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.entity.Coche;
import model.respository.CocheRepository;

public class CocheRepositoryImpl implements CocheRepository{

	private Connection conexion;
	
	/**
	 * 
	 * @return -> Devuelve true en caso de que se haya podido abrir la conexión
	 */
	public boolean abrirConexion(){
		String url = "jdbc:mysql://localhost:3306/bbdd";
		String usuario = "root";
		String password = "";
		try {
			conexion = DriverManager.getConnection(url,usuario,password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @return -> Devuelve true en caso de que se haya podido cerrar la conexión
	 */
	public boolean cerrarConexion(){
		try {
			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * @param c -> Parámetro objeto Coche 
	 * @return -> Devuelve true en caso de que se haya podido completar el alta del coche
	 */
	@Override
	public boolean alta(Coche c) {
		if(!abrirConexion()){
			return false;
		}
		boolean alta = true;
		
		String query = "insert into coche (MARCA,MODEL,KM,MATRICULA) "
				+ " values(?,?,?,?)";
		try {
			//preparamos la query con valores parametrizables(?)
			PreparedStatement ps = conexion.prepareStatement(query);
			ps.setString(1, c.getMarca());
			ps.setString(2, c.getModel());
			ps.setInt(3, c.getKm());
			ps.setString(4, c.getMatricula());
			
			int numeroFilasAfectadas = ps.executeUpdate();
			if(numeroFilasAfectadas == 0) {
				alta = false;
			}
		} catch (SQLException e) {
			System.out.println("alta -> Error al insertar: " + c);
			alta = false;
			e.printStackTrace();
		} finally{
			cerrarConexion();
		}
		
		return alta;
	}

	/**
	 * @param id -> Parámetro con la id del coche que se quiere dar de baja
	 * @return -> Devuelve true en caso de que se haya podido completar la eliminación del coche
	 */
	@Override
	public boolean baja(int id) {
		if(!abrirConexion()){
			return false;
		}
		
		boolean borrado = true;
		String query = "delete from coche where id = ?";
		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			//sustituimos la primera interrgante por la id
			ps.setInt(1, id);
			
			int numeroFilasAfectadas = ps.executeUpdate();
			if(numeroFilasAfectadas == 0)
				borrado = false;
		} catch (SQLException e) {
			System.out.println("baja -> No se ha podido dar de baja"
					+ " el id " + id);
			e.printStackTrace();
		} finally {
			cerrarConexion();
		}
		return borrado; 
	}

	/**
	 * @param c -> Parámetro objeto Coche
	 * @return -> Devuelve true en caso de que se haya podido completar la modificación del coche
	 */
	@Override
	public boolean modificar(Coche c) {
		if(!abrirConexion()){
			return false;
		}
		boolean modificado = true;
		String query = "update COCHE set MARCA=?, MODEL=?, "
				+ "KM=?, MATRICULA=? WHERE ID=?";
		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			ps.setString(1, c.getMarca());
			ps.setString(2, c.getModel());
			ps.setInt(3, c.getKm());
			ps.setString(4, c.getMatricula());
			ps.setInt(5, c.getId());
			
			int numeroFilasAfectadas = ps.executeUpdate();
			if(numeroFilasAfectadas == 0)
				modificado = false;
			else
				modificado = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("modificar -> error al modificar la "
					+ " coche " + c);
			modificado = false;
			e.printStackTrace();
		} finally{
			cerrarConexion();
		}
		
		return modificado;
	}

	/**
	 * @param id -> Parámetro con la id del coche que se quiere obtener
	 * @return -> Devuelve true en caso de que se haya podido obtener el coche
	 */
	@Override
	public Coche obtener(int id) {
		if(!abrirConexion()){
			return null;
		}		
		Coche coche = null;
		
		String query = "select ID,MARCA,MODEL,KM,MATRICULA from coche "
				+ "where id = ?";
		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				coche = new Coche();
				coche.setId(rs.getInt(1));
				coche.setMarca(rs.getString(2));
				coche.setModel(rs.getString(3));
				coche.setKm(rs.getInt(4));
				coche.setMatricula(rs.getString(5));
			}
		} catch (SQLException e) {
			System.out.println("obtener -> error al obtener la "
					+ "coche con id " + id);
			e.printStackTrace();
		} finally {
			cerrarConexion();
		}
		
		
		return coche;
	}

	/**
	 * @return -> Devuelve la lista de Coches existentes en la BBDD
	 */
	@Override
	public List<Coche> listar() {
		if(!abrirConexion()){
			return null;
		}		
		List<Coche> listaCoches = new ArrayList<>();
		
		String query = "select ID,MARCA,MODEL,KM,MATRICULA from coche";
		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				Coche coche = new Coche();
				coche.setId(rs.getInt(1));
				coche.setMarca(rs.getString(2));
				coche.setModel(rs.getString(3));
				coche.setKm(rs.getInt(4));
				coche.setMatricula(rs.getString(5));
				
				listaCoches.add(coche);
			}
		} catch (SQLException e) {
			System.out.println("listar -> error al obtener las "
					+ "coches");
			e.printStackTrace();
		} finally {
			cerrarConexion();
		}
		
		
		return listaCoches;
	}
	
	//Bloque estatico, los bloques estaticos son ejecutados
	//por java JUSTO ANTES de ejecutar el metodo main()
	//Java busca todos los metodos staticos que haya en el programa
	//y los ejecuta
	/*
	static{
		try {
			//Esta sentencia carga del jar que hemos importado
			//una clase que se llama Driver que esta en el paqueta
			//com.mysql.jdbc. Esta clase se carga previamente en
			//java para más adelante ser llamada
			//Esto SOLO es necesario si utilizamos una versión java anterior
			//a la 1.7 ya que desde esta versión java busca automaticamente 
			//los drivers
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver cargado");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver NO cargado");
			e.printStackTrace();
		}
	}*/

}
