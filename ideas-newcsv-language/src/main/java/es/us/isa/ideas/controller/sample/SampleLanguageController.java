package es.us.isa.ideas.controller.sample;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.us.isa.ideas.module.common.AppResponse;
import es.us.isa.ideas.module.common.AppResponse.Status;
import es.us.isa.ideas.module.controller.BaseLanguageController;

@Controller
@RequestMapping("/language")
public class SampleLanguageController extends BaseLanguageController {

    @RequestMapping(value = "/operation/{id}/execute", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public AppResponse executeOperation(String id, String content, String fileUri, String auxArg0) {

    	AppResponse appResponse = new AppResponse();
        appResponse.setFileUri(fileUri);
        
        appResponse.setHtmlMessage("<pre><b>This is a valid document.</b></pre>");
        appResponse.setStatus(Status.OK);

        return appResponse;
    }

    @RequestMapping(value = "/format/{format}/checkLanguage", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public AppResponse checkLanguage(String id, String content, String fileUri) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @SuppressWarnings("deprecation")
	@RequestMapping(value = "/convert", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public AppResponse convertFormat(String currentFormat, String desiredFormat, String fileUri, String content) {
    	
    	String dataContent = "";
    	AppResponse result = new AppResponse();
        result.setFileUri(fileUri);
        
        if(currentFormat.equals("csv") || currentFormat.equals("arff")){
        	dataContent += content;
        }
        else if(currentFormat.equals("xlsx") || currentFormat.equals("xlsm") || currentFormat.equals("xlsm")){
        	try{        		
        		InputStream excelFile = new ByteArrayInputStream(content.getBytes(StandardCharsets.ISO_8859_1.name()));
                @SuppressWarnings("resource")
				Workbook workbook = new XSSFWorkbook(excelFile);
                Sheet datatypeSheet = workbook.getSheetAt(0);
                Iterator<Row> iterator = datatypeSheet.iterator();
                
                while (iterator.hasNext()){
                    Row currentRow = iterator.next();
                    Iterator<Cell> cellIterator = currentRow.iterator();

                    while (cellIterator.hasNext()){
                        Cell currentCell = cellIterator.next();
                        //getCellTypeEnum shown as deprecated for version 3.15
                        //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
                        if (currentCell.getCellTypeEnum() == CellType.STRING) {
                            dataContent += currentCell.getStringCellValue() + ", ";
                        } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        	dataContent += currentCell.getNumericCellValue() + ", ";
                        }

                    }
                    
                    if(iterator.hasNext())
                    	dataContent += "\n";
                }
        	} catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
        String dataFormated = "";
        
        if(desiredFormat.equals("arff")){
        	if(currentFormat.equals("arff")){
        		dataFormated = dataContent;
        	}
        	else if(currentFormat.equals("csv") || currentFormat.equals("xlsx") || currentFormat.equals("xlsm")){
        		if(!dataContent.startsWith("@RELATION")){
	        		dataFormated += "@RELATION iris\n";
	        		dataFormated += "\n";
	        		
	        		String[] rows = dataContent.split("\n");
	        		rows = rows[0].split(",");
	        		
	        		for(int i=0;i<rows.length;i++){
	        			dataFormated += "@ATTRIBUTE "+i+" STRING\n";
	        		}
	        		
	        		dataFormated += "\n";
	        		dataFormated += "@DATA \n";
	        		dataFormated += dataContent;
        		}
        		else{
        			dataFormated += dataContent;
        		}
        	}
        }
        
        result.setData(dataFormated);
        result.setStatus(Status.OK);
        
        return result;
    }

}
