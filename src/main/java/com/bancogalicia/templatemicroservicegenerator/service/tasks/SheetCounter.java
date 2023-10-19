package com.bancogalicia.templatemicroservicegenerator.service.tasks;

public class SheetCounter {

    private int items = 0;

    Object lock = new Object();

    public void increment(){
        synchronized (this.lock){
            items++;
            System.out.println("Sheets count: "+items);
        }
    }

    public int getItems(){
        synchronized (this.lock){
            return items;
        }
    }
}
