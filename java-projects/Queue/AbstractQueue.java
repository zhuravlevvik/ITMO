package queue;

import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    public void removeIf(Predicate<Object> predicate) {
        for (int i = 0; i < size; i++) {
            Object element = dequeue();
            if (!predicate.test(element)) {
                enqueue(element);
            } else {
                i--;
            }
        }
    }

    public void retainIf(Predicate<Object> predicate) {
        removeIf(predicate.negate());
    }

    public void takeWhile(Predicate<Object> predicate) {
        int num = numberOfConsistentByPredicate(predicate);
        while (size > num) {
            remove();
        }
    }

    public void dropWhile(Predicate<Object> predicate) {
        int num = numberOfConsistentByPredicate(predicate), newSize = size - num;
        while (size > newSize) {
            dequeue();
        }
    }

    protected abstract int numberOfConsistentByPredicate(Predicate<Object> predicate);

    public Object peek() {
        assert size > 0;

        return peekImpl();
    }

    protected abstract Object peekImpl();

    public void push(Object element) {
        assert element != null;

        pushImpl(element);
        size++;
    }

    protected abstract void pushImpl(Object element);

    public Object remove() {
        assert size > 0;

        Object result = peek();
        popLast();
        size--;
        return result;
    }

    protected abstract void popFirst();
    protected abstract void popLast();

    public Object dequeue() {
        assert size > 0;

        Object result = element();
        popFirst();
        size--;
        return result;
    }

    public void clear() {
        size = 0;

        clearImpl();
    }

    protected abstract void clearImpl();

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object element() {
        assert size > 0;

        return elementImpl();
    }

    protected abstract Object elementImpl();

    public void enqueue(Object element) {
        assert element != null;

        size++;
        enqueueImpl(element);
    }

    protected abstract void enqueueImpl(Object element);

    public int indexOf(Object element) {
        assert element != null;

        return firstEntry(element);
    }

    protected abstract int firstEntry(Object element);

    public int lastIndexOf(Object element) {
        assert element != null;

        return lastEntry(element);
    }

    protected abstract int lastEntry(Object element);
}
