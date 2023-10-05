package com.bancogalicia.templatemicroservicegenerator.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProcessTemplateRequest {

    @JsonProperty("template_path")
    private String templatePath;

    @JsonProperty("microservice_path")
    private String microservicePath;

}
