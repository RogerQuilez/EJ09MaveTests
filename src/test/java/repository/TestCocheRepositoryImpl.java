package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.entity.Coche;
import model.repository.impl.CocheRepositoryImpl;

public class TestCocheRepositoryImpl {

	private CocheRepositoryImpl cocheRepo = new CocheRepositoryImpl();
	
	@Before
	public void cargarBasedeDatos() {
		cocheRepo.abrirConexion();
	}
	
	@After
	public void cerarBaseDeDatos() {
		cocheRepo.cerrarConexion();
	}
	
	@Test
	public void altaTest() {
		
		Coche coche = new Coche();
		coche.setKm(213);
		coche.setMarca("Opel");
		coche.setModel("Corsa");
		coche.setMatricula("1232123");
		
		assertEquals(true, cocheRepo.alta(coche));
		
	}
	
	@Test
	public void bajaTest() {
		//No existe ningún coche con esta id
		assertEquals(false, cocheRepo.baja(12312));
		
		assertEquals(true, cocheRepo.baja(43));
	}
	
	@Test
	public void obtenerTest() {
		
		Coche cocheObtenido = cocheRepo.obtener(4);
		assertEquals("1232123", cocheObtenido.getMatricula());
		
	}
	
	@Test
	public void listTest() {
		
		List<Coche> coches = cocheRepo.listar();
		assertNotEquals(null, coches);
	}
	
	@Test
	public void modificarTest() {
		
		Coche c = cocheRepo.obtener(4);
		c.setKm(1223);
		assertEquals(true, cocheRepo.modificar(c));
	}
	
	
	
}
