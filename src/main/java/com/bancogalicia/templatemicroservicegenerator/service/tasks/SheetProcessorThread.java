package com.bancogalicia.templatemicroservicegenerator.service.tasks;

import com.bancogalicia.templatemicroservicegenerator.models.EndpointConfig;
import com.bancogalicia.templatemicroservicegenerator.repository.TemplateMicroservicesRespository;
import com.bancogalicia.templatemicroservicegenerator.service.processors.EndpointConfigProcessor;
import com.bancogalicia.templatemicroservicegenerator.service.processors.PojoProcessor;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SheetProcessorThread implements Runnable {

    private PojoProcessor pojoProcessor;

    private EndpointConfigProcessor endpointConfigProcessor;

    private ThreadSafeQueue queue;

    private List<Sheet> sheets;

    private SheetCounter sheetsCounter;

    public SheetProcessorThread(ThreadSafeQueue queue,SheetCounter sheetsCounter, TemplateMicroservicesRespository templateMicroservicesRespository, EndpointConfigProcessor endpointConfigProcessor){
        this.queue = queue;
        this.sheets = new ArrayList<>();
        this.pojoProcessor = new PojoProcessor(templateMicroservicesRespository);
        this.endpointConfigProcessor = endpointConfigProcessor;
        this.sheetsCounter = sheetsCounter;
    }

    @Override
    public void run() {
        processSheets();
    }

    private void processSheets(){
        System.out.println(Thread.currentThread().getName() + " sheets: " + sheets.size());

        List<String> classesToCreate = new ArrayList<>();
        sheets.forEach(sheet -> classesToCreate.addAll(processSheet(sheet)));

        classesToCreate.forEach(c -> queue.add(c));


       if (sheetsCounter.getItems() == 6) { //En este caso 1 clase x hoja
            queue.terminate();
            System.out.println("No more sheets to read");
       }

    }

    private List<String> processSheet(Sheet sheet){
            sheetsCounter.increment();
            System.out.println("Sheets count: "+sheetsCounter.getItems());

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


    public void addSheet(Sheet sheet){
        this.sheets.add(sheet);
    }

}
