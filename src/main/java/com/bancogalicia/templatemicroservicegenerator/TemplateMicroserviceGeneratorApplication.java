package com.bancogalicia.templatemicroservicegenerator;

import com.bancogalicia.templatemicroservicegenerator.service.TemplateGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
public class TemplateMicroserviceGeneratorApplication {

	@Autowired
	static TemplateGeneratorService templateGeneratorService;
	static ApplicationContext context;

	public static void main(String[] args) {


		//SpringApplication.run(TemplateMicroserviceGeneratorApplication.class, args);

		// added this - get reference to application context
		context = SpringApplication.run(TemplateMicroserviceGeneratorApplication.class, args);
		// added this - get the object via the context as a bean
		templateGeneratorService = (TemplateGeneratorService) context.getBean("templateGeneratorService");

		templateGeneratorService.readExcel("C:\\Users\\Florencia\\Downloads\\template-galicia.xlsx","C:\\template-galicia\\pom-idpo-exampletemplate\\src\\main\\java\\ar\\com\\bancogalicia\\pom\\idpo\\exampletemplate");

		/*OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

		int availableProcessors = osBean.getAvailableProcessors(); // Núcleos virtuales
		int physicalCores = Runtime.getRuntime().availableProcessors(); // Núcleos físicos

		System.out.println("Núcleos virtuales: " + availableProcessors);
		System.out.println("Núcleos físicos: " + physicalCores);*/
	}

}
