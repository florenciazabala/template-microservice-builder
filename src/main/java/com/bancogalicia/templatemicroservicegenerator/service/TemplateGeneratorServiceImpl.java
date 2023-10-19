package com.bancogalicia.templatemicroservicegenerator.service;

import com.bancogalicia.templatemicroservicegenerator.models.TemplateMicroservice;
import com.bancogalicia.templatemicroservicegenerator.repository.TemplateMicroservicesRespository;
import com.bancogalicia.templatemicroservicegenerator.service.processors.EndpointConfigProcessor;
import com.bancogalicia.templatemicroservicegenerator.service.tasks.ClassWriterThread;
import com.bancogalicia.templatemicroservicegenerator.service.tasks.SheetCounter;
import com.bancogalicia.templatemicroservicegenerator.service.tasks.SheetProcessorThread;
import com.bancogalicia.templatemicroservicegenerator.service.tasks.ThreadSafeQueue;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateGeneratorServiceImpl implements TemplateGeneratorService {

    public static final String MAIN_CLASSNAME = "\\Application.java";
    @Autowired
    private TemplateMicroservicesRespository templateMicroservicesRespository;

    @Autowired
    private EndpointConfigProcessor endpointConfigProcessor;

    private int numberOfThread;


    //private static Integer NUMBER_OF_THREADS = 4;

    List<Thread> threads = new ArrayList<>();

    @Override
    public void readExcel(String templatePath, String destinationPath, int numberOfThreads){

        try {
            distributeSheets(templatePath, destinationPath, numberOfThreads);
            changeImplementationRestTemplateBean(destinationPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void distributeSheets(String templatePath, String destinationPath, int numberOfThreads) throws IOException, InterruptedException {

        FileInputStream file = new FileInputStream(new File(templatePath));

        Workbook workbook = WorkbookFactory.create(file);

        List<Sheet> sheets = new ArrayList<>();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++)
        {
            sheets.add(workbook.getSheetAt(i));
        }

        //Producers
        ThreadSafeQueue queue = new ThreadSafeQueue();
        SheetCounter sheetsCounter = new SheetCounter();

        List<SheetProcessorThread> sheetProcessorTask = new ArrayList<>();
        for(int i = 0; i < numberOfThreads; i++){
            sheetProcessorTask.add(new SheetProcessorThread(queue,sheetsCounter,templateMicroservicesRespository,endpointConfigProcessor));
        }

        int i = 0;
        for (Sheet sheet: sheets){
            i = i == numberOfThreads ? 0 : i;
            sheetProcessorTask.get(i).addSheet(sheet);
            i++;
        }

        sheetProcessorTask.forEach(runnable -> threads.add(new Thread(runnable)));

        //Consumer
        Thread classWriterThread =new Thread(new ClassWriterThread(queue,destinationPath));

        threads.forEach(thread -> thread.start());
        classWriterThread.start();

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        classWriterThread.join();

    }

    private void changeImplementationRestTemplateBean(String destinationPath) throws IOException {

        System.out.println("RestTemplate implementation");
        Path path = Paths.get(String.format("%s%s", destinationPath, MAIN_CLASSNAME));
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        int position = 25;
        TemplateMicroservice templateMicroservice = templateMicroservicesRespository.findByName("rest_template_impl").orElse(new TemplateMicroservice());
        String extraLine = templateMicroservice.getStructure();
        if(!lines.get(position).contains("RestTemplate restTemplate = restTemplateBuilder.build();")){
            lines.remove(position);
            lines.add(position, extraLine);
            Files.write(path, lines, StandardCharsets.UTF_8);
        }
    }
}
