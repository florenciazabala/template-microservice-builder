package com.bancogalicia.templatemicroservicegenerator.service;

import com.bancogalicia.templatemicroservicegenerator.utils.StringUtil;
import io.micrometer.common.util.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PojoProcessor {

    //Guardar en 1 base Mongo? --> id, type, value

    //Agregar package y anotations
    private final String CLASS_TEMPLATE = "%s\n@Data\n" +
            "@JsonIgnoreProperties(ignoreUnknown = true)\n" +
            "@JsonInclude(JsonInclude.Include.NON_NULL)\npublic class %s {\n\n%s\n\n}";
    private  final  String ATTRIBUTE_TEMPLATE  = "@JsonProperty(\"%s\")\n" +
            "private %s %s;";

    //Environment
    private final int ROW_SINCE = 6;

    public String getPojoClass(Sheet sheet, int columnType, int columnValue){
        Map<String, String> attributtes = getAttributtes(sheet,columnType,columnValue);
        List<String> attributesTemplate = attributesTemplate(attributtes);
        return buildClass(new ArrayList<>(),"ExampleClassName",attributesTemplate);
    }
    public Map<String, String> getAttributtes(Sheet sheet, int columnType, int columnValue){
        //Guardar en 1 mapa clave attributte name, valor tipo dato
        Map<String, String> attributtes = new HashMap<>();
        for(int i = ROW_SINCE; i < sheet.getLastRowNum(); i++){
            Row row = sheet.getRow(i);
            String attributeType = row.getCell(columnType).getStringCellValue();
            String attributeName = row.getCell(columnValue).getStringCellValue();
            if(!StringUtils.isBlank(attributeType) && !StringUtils.isBlank(attributeName)){
                attributtes.put(attributeName.trim(),attributeType.trim());
            }
        }
        return attributtes;
    }

    public List<String> attributesTemplate(Map<String, String> attributes){
        List<String> attributesTemplate = new ArrayList<>();
        attributes.forEach(
                (k, v) -> attributesTemplate.add(
                        String.format(ATTRIBUTE_TEMPLATE,k,v, StringUtil.snakeCaseToCammelCase(k)
                        )
                ));
        return attributesTemplate;
    }

    public String buildClass(List<String> imports,String className, List<String> attributes){
        String attributtesFormatted = attributes.stream().map(e -> e + "\n").reduce("", String::concat);
        return String.format(CLASS_TEMPLATE,"",className,attributtesFormatted);
    }
}
