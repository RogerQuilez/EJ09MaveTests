package model.service;

import java.util.HashMap;
import java.util.List;

import model.entity.Coche;

public interface CocheService {
		
		public HashMap<String, String> alta(Coche c);
		public HashMap<String, String> baja(int id);
		public HashMap<String, String> modificar(Coche c);
		public Coche obtener(int id);
		public List<Coche> listar();
		public void exportToPdf();
		public void exportToExcel();
}
