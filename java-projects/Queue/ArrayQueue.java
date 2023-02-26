package queue;

import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue {
    private Object[] arr = new Object[2];
    private int length = 2, head = 0, tail = 0;

    //Pred: true
    //Post: R: 0 <= R < size && ∀ i in [0..R - 1]: predicate.test(a[i])
    protected int numberOfConsistentByPredicate(Predicate<Object> predicate) {
        for (int i = 0; i < size; i++) {
            if (!predicate.test(arr[(head + i) % length])) {
                return i;
            }
        }
        return size;
    }

    protected void pushImpl(Object x) {
        ensureCapacity(size + 1);
        head = (length + head - 1) % length;
        arr[head] = x;
    }

    protected Object peekImpl() {
        return arr[(tail - 1 + length) % length];
    }

    protected void popLast() {
        tail = (tail - 1 + length) % length;
        arr[tail] = null;
    }

    protected int firstEntry(Object x) {
        for (int i = head, pos = 0; i != tail; i = (i + 1) % length, pos++) {
            if (arr[i].equals(x)) {
                return pos;
            }
        }
        return -1;
    }

    protected int lastEntry(Object x) {
        int i = (tail - 1 + length) % length, pos = size - 1;
        while (i != (head - 1 + length) % length) {
            if (arr[i].equals(x)) {
                return pos;
            }
            pos--;
            i = (i - 1 + length) % length;
        }
        return -1;
    }

    protected void enqueueImpl(Object x) {
        ensureCapacity(size + 1);
        arr[tail] = x;
        tail = (tail + 1) % length;
    }


    protected void popFirst() {
        arr[head] = null;
        head = (head + 1) % length;
    }

    protected Object elementImpl() {
        return arr[head];
    }

    public void clearImpl() {
        arr = new Object[2];
        head = 0;
        tail = 0;
        length = 2;
    }

    //Pred: true
    //Post: immutable(size') && size' == size
    private void ensureCapacity(int size) {
        if (arr.length <= size) {
            Object[] newArr = new Object[2 * length];
            int pos = head, npos = 0;
            while (pos != tail) {
                newArr[npos++] = arr[pos];
                pos = (pos + 1) % length;
            }
            head = 0;
            tail = npos;
            length = 2 * length;
            arr = newArr;
        }
    }
}
