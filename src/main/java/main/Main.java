package main;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import model.entity.Coche;
import model.service.CocheService;
import model.service.impl.CocheServiceImpl;

public class Main {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		CocheService cocheService = new CocheServiceImpl();
		Scanner sc = new Scanner(System.in);
		Integer opcion = 1;
		
		do {
			printMenu();
			opcion = Integer.parseInt(sc.nextLine());
			HashMap<String, String> message = new HashMap<>();
			
			switch(opcion) {
			
				case 0:
					break;
			
				case 1:
					message = cocheService.alta(getCar(sc, new Coche()));
					printMessage(message);
					break;
					
				case 2:
					printListCoche(cocheService.listar());
					break;
					
				case 3:
					printCar(getCarById(sc, cocheService));
					break;
					
				case 4:
					Coche updateCoche = getCarById(sc, cocheService);
					message = cocheService.modificar(getCar(sc, updateCoche));
					printMessage(message);
					break;
					
				case 5:
					Coche deleteCoche = getCarById(sc, cocheService);
					message = cocheService.baja(deleteCoche.getId());
					printMessage(message);
					break;
					
				case 6:
					cocheService.exportToExcel();
			}
			
		} while(opcion!=0);
	}
	
	/**
	 * Print main Menu 
	 */
	public static void printMenu() {
		System.out.println("-------  Select an option  --------\n"
				+ "\t 1. Insert new Car\n"
				+ "\t 2. List all Cars\n"
				+ "\t 3. Find Car By Id\n"
				+ "\t 4. Update existent car\n"
				+ "\t 5. Delete a car\n"
				+ "\t 6. Export BBDD to PDF\n"
				+ "\t 0. Exit");
				
	}
	
	/**
	 * @param sc -> Scanner para leer por console
	 * @param coche -> Objeto coche con el que trabajará el método
	 * @return coche -> Devuelve un objeto coche enviado por parametros con los atributos asignados
	 * por el usuario
	 */
	public static Coche getCar(Scanner sc, Coche coche) {
		
		try {
			
			System.out.println("Introcuce Marca of car");
			String marca = sc.nextLine();
			
			System.out.println("Introduce Model of car");
			String model = sc.nextLine();
			System.out.println("Introduce Km's of car");
			Integer km = Integer.parseInt(sc.nextLine());
			System.out.println("Introduce Matricula of car");
			String matricula = sc.nextLine();
			
			coche.setMarca(marca);
			coche.setModel(model);
			coche.setKm(km);
			coche.setMatricula(matricula);
			return coche;
			
		} catch (NumberFormatException e) {
			System.out.println("Error Null -> El campo kilómetros solo acepta números");
			return null;
		}
		
	}
	
	/**
	 * 
	 * @param c -> Objeto Coche
	 * Printa los atributos del objeto Coche
	 */
	public static void printCar(Coche c) {
		System.out.println("ID: " + c.getId());
		System.out.println("Marca: " + c.getMarca());
		System.out.println("Model: " + c.getModel());
		System.out.println("KM'S: " + c.getKm());
		System.out.println("Matricula: " + c.getMatricula());
		System.out.println("-------------------");
	}
	
	/**
	 * @param listCoche -> Parámetro que contiene la lista de los coches de la BBDD
	 * Printa la lista de los coches disponibles en la BBDD
	 */
	public static void printListCoche(List<Coche> listCoche) {
		for (Coche c: listCoche) {
			printCar(c);
		}
	}
	
	/**
	 * @param sc -> Scanner para leer por consola
	 * @param cocheService -> Servicio para poder obtener el objeto Coche a través de la id
	 * @return Retorna un coche obtenido por el id que proporciona el cliente
	 */
	public static Coche getCarById(Scanner sc, CocheService cocheService) {
		Integer id = null;
		Coche coche;
		
		do {
			System.out.println("Por favor, introduce la ID del coche");
			id = Integer.parseInt(sc.nextLine());
			coche = cocheService.obtener(id);
			
			if (coche == null) 
				System.out.println("-----------ID Inválida----------");
			
		} while(coche == null);
		
		return coche;
	}
	
	/**
	 * @param errors -> HashMap con los errores o las confirmaciones del servicio coche
	 * Printa todos los mensajes que tenga el objeto message
	 */
	public static void printMessage(HashMap<String, String> message) {
		System.out.println("***************************************");
		for (String mess: message.keySet()) {
			System.out.println(mess + message.get(mess));
		}
		System.out.println("***************************************");
	}
}
