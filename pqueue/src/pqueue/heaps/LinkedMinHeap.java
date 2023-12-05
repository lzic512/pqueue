
package pqueue.heaps; // ******* <---  DO NOT ERASE THIS LINE!!!! *******

/* *****************************************************************************************
 * THE FOLLOWING IMPORT IS NECESSARY FOR THE ITERATOR() METHOD'S SIGNATURE. FOR THIS
 * REASON, YOU SHOULD NOT ERASE IT! YOUR CODE WILL BE UNCOMPILABLE IF YOU DO!
 * ********************************************************************************** */


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ConcurrentModificationException;

/**
 * <p>A {@link LinkedMinHeap} is a tree (specifically, a <b>complete</b> binary tree) where every node is
 * smaller than or equal to its descendants (as defined by the {@link Comparable#compareTo(Object)} overridings of the type T).
 * Percolation is employed when the root is deleted, and insertions guarantee maintenance of the heap property in logarithmic time. </p>
 *
 * <p>You <b>must</b> edit this class! To receive <b>any</b> credit for the unit tests related to this class,
 * your implementation <b>must</b> be a &quot;linked&quot;, <b>non-contiguous storage</b> implementation based on a
 * binary tree of nodes and references. Use the skeleton code we have provided to your advantage, but always remember
 * that the only functionality our tests can test is {@code public} functionality.</p>
 * 
 * @author --- Luke Zic ---
 *
 * @param <T> The {@link Comparable} type of object held by {@code this}.
 *
 * @see MinHeap
 * @see ArrayMinHeap
 */


public class LinkedMinHeap<T extends Comparable<T>> implements MinHeap<T> {

    private class Node {
        private T data;
        private Node leftChild, rightChild;
        private int index;
    }

    private Node root;
    private int size;
    private int modificationCount = 0;

    public LinkedMinHeap() {
        this.root = null;
        this.size = 0;
    }

    public LinkedMinHeap(T rootElement) {
        this.root = new Node();
        this.root.data = rootElement;
        this.size = 1;
    }

    public LinkedMinHeap(MinHeap<T> other) {
        this.size = ((LinkedMinHeap<T>) other).size;
        this.root = copyConstructorHelper(this.root, ((LinkedMinHeap<T>) other).root, 0);
    }

    protected Node copyConstructorHelper(Node currNode, Node corresNode, int index) {
        if (corresNode == null) {
            return null;
        } else {
            currNode = new Node();
            currNode.data = corresNode.data;
            currNode.index = index;
            currNode.leftChild = copyConstructorHelper(currNode.leftChild, corresNode.leftChild, 2 * index + 1);
            currNode.rightChild = copyConstructorHelper(currNode.rightChild, corresNode.rightChild, 2 * index + 2);
            return currNode;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MinHeap))
            return false;
        Iterator<T> iteratorThis = iterator();
        Iterator<?> iteratorOther = ((MinHeap<?>) other).iterator();
        while (iteratorThis.hasNext())
            if (!iteratorThis.next().equals(iteratorOther.next()))
                return false;
        return !iteratorOther.hasNext();
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void insert(T element) {
        modificationCount++;
        Node newNode = new Node();
        newNode.data = element;
        newNode.index = size;
        if (root == null) {
            root = newNode;
        } else {
            insertNode(root, newNode);
        }
        size++;
    }

    private void insertNode(Node current, Node newNode) {
        if (current.leftChild == null) {
            current.leftChild = newNode;
        } else if (current.rightChild == null) {
            current.rightChild = newNode;
        } else {
            // Choose the side with fewer nodes
            if (countNodes(current.leftChild) <= countNodes(current.rightChild)) {
                insertNode(current.leftChild, newNode);
            } else {
                insertNode(current.rightChild, newNode);
            }
        }
        heapifyUp(newNode);
    }

    private int countNodes(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.leftChild) + countNodes(node.rightChild);
    }

    private void heapifyUp(Node node) {
        while (node.index > 0 && node.data.compareTo(getParent(node).data) < 0) {
            swapNodes(node, getParent(node));
            node = getParent(node);
        }
    }

    private Node getParent(Node node) {
        return node.index % 2 == 0 ? getNode((node.index - 2) / 2).rightChild : getNode((node.index - 1) / 2).leftChild;
    }

    private Node getNode(int index) {
        ArrayList<Node> nodeList = new ArrayList<>();
        flattenHeap(root, nodeList);
        return index < nodeList.size() ? nodeList.get(index) : null;
    }

    private void flattenHeap(Node node, ArrayList<Node> nodeList) {
        if (node != null) {
            nodeList.add(node);
            flattenHeap(node.leftChild, nodeList);
            flattenHeap(node.rightChild, nodeList);
        }
    }

    private void swapNodes(Node node1, Node node2) {
        Collections.swap(nodeList(node1), node1.index, node2.index);
        int temp = node1.index;
        node1.index = node2.index;
        node2.index = temp;
    }

    private ArrayList<Node> nodeList(Node root) {
        ArrayList<Node> list = new ArrayList<>();
        flattenHeap(root, list);
        return list;
    }

    @Override
    public T getMin() throws EmptyHeapException {
        if (isEmpty()) {
            throw new EmptyHeapException("Empty Heap for getMin");
        }
        return root.data;
    }

    @Override
    public T deleteMin() throws EmptyHeapException {
        if (isEmpty()) {
            throw new EmptyHeapException("Empty Linked Heap");
        }
        modificationCount++;
        T minValue = root.data;
        Node lastNode = getNode(size - 1);
        if (size > 1) {
            swapNodes(root, lastNode);
            removeLastNode(getParent(lastNode));
            heapifyDown(root);
        } else {
            root = null;
        }
        size--;
        return minValue;
    }

    private void removeLastNode(Node parent) {
        if (parent != null) {
            if (parent.leftChild != null && parent.leftChild.index == size - 1) {
                parent.leftChild = null;
            } else if (parent.rightChild != null && parent.rightChild.index == size - 1) {
                parent.rightChild = null;
            }
        }
    }

    private void heapifyDown(Node node) {
        while (true) {
            Node smallest = node;
            if (node.leftChild != null && node.leftChild.data.compareTo(smallest.data) < 0) {
                smallest = node.leftChild;
            }
            if (node.rightChild != null && node.rightChild.data.compareTo(smallest.data) < 0) {
                smallest = node.rightChild;
            }
            if (smallest != node) {
                swapNodes(node, smallest);
            } else {
                break;
            }
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new HeapIterator(root);
    }

    private class HeapIterator implements Iterator<T> {
        private Node current;
        private int remainingNodes;
        private int expectedModificationCount;

        public HeapIterator(Node root) {
            this.current = copyNode(root, 0);
            this.remainingNodes = size;
            this.expectedModificationCount = modificationCount;
        }

        @Override
        public boolean hasNext() {
            if (expectedModificationCount != modificationCount) {
                throw new ConcurrentModificationException();
            }
            return remainingNodes > 0;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new ConcurrentModificationException();
            }
            T result = current.data;
            current = getNextNode(current.index);
            remainingNodes--;
            return result;
        }

        private Node getNextNode(int index) {
            if (index + 1 < size) {
                return getNode(index + 1);
            }
            return null;


        }

        private Node copyNode(Node node, int index) {
            if (node == null) {
                return null;
            }
            Node copy = new Node();
            copy.data = node.data;
            copy.index = index;
            copy.leftChild = copyNode(node.leftChild, 2 * index + 1);
            copy.rightChild = copyNode(node.rightChild, 2 * index + 2);
            return copy;
        }
    }
}
