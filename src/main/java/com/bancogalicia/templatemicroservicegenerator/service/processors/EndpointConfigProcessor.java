package com.bancogalicia.templatemicroservicegenerator.service.processors;

import com.bancogalicia.templatemicroservicegenerator.models.AuthenticationMethod;
import com.bancogalicia.templatemicroservicegenerator.models.EndpointConfig;
import com.bancogalicia.templatemicroservicegenerator.models.MethodType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EndpointConfigProcessor {

    private final int ROW_SINCE = 6;
    public EndpointConfig getBasicConfig(Sheet sheet){

        EndpointConfig endpointConfig = new EndpointConfig();
        endpointConfig.setClassName(getCellValue(sheet,1));
        endpointConfig.setMethodType(MethodType.valueOf(getCellValue(sheet,2)));
        endpointConfig.setBasePath(getCellValue(sheet,3));
        endpointConfig.setEndpoints(Arrays.asList(getCellValue(sheet,4)));
        endpointConfig.setAutenticationMethod(AuthenticationMethod.valueOf(getCellValue(sheet,5)));

        return endpointConfig;
    }

    private String getCellValue(Sheet sheet,int column){
        Row row = sheet.getRow(ROW_SINCE);
        String attributeType = row.getCell(column).getStringCellValue();
        return attributeType;
    }
}
