package edu.uci.ics.lillih1.service.api_gateway.threadpool;

import edu.uci.ics.lillih1.service.api_gateway.core.DispatchClientRequest;
import edu.uci.ics.lillih1.service.api_gateway.logger.ServiceLogger;

public class Worker extends Thread {
    int id;
    ThreadPool threadPool;

    private Worker(int id, ThreadPool threadPool) {
        this.id = id;
        this.threadPool = threadPool;
    }

    public static Worker CreateWorker(int id, ThreadPool threadPool) {
        return new Worker(id, threadPool);
    }

    public void process(ClientRequest request, int id) {
        try {
            DispatchClientRequest.dispatchClientRequest(request, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true)
        {
            ClientRequest request = threadPool.remove();

            ServiceLogger.LOGGER.config("Thread " + this.id + " work on transaction " + request.getTransactionID() +
                    "for email: " + request.getEmail() + ", URI: " + request.getURI() + ", Endpoint: " + request.getEndpoint());
            process(request, id);
        }
    }
}
