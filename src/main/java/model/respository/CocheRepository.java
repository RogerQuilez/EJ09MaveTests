package model.respository;

import java.util.List;

import model.entity.Coche;

public interface CocheRepository {
	boolean alta(Coche c);
	boolean baja(int id);
	boolean modificar(Coche c);
	Coche obtener(int id);
	List<Coche> listar();
}
