package com.bancogalicia.templatemicroservicegenerator.controller;

import com.bancogalicia.templatemicroservicegenerator.models.ProcessTemplateRequest;
import com.bancogalicia.templatemicroservicegenerator.service.TemplateGeneratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemplateGeneratorController {

    @Autowired
    TemplateGeneratorServiceImpl templateGeneratorService;
    @PostMapping("")
    public void processTemplate(@RequestBody ProcessTemplateRequest processTemplateRequest){
        templateGeneratorService.readExcel(processTemplateRequest.getTemplatePath(), processTemplateRequest.getMicroservicePath(), processTemplateRequest.getNumberOfThreads());
    }
}
