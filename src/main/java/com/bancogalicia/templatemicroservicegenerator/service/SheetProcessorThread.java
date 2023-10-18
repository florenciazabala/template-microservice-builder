package com.bancogalicia.templatemicroservicegenerator.service;

import com.bancogalicia.templatemicroservicegenerator.models.EndpointConfig;
import com.bancogalicia.templatemicroservicegenerator.repository.TemplateMicroservicesRespository;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class SheetProcessorThread implements Runnable {

    PojoProcessor pojoProcessor;

    EndpointConfigProcessor endpointConfigProcessor;

    TemplateMicroservicesRespository templateMicroservicesRespository;

    public static int SHEETS_PROCECESS = 0;

    private List<Sheet> sheets;

    private String destinationPath;


    public SheetProcessorThread() {
    }

    public SheetProcessorThread(String destinationPath, TemplateMicroservicesRespository templateMicroservicesRespository, EndpointConfigProcessor endpointConfigProcessor){
        this.sheets = new ArrayList<>();
        this.destinationPath = destinationPath;
        this.pojoProcessor = new PojoProcessor(templateMicroservicesRespository);
        this.endpointConfigProcessor = endpointConfigProcessor;
    }

    @Override
    public void run() {
        processSheets();
    }

    private void processSheets(){
        System.out.println(Thread.currentThread().getName() + " sheets: " + sheets.size());

        List<String> classesToCreate = new ArrayList<>();
        sheets.forEach(sheet -> classesToCreate.addAll(processSheet(sheet)));
        classesToCreate.forEach(classToProcess -> createClass(String.format("%s%s%d.java",destinationPath, "\\model\\example",SHEETS_PROCECESS++),classToProcess));
    }

    private List<String> processSheet(Sheet sheet){
           // System.out.println(SHEETS_PROCECESS);

            //1° Extraigo objetos con info --> Config, request, response
            //2° Genero clases
            // En Lugar de lista con clases map key --> Nombre de la clase, si ya existe agrego endpoint

            //Process endpoint data
            //Process request
            //process response
            EndpointConfig endpointConfig = endpointConfigProcessor.getBasicConfig(sheet);
            List<String> classesToCreate = new ArrayList<>();
            classesToCreate.add(pojoProcessor.getPojoClass(sheet, endpointConfig.getClassName(), 6, 7));
            return classesToCreate;
    }

    private void createClass(String to, String classToCreate){

            Path path = Paths.get(to);
            try {
                System.out.println("In create class " + to);
                Files.writeString(path, classToCreate, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                System.out.print("Invalid Path");
            }

    }


    public void addSheet(Sheet sheet){
        this.sheets.add(sheet);
    }

}

