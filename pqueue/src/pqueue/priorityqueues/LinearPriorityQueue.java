package pqueue.priorityqueues;

import pqueue.exceptions.InvalidCapacityException;
import pqueue.exceptions.InvalidPriorityException;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class LinearPriorityQueue<T> implements PriorityQueue<T> {

    private LinkedList<Qnode> queue;
    private int modifyCounter = 0;

    private class Qnode {
        T data;
        int priority;

        public Qnode(T data, int priority) {
            this.data = data;
            this.priority = priority;
        }
    }

    public LinearPriorityQueue() {
        this.queue = new LinkedList<>();
    }

    public LinearPriorityQueue(int capacity) throws InvalidCapacityException {
        if (capacity < 1) {
            throw new InvalidCapacityException("Invalid Capacity");
        } else {
            queue = new LinkedList<>();
        }
    }

    @Override
    public void enqueue(T element, int priority) throws InvalidPriorityException {
        if (priority <= 0) {
            throw new InvalidPriorityException("Invalid Priority Number");
        } else {
            modifyCounter++;
            ListIterator<Qnode> iterator = queue.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next().priority > priority) {
                    iterator.previous();
                    iterator.add(new Qnode(element, priority));
                    return;
                }
            }
            queue.add(new Qnode(element, priority));
        }
    }

    @Override
    public T dequeue() throws EmptyPriorityQueueException {
        if (this.queue.isEmpty()) {
            throw new EmptyPriorityQueueException("Empty Queue");
        } else {
            modifyCounter++;
            return queue.removeFirst().data;
        }
    }

    @Override
    public T getFirst() throws EmptyPriorityQueueException {
        if (this.queue.isEmpty()) {
            throw new EmptyPriorityQueueException("Empty Queue");
        } else {
            return queue.getFirst().data;
        }
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return new LinearPQIterator();
    }

    private class LinearPQIterator implements Iterator<T> {
        private ListIterator<Qnode> iterator = queue.listIterator();
        private int currentMod = modifyCounter;

        @Override
        public T next() throws ConcurrentModificationException {
            if (currentMod != modifyCounter) {
                throw new ConcurrentModificationException();
            }
            return iterator.next().data;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }
    }
}
