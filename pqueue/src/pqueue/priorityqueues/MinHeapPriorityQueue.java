package pqueue.priorityqueues; // ******* <---  DO NOT ERASE THIS LINE!!!! *******


/* *****************************************************************************************
 * THE FOLLOWING IMPORTS WILL BE NEEDED BY YOUR CODE, BECAUSE WE REQUIRE THAT YOU USE
 * ANY ONE OF YOUR EXISTING MINHEAP IMPLEMENTATIONS TO IMPLEMENT THIS CLASS. TO ACCESS
 * YOUR MINHEAP'S METHODS YOU NEED THEIR SIGNATURES, WHICH ARE DECLARED IN THE MINHEAP
 * INTERFACE. ALSO, SINCE THE PRIORITYQUEUE INTERFACE THAT YOU EXTEND IS ITERABLE, THE IMPORT OF ITERATOR
 * IS NEEDED IN ORDER TO MAKE YOUR CODE COMPILABLE. THE IMPLEMENTATIONS OF CHECKED EXCEPTIONS
 * ARE ALSO MADE VISIBLE BY VIRTUE OF THESE IMPORTS.
 ** ********************************************************************************* */


import pqueue.exceptions.*;
import pqueue.heaps.ArrayMinHeap;
import pqueue.heaps.EmptyHeapException;
import pqueue.heaps.MinHeap;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
/**
 * <p>{@link MinHeapPriorityQueue} is a {@link PriorityQueue} implemented using a {@link MinHeap}.</p>
 *
 * <p>You  <b>must</b> implement the methods of this class! To receive <b>any credit</b> for the unit tests
 * related to this class, your implementation <b>must</b> use <b>whichever</b> {@link MinHeap} implementation
 * among the two that you should have implemented you choose!</p>
 *
 * @author  ---- Luke Zic----
 *
 * @param <T> The Type held by the container.
 *
 * @see LinearPriorityQueue
 * @see MinHeap
 * @see PriorityQueue
 */

public class MinHeapPriorityQueue<T> implements PriorityQueue<T> {

    private ArrayMinHeap<PriorityNode> minHeap;
    private int modificationCount = 0;

    private class PriorityNode implements Comparable<PriorityNode> {
        protected T data;
        protected int priority;

        public PriorityNode(T data, int priority) {
            this.data = data;
            this.priority = priority;
        }

        @Override
        public int compareTo(PriorityNode other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    public MinHeapPriorityQueue() {
        this.minHeap = new ArrayMinHeap<>();
    }

    @Override
    public void enqueue(T element, int priority) throws InvalidPriorityException {
        if (priority <= 0) {
            throw new InvalidPriorityException("Invalid Priority Number");
        }
        modificationCount++;
        minHeap.insert(new PriorityNode(element, priority));
    }

    @Override
    public T dequeue() throws EmptyPriorityQueueException {
        checkEmpty();
        modificationCount++;
        try {
            return minHeap.deleteMin().data;
        } catch (EmptyHeapException e) {
            throw new EmptyPriorityQueueException("Empty Queue for dequeue");
        }
    }

    @Override
    public T getFirst() throws EmptyPriorityQueueException {
        checkEmpty();
        try {
			return minHeap.getMin().data;
		} catch (EmptyHeapException e) {
			throw new EmptyPriorityQueueException("Empty Queue for getFirst");
		}
    }

    @Override
    public int size() {
        return minHeap.size();
    }

    @Override
    public boolean isEmpty() {
        return minHeap.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return new PriorityQueueIterator();
    }

    private void checkEmpty() throws EmptyPriorityQueueException {
        if (minHeap.isEmpty()) {
            throw new EmptyPriorityQueueException("Empty Queue");
        }
    }

    private class PriorityQueueIterator implements Iterator<T> {
        Iterator<PriorityNode> iterator = minHeap.iterator();
        int expectedModificationCount = modificationCount;

        @Override
        public boolean hasNext() {
            if (expectedModificationCount != modificationCount) {
                throw new ConcurrentModificationException();
            }
            return iterator.hasNext();
        }

        @Override
        public T next() {
            if (expectedModificationCount != modificationCount) {
                throw new ConcurrentModificationException();
            }
            return iterator.next().data;
        }
    }
}
