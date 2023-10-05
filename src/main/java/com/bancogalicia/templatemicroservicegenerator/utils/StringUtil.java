package com.bancogalicia.templatemicroservicegenerator.utils;

import java.util.regex.Pattern;

public class StringUtil {

    public static String snakeCaseToCammelCase(String snakeCase){
        //return snakeCase.toLowerCase().replaceAll("/_/g", (match) => `_${match}`);
        return Pattern.compile("[_]+\\w")
                .matcher(snakeCase)
                .replaceAll(matche -> String.valueOf(matche.group().charAt(1)).toUpperCase());
    }

    public static String cammelCaseToSnakeCase(String cammelCase){
        return cammelCase.replaceAll("([A-Z])", "_$1").toLowerCase();
    }
}
