package com.bancogalicia.templatemicroservicegenerator.controller;

import com.bancogalicia.templatemicroservicegenerator.models.ProcessTemplateRequest;
import com.bancogalicia.templatemicroservicegenerator.service.TemplateGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemplateGeneratorController {

    @Autowired
    TemplateGeneratorService templateGeneratorService;
    @PostMapping("")
    public void processTemplate(@RequestBody ProcessTemplateRequest processTemplateRequest){
        templateGeneratorService.readExcel(processTemplateRequest.getTemplatePath());
    }
}
