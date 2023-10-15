package com.bancogalicia.templatemicroservicegenerator.models;

public enum ClassType {
    ENTITY(""),
    DTO("DTO"),
    SERVICE("Service"),
    CONTROLLER("Controller"),
    MAPPER("Mapper"),
    REQUEST_BODY("RequestBody");

    private String classTypeName;

    ClassType(String name) {
        this.classTypeName = name;
    }

    public String getClassTypeName() {
        return classTypeName;
    }
}
