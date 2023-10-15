package com.bancogalicia.templatemicroservicegenerator;

import com.bancogalicia.templatemicroservicegenerator.service.TemplateGeneratorService;
import com.bancogalicia.templatemicroservicegenerator.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

@SpringBootApplication
public class TemplateMicroserviceGeneratorApplication {
	@Autowired
	TemplateGeneratorService templateGeneratorService;


	public static void main(String[] args) {


		SpringApplication.run(TemplateMicroserviceGeneratorApplication.class, args);

		TemplateGeneratorService templateGeneratorService = new TemplateGeneratorService();
		templateGeneratorService.readExcel("C:\\Users\\Florencia\\Downloads\\template-galicia.xlsx","C:\\template-galicia\\pom-idpo-exampletemplate\\src\\main\\java\\ar\\com\\bancogalicia\\pom\\idpo\\exampletemplate");

		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

		int availableProcessors = osBean.getAvailableProcessors(); // Núcleos virtuales
		int physicalCores = Runtime.getRuntime().availableProcessors(); // Núcleos físicos

		System.out.println("Núcleos virtuales: " + availableProcessors);
		System.out.println("Núcleos físicos: " + physicalCores);
	}

}
