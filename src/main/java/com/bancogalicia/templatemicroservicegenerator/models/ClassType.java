package com.bancogalicia.templatemicroservicegenerator.models;

public enum ClassType {
    ENTITY("pojo_class_name"),
    DTO("dto_class_name"),
    SERVICE("service_class_name"),
    CONTROLLER("controller_class_name"),
    MAPPER("mapper_class_name"),
    REQUEST_BODY("request_body_class_name");

    private String classTypeName;

    ClassType(String name) {
        this.classTypeName = name;
    }

    public String getClassTypeName() {
        return classTypeName;
    }
}
