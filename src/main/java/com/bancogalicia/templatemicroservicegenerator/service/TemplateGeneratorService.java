package com.bancogalicia.templatemicroservicegenerator.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateGeneratorService {

    /*The number of logical cores is the number of physical cores multiplied with the number of threads you can run on each of them. On a Hexacore with 2 Threads per Core this would be 12 logical cores then afaik. 6 logical processors though*/
    //private Integer virtualCores = Runtime.getRuntime().availableProcessors();

    OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

    int availableProcessors = osBean.getAvailableProcessors(); // Núcleos virtuales
    int physicalCores = Runtime.getRuntime().availableProcessors(); // Núcleos físicos


    private static Integer NUMBER_OF_THREADS = 4;

    List<Thread> threads = new ArrayList<>();

    public void readExcel(String templatePath){

        try {

            FileInputStream file = new FileInputStream(new File(templatePath));

            Workbook workbook = WorkbookFactory.create(file);

            List<Sheet> sheets = new ArrayList<>();

            for (int i = 0; i < workbook.getNumberOfSheets(); i++)
            {
                sheets.add(workbook.getSheetAt(i));
            }

            List<SheetProcessorThread> sheetProcessorTask = new ArrayList<>();
            for(int i = 0; i < NUMBER_OF_THREADS; i++){
                sheetProcessorTask.add(new SheetProcessorThread());
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
