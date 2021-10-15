package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import model.entity.Coche;
import model.service.CocheService;
import model.service.impl.CocheServiceImpl;

public class TestCocheServiceImpl {

	CocheService cocheService = new CocheServiceImpl();
	
	@Test
	public void altaTest() {
		//Todo correcto
		Coche c = new Coche();
		c.setKm(12312);
		c.setMatricula("1232123");
		c.setModel("Opel");
		c.setMarca("Corsa");
		HashMap<String, String> messages = cocheService.alta(c);
		for (String mess: messages.keySet()) {
			assertEquals("Alta Correcta", mess);
		}
		
		//La matricula tiene que tener 7 caracteres
		Coche c2 = new Coche();
		c2.setKm(12312);
		c2.setMatricula("123");
		c2.setModel("Opel");
		c2.setMarca("Corsa");
		
		messages = cocheService.alta(c2);
		for (String mess: messages.keySet()) {
			assertEquals("Matricula Error", mess);
		}
		
		//El modelo no puede estar vacío
		Coche c3 = new Coche();
		c3.setKm(12312);
		c3.setModel("");
		c3.setMatricula("1232321");
		c3.setMarca("Corsa");
		
		messages = cocheService.alta(c3);
		for (String mess: messages.keySet()) {
			assertEquals("Model Error", mess);
		}
		
		//La marca no puede estar vacía
		Coche c4 = new Coche();
		c4.setKm(12312);
		c4.setModel("Opel");
		c4.setMatricula("1232321");
		c4.setMarca("");
		
		messages = cocheService.alta(c4);
		for (String mess: messages.keySet()) {
			assertEquals("Marca Error", mess);
		}
	}
	
	@Test
	public void modificarTest() {
		
		Coche c = cocheService.obtener(4);
		
		//Todo correcto
		c.setMarca("Kilimanjaro");
		HashMap<String, String> messages = cocheService.modificar(c);
		for (String mess: messages.keySet()) {
			assertEquals("Modificar Correcta", mess);
		}
		
		//La Marca tiene que tener 7 caracteres
		c.setMarca("");
		messages = cocheService.modificar(c);
		for (String mess: messages.keySet()) {
			assertEquals("Marca Error", mess);
		}
		
		//El modelo no puede estar vacío
		c.setMarca("Opel");
		c.setModel("");
		messages = cocheService.modificar(c);
		for (String mess: messages.keySet()) {
			assertEquals("Model Error", mess);
		}
		
		//La Matricula no puede estar vacía
		c.setModel("Corsa");
		c.setMatricula("123");
		messages = cocheService.modificar(c);
		for (String mess: messages.keySet()) {
			assertEquals("Matricula Error", mess);
		}
	}
	
	@Test
	public void obtenerTest() {
		
		String matricula = "1232123";
		String matriculaErronea = "123";
		Coche c = cocheService.obtener(4);
		
		assertEquals(matricula, c.getMatricula());
		assertNotEquals(matriculaErronea, c.getMatricula());
	}
	
	@Test
	public void listTest() {
		List<Coche> coches = cocheService.listar();
		assertNotNull(coches);
	}
	
	//Solo funciona la primera vez, se debe cambiar el id que ha borrado ya para que vuelva
	//a funcionar con un id existente
	@Test
	public void bajaTest() {
		HashMap<String, String> messages = cocheService.baja(40);
		for (String mess: messages.keySet()) {
			assertEquals("Baja Correcta", mess);
		}
	}
}
