package com.bancogalicia.templatemicroservicegenerator.models;

import lombok.Data;

import java.util.List;

@Data
public class EndpointConfig {

    private MethodType methodType;

    private String basePath;

    private List<String> endpoints;

    private String className;

    private String autenticationMethod; //Crear enum
}
