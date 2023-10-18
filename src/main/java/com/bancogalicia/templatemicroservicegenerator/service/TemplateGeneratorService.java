package com.bancogalicia.templatemicroservicegenerator.service;

import com.bancogalicia.templatemicroservicegenerator.models.TemplateMicroservice;
import com.bancogalicia.templatemicroservicegenerator.repository.TemplateMicroservicesRespository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateGeneratorService {

    public static final String MAIN_CLASSNAME = "\\Application.java";
    @Autowired
    private TemplateMicroservicesRespository templateMicroservicesRespository;

    @Autowired
    private EndpointConfigProcessor endpointConfigProcessor;


    /*The number of logical cores is the number of physical cores multiplied with the number of threads you can run on each of them. On a Hexacore with 2 Threads per Core this would be 12 logical cores then afaik. 6 logical processors though*/
    //private Integer virtualCores = Runtime.getRuntime().availableProcessors();

    OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

    int availableProcessors = osBean.getAvailableProcessors(); // Núcleos virtuales
    int physicalCores = Runtime.getRuntime().availableProcessors(); // Núcleos físicos


    private static Integer NUMBER_OF_THREADS = 4;

    List<Thread> threads = new ArrayList<>();

    public void readExcel(String templatePath, String destinationPath){

        try {
            distributeSheets(templatePath, destinationPath);
            changeImplementationRestTemplateBean(destinationPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void distributeSheets(String templatePath, String destinationPath) throws IOException {

        FileInputStream file = new FileInputStream(new File(templatePath));

        Workbook workbook = WorkbookFactory.create(file);

        List<Sheet> sheets = new ArrayList<>();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++)
        {
            sheets.add(workbook.getSheetAt(i));
        }

        List<SheetProcessorThread> sheetProcessorTask = new ArrayList<>();
        for(int i = 0; i < NUMBER_OF_THREADS; i++){
            sheetProcessorTask.add(new SheetProcessorThread(destinationPath,templateMicroservicesRespository,endpointConfigProcessor));
        }

        int i = 0;
        for (Sheet sheet: sheets){
            i = i == NUMBER_OF_THREADS ? 0 : i;
            sheetProcessorTask.get(i).addSheet(sheet);
            i++;
        }

        sheetProcessorTask.forEach(runnable -> threads.add(new Thread(runnable)));

        threads.forEach(thread -> thread.start());
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
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
