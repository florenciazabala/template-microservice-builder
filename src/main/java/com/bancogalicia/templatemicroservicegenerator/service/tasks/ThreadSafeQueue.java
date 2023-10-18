package com.bancogalicia.templatemicroservicegenerator.service.tasks;

import java.util.LinkedList;
import java.util.Queue;

public class ThreadSafeQueue {
    private Queue<String> queue = new LinkedList<>();
    private boolean isEmpty = true;
    private boolean isTerminate = false;
    private static final int CAPACITY = 2;

    public synchronized void add(String classToCreate) {
        while (queue.size() == CAPACITY) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        queue.add(classToCreate);
        isEmpty = false;
        notify();
    }

    public synchronized String remove() {
        String classToCreate = null;
        while (isEmpty && !isTerminate) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        if (queue.size() == 1) {
            isEmpty = true;
        }

        if (queue.size() == 0 && isTerminate) {
            return null;
        }

        System.out.println("queue size " + queue.size());

        classToCreate = queue.remove();
        if (queue.size() == CAPACITY - 1) {
            notifyAll();
        }
        return classToCreate;
    }

    public synchronized void terminate() {
        isTerminate = true;
        notifyAll();
    }
}
