package commons;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcel {
	
	private static XSSFSheet ExcelWSheet;	 
	private static XSSFWorkbook ExcelWBook;
	private static XSSFCell Cell;
	private static XSSFRow Row;
	private static MissingCellPolicy xRow;
	
	//This method is to set the File path and to open the Excel file, Pass Excel Path and Sheetname as Arguments to this method
	public static void setExcelFile() throws Exception {

		try {

   			// Open the Excel file	
			FileInputStream ExcelFile = new FileInputStream(variables.FilePath+variables.FileName);

			// Access the required test data sheet	
			ExcelWBook = new XSSFWorkbook(ExcelFile);	
			ExcelWSheet = ExcelWBook.getSheet(variables.sheetname);
			
			System.out.println("Total rows "+ExcelWSheet.getLastRowNum());

		} catch (FileNotFoundException e){
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		}
	}
	
	//This method is to read the test data from the Excel cell, in this we are passing parameters as Row num and Col num
    public static String getCellData(int RowNum, int ColNum) throws Exception{

			try{
				
	  			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
	  			String CellData = Cell.getStringCellValue();
	
	  			return CellData;

  			}catch (Exception e){
				return"";
  			}
    }
    
  //This method is to write in the Excel cell, Row num and Col num are the parameters
	public static void setCellData(String Result, int RowNum, int ColNum) throws Exception	{
		System.out.println("Writing "+Result+" to: "+RowNum +" - "+ColNum);
		try{

			//if row doesn't exist (no data in it) create new one
			if (ExcelWSheet.getRow(RowNum) == null) {
				ExcelWSheet.createRow(RowNum);
			}

  			Row  = ExcelWSheet.getRow(RowNum);
  			Cell = Row.getCell(ColNum, xRow.RETURN_BLANK_AS_NULL);

			if (Cell == null) {

				Cell = Row.createCell(ColNum);	
				Cell.setCellValue(Result);

				} else {

					Cell.setCellValue(Result);

				}

				// Constant variables Test Data path and Test Data file name
  				FileOutputStream fileOut = new FileOutputStream(variables.FilePath+variables.FileName);
  				ExcelWBook.write(fileOut);	
  				fileOut.flush();
				fileOut.close();

			}catch(Exception e){
				throw (e);
		}
	}

	public static void writeCellData (String Result, int RowNum, int ColNum) throws Exception {
		File file = new File(variables.FilePath+variables.FileName);

		try {

			FileInputStream fis = new FileInputStream(file);
			ExcelWBook = new XSSFWorkbook(fis);
			ExcelWSheet = ExcelWBook.getSheet(variables.sheetname);

			//if row doesn't exist (no data in it) create new one
			if (ExcelWSheet.getRow(RowNum) == null) {
				ExcelWSheet.createRow(RowNum);
			}

			ExcelWSheet.getRow(RowNum).createCell(ColNum).setCellValue(Result);

			FileOutputStream fos = new FileOutputStream(file);

			ExcelWBook.write(fos);
			ExcelWBook.close();

		} catch (FileNotFoundException e){
			System.out.println("Could not read the Excel sheet");
		}
	}
}
