package edu.uci.ics.lillih1.service.api_gateway.threadpool;

import edu.uci.ics.lillih1.service.api_gateway.logger.ServiceLogger;

public class ThreadPool {
    private int numWorkers;
    private Worker[] workers;
    private ClientRequestQueue queue;

    public ThreadPool(int numWorkers) {
        this.numWorkers = numWorkers;
        this.workers = new Worker[numWorkers];

        this.queue = new ClientRequestQueue();

        for (int i=0; i<numWorkers; i++)
        {
            this.workers[i] = Worker.CreateWorker(i, this);
            this.workers[i].start();
        }

        ServiceLogger.LOGGER.config("Created " + numWorkers + " threads.");
    }

    public synchronized void add(ClientRequest clientRequest) {
        queue.enqueue(clientRequest);
        synchronized (queue)
        {
            queue.notifyAll();
        }
    }

    public synchronized ClientRequest remove() {
        while (queue.isEmpty())
        {
            ServiceLogger.LOGGER.config("QUEUE empty. Waiting...");

            synchronized (queue)
            {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        ServiceLogger.LOGGER.config("GOT a queue item");

        return queue.dequeue();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public ClientRequestQueue getQueue() {
        return queue;
    }
}
