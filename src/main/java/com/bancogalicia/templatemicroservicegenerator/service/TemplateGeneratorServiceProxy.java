package com.bancogalicia.templatemicroservicegenerator.service;

public class TemplateGeneratorServiceProxy implements TemplateGeneratorService{

    private TemplateGeneratorService templateGeneratorService;

    public TemplateGeneratorServiceProxy(TemplateGeneratorService templateGeneratorService) {
        super();
        this.templateGeneratorService = templateGeneratorService;
    }

    @Override
    public void readExcel(String templatePath, String destinationPath, int numberOfThreads) {
        long startTime = System.currentTimeMillis();
        templateGeneratorService.readExcel(templatePath,destinationPath,numberOfThreads);
        long endTime = System.currentTimeMillis();
        System.out.println("Duration : " + (endTime - startTime));
    }
}
