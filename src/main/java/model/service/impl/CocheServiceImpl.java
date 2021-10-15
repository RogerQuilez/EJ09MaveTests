package model.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import model.entity.Coche;
import model.repository.impl.CocheRepositoryImpl;
import model.respository.CocheRepository;
import model.service.CocheService;

public class CocheServiceImpl implements CocheService {

	CocheRepository cocheRepository = new CocheRepositoryImpl();

	/**
	 * @param c -> Objeto coche
	 * @return -> Devuelve un objeto HashMap indicando si está todo correcto o han habido errores
	 */
	@Override
	public HashMap<String, String> alta(Coche c) {
		return altaAndModificar(c, "Alta");
	}

	/**
	 * @param id -> parámetro para buscar a través del repositorio un objeto Coche con esa ID
	 * @return -> Devuelve un objeto HashMap indicando si está todo correcto o han habido errores
	 */
	@Override
	public HashMap<String, String> baja(int id) {
		HashMap<String, String> messages = new HashMap<>();
		Coche coche = cocheRepository.obtener(id);
		if (coche != null) {			
			if (cocheRepository.baja(id))
				messages.put("Baja Correcta -> ", "El vehiculo se ha eliminado correctamente");
			else
				messages.put("Persistence Error -> ", "Error contra la base de datos");
		} else {
			messages.put("Null Error -> ", "El coche con la ID " + id + " no se encuentra en la bbdd");
		}
		return messages;
	}

	/**
	 * @param c -> Objeto coche
	 * @return -> Devuelve un objeto HashMap indicando si está todo correcto o han habido errores
	 */
	@Override
	public HashMap<String, String> modificar(Coche c) {
		return altaAndModificar(c, "Modificar");
	}

	/**
	 * @param id -> parámetro para buscar a través del repositorio un objeto Coche con esa ID
	 * @return -> Devuelve el objeto Coche encontrado con esa ID
	 */
	@Override
	public Coche obtener(int id) {
		return cocheRepository.obtener(id);
	}

	/**
	 * @return -> Devuelve la lista de los coches existentes en la BBDD
	 */
	@Override
	public List<Coche> listar() {
		return cocheRepository.listar();
	}
	
	/**
	 * @param c -> Objeto coche
	 * @param message -> Mensaje indicando si es Alta o Modificación
	 * @return -> Devuelve un objeto HashMap indicando si está todo correcto o han habido errores
	 */
	public HashMap<String, String> altaAndModificar(Coche c, String message) {
		
		HashMap<String, String> messages = new HashMap<>();
		if (c != null) {
			if (c.getMarca().length() == 0) {
				messages.put("Marca Error -> ", "El campo Marca no puede estar vacío");
			} 
			if (c.getMatricula().length() != 7) {
				messages.put("Matricula Error -> ", "El campo Matricula debe tener 7 caracteres");
			}
			if (c.getModel().length() == 0) {
				messages.put("Model Error -> ", "El campo Modelo no puede estar vacío");
			}
			if (messages.isEmpty()) {
				if (this.cocheRepository.alta(c))
					messages.put(message +" Correcta -> ", "El vehiculo ha sido creado correctamente");
				else 
					messages.put("Persistence Error -> ", "Error contra la base de datos");
			}
		} else {
			messages.put("Null Error -> ", "El objeto coche no puede ser nulo");
		}
		return messages;
	}
	
	/**
	 * Exporta la base de datos a un fichero tipo Excel
	 */
	@Override
	public void exportToExcel() {
		
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("Choches");
		
		int numeroRenglon = 0;

		Row rowHeader = sheet.createRow(numeroRenglon++);
		
		printHeaderExcel(rowHeader);
		
		printCarsExcel(sheet, numeroRenglon);
		
		try {
            FileOutputStream out = new FileOutputStream(new File("src/main/resources/coches.xlsx"));
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
				
	
	}

	/**
	 * Printa los atributos de los coches en la tabla del excel
	 * @param coches
	 * @param sheet
	 * @param numeroRenglon
	 */
	private void printCarsExcel(Sheet sheet, int numeroRenglon) {
		
		List<Coche> coches = this.listar();
		
		for (int i = 0; i < coches.size(); i++) {
			
			Row row = sheet.createRow(numeroRenglon++);
			Coche c = coches.get(i);
			
			int numeroCelda = 0;
			
			Cell cell = row.createCell(numeroCelda++);
			cell.setCellValue(c.getMarca());
			cell = row.createCell(numeroCelda++);
			cell.setCellValue(c.getModel());
			cell = row.createCell(numeroCelda++);
			cell.setCellValue(c.getKm());
			cell = row.createCell(numeroCelda++);
			cell.setCellValue(c.getMatricula());
		}
	}

	/**
	 * Printa el nombre de los atributos de los objetos tipo coche
	 * @param headers
	 * @param rowHeader
	 */
	private void printHeaderExcel(Row rowHeader) {
		String[] headers = {"Marca", "Modelo", "Km's", "Matricula"};
		for (int i = 0; i < headers.length; i++) {
			Cell cell = rowHeader.createCell(i);
			cell.setCellValue(headers[i]);
		}
	}

	/**
	 * Exporta la base de datos a un fichero tipo Pdf
	 */
	@Override
	public void exportToPdf() {
		
		try (PDDocument doc = new PDDocument()) {

			PDPage myPage = new PDPage();
			doc.addPage(myPage);

			try (PDPageContentStream cont = new PDPageContentStream(doc, myPage)) {

				printTitlePdf(cont);
				
				printVehiclesPdf(cont);

				cont.endText();		
				
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
			doc.save("src/main/resources/coches.pdf");
			System.out.println("Fichero pdf creado en src/main/resources/coches.pdf");
			System.out.println("Refresque el proyecto en caso de que no aparezca");
			
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Printa los coches de la base de datos en el pdf
	 * @param cont
	 * @throws IOException
	 */
	private void printVehiclesPdf(PDPageContentStream cont) throws IOException {
		
		List<Coche> coches = this.listar();
		
		for (int i = 0; i < coches.size(); i++) {
			String carLine = coches.get(i).getMarca() + " " + coches.get(i).getModel() + " " +
							coches.get(i).getKm() + " " + coches.get(i).getMatricula();
			cont.showText(carLine);
			cont.newLine();
		}
	}

	/**
	 * Printa el nombre de los atributos del titulo en el pdf
	 * @param cont
	 * @throws IOException
	 */
	private void printTitlePdf(PDPageContentStream cont) throws IOException {
		
		String[] headers = {"Marca", "Modelo", "Km's", "Matricula"};
		
		cont.beginText();

		cont.setFont(PDType1Font.TIMES_ROMAN, 12);
		cont.setLeading(14.5f);

		cont.newLineAtOffset(25, 700);
		String headersLine = "";
		for (String h: headers) headersLine += h + " ";
		cont.showText(headersLine);

		cont.newLine();
	}

		
	

}
