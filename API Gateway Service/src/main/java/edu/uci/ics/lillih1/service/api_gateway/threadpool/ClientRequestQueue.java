package edu.uci.ics.lillih1.service.api_gateway.threadpool;

public class ClientRequestQueue {
    private ListNode head;
    private ListNode tail;

    public ClientRequestQueue() {
        this.head = null;
        this.tail = null;
    }

    public synchronized void enqueue(ClientRequest clientRequest) {
        ListNode node = new ListNode(clientRequest,null);
        if (isEmpty())
        {
            this.head = node;
            this.tail = node;
        }
        else
        {
            this.tail.setNext(node);
            this.tail = node;
        }

        synchronized (this) {
            this.notifyAll();
        }
    }

    public synchronized ClientRequest dequeue() {

        if (this.head == null)
            return null;

        ListNode temp = this.head;
        this.head = this.head.getNext();

        if (this.head == null)
            this.tail = null;

        return temp.getClientRequest();

    }

    boolean isEmpty() {
        if(head == null && tail == null)
        {
            return true;
        }
        return false;
    }

    boolean isFull() {
        return false;
    }
}
