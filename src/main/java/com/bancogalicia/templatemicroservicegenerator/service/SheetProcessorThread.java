package com.bancogalicia.templatemicroservicegenerator.service;

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

    @Autowired
    PojoProcessor pojoProcessor = new PojoProcessor();
    public List<Sheet> sheets;

    public SheetProcessorThread(){
        this.sheets = new ArrayList<>();
    }

    @Override
    public void run() {
        processSheets();
    }

    private void processSheets(){
        System.out.println(Thread.currentThread().getName() + " sheets: " + sheets.size());

        List<String> classesToCreate = new ArrayList<>();
        sheets.forEach(sheet -> classesToCreate.addAll(processSheet(sheet)));
        classesToCreate.forEach(classToProcess -> createClass("C:\\Users\\Florencia\\Downloads\\example.java",classToProcess));
    }

    private List<String> processSheet(Sheet sheet){

        //1° Extraigo objetos con info --> Config, request, response
        //2° Genero clases
        // En Lugar de lista con clases map key --> Nombre de la clase, si ya existe agrego endpoint

        //Process endpoint data
        //Process request
        //process response
        List<String> classesToCreate = new ArrayList<>();
        classesToCreate.add(pojoProcessor.getPojoClass(sheet,4,5));
        return classesToCreate;
    }

    private void createClass(String to, String classToCreate){
        Path path = Paths.get(to);
        try {
            Files.writeString(path,classToCreate,StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.print("Invalid Path");
        }
    }


    public void addSheet(Sheet sheet){
        this.sheets.add(sheet);
    }
}
