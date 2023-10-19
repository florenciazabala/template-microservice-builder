package com.bancogalicia.templatemicroservicegenerator;

import com.bancogalicia.templatemicroservicegenerator.service.TemplateGeneratorService;
import com.bancogalicia.templatemicroservicegenerator.service.TemplateGeneratorServiceImpl;
import com.bancogalicia.templatemicroservicegenerator.service.TemplateGeneratorServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

@SpringBootApplication
@Component
public class TemplateMicroserviceGeneratorApplication {

	@Autowired
	static TemplateGeneratorServiceImpl templateGeneratorServiceImpl;

	static ApplicationContext context;

	public static void main(String[] args) {


		//SpringApplication.run(TemplateMicroserviceGeneratorApplication.class, args);

		context = SpringApplication.run(TemplateMicroserviceGeneratorApplication.class, args);
		templateGeneratorServiceImpl = (TemplateGeneratorServiceImpl) context.getBean("templateGeneratorServiceImpl");

		TemplateGeneratorService templateGeneratorService = new TemplateGeneratorServiceProxy(templateGeneratorServiceImpl);

		/*The number of logical cores is the number of physical cores multiplied with the number of threads you can run on each of them. On a Hexacore with 2 Threads per Core this would be 12 logical cores then afaik. 6 logical processors though*/
		//private Integer virtualCores = Runtime.getRuntime().availableProcessors();

		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

		int availableProcessors = osBean.getAvailableProcessors(); // Núcleos virtuales
		int physicalCores = Runtime.getRuntime().availableProcessors(); // Núcleos físicos

		System.out.println("Núcleos virtuales: " + availableProcessors);
		System.out.println("Núcleos físicos: " + physicalCores);

		final int NUMBER_OF_THREADS = physicalCores / 4;

		templateGeneratorService.readExcel("C:\\Users\\Florencia\\Downloads\\template-galicia.xlsx","C:\\template-galicia\\pom-idpo-exampletemplate\\src\\main\\java\\ar\\com\\bancogalicia\\pom\\idpo\\exampletemplate", NUMBER_OF_THREADS);

	}

}
