package com.bancogalicia.templatemicroservicegenerator.service;


public interface TemplateGeneratorService {

    void readExcel(String templatePath, String destinationPath, int numberOfThreads);

}
