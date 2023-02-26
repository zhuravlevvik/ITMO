package queue;

import java.util.Objects;
import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue {
    private Node head, tail;

    public LinkedQueue() {
        head = new Node("head");
        tail = new Node("tail");
        head.next = tail;
        tail.prev = head;
    }

    protected int numberOfConsistentByPredicate(Predicate<Object> predicate) {
        Node node = head.next;
        for (int i = 0; i < size; i++) {
            if (!predicate.test(node.value)) {
                return i;
            }
            node = node.next;
        }
        return size;
    }

    protected void enqueueImpl(Object element) {
        Node node = new Node(element);
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
    }

    protected void popFirst() {
        head = head.next;
    }

    protected void pushImpl(Object element) {
        Node node = new Node(element);
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
        node.prev = head;
    }

    protected Object peekImpl() {
        return tail.prev.value;
    }

    protected void popLast() {
        tail = tail.prev;
    }

    protected Object elementImpl() {
        return head.next.value;
    }

    protected void clearImpl() {
        head.next = tail;
        tail.prev = head;
    }

    protected int firstEntry(Object element) {
        Node node = head.next;
        for (int i = 0; i < size; i++) {
            if (node.value.equals(element)) {
                return i;
            }
            node = node.next;
        }
        return -1;
    }

    protected int lastEntry(Object element) {
        Node node = tail.prev;
        for (int i = size - 1; i >= 0; i--) {
            if (node.value.equals(element)) {
                return i;
            }
            node = node.prev;
        }
        return -1;
    }

    private class Node {
        private final Object value;
        private Node prev, next;

        public Node(Object value) {
            Objects.requireNonNull(value);

            this.value = value;
        }
    }
}
