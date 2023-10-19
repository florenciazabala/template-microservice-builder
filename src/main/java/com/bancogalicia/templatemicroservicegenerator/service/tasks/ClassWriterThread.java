package com.bancogalicia.templatemicroservicegenerator.service.tasks;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClassWriterThread implements Runnable{

    private ThreadSafeQueue queue;

    private String destinationPath;

    int count = 0;

    public ClassWriterThread(ThreadSafeQueue queue, String destinationPath) {
        this.queue = queue;
        this.destinationPath = destinationPath;
    }

    private boolean isFinish = Boolean.TRUE;
    @Override
    public void run() {
        while (isFinish) {
            String classToSave = queue.remove();

            if (classToSave == null) {
                System.out.println("No more classes to read from the queue, consumer is terminating");
                isFinish = Boolean.FALSE;
            }else{
                createClass(String.format("%s%s%d.java", destinationPath, "\\model\\example", count), classToSave);
            }

            if (count == 5) { //En este caso 1 clase x hoja
                queue.terminate();
                System.out.println("No more sheets to read");
            }

            count++;
        }

    }

    private void createClass(String to, String classToCreate){
        Path path = Paths.get(to);
        try {
            Files.writeString(path, classToCreate, StandardCharsets.UTF_8);
            System.out.println("Create class " + to);
        } catch (IOException ex) {
            System.out.print("Invalid Path");
        }

    }
}
