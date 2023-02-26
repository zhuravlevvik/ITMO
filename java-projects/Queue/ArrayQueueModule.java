package queue;

/*
    Model: a[0]..a[size - 1] && size > 0 || size == 0
    Invariant: size > 0 && for i in [0..size - 1] : a[i] != null || size == 0
    Let immutable(n) == ∀ i in [0..n - 1] arr'[i] == arr[i]
 */

import java.util.Objects;

public class ArrayQueueModule {
    private static Object[] arr = new Object[2];
    private static int size = 0, length = 2, head = 0, tail = 0;

    //Pred: element != null
    //Post: immutable(size') && size' == size + 1 && arr'[0] == element
    public static void push(Object x) {
        Objects.requireNonNull(x);
        ensureCapacity(size + 1);
        head = (length + head - 1) % length;
        arr[head] = x;
        size++;
    }

    //Pred: size > 0
    //Post: immutable(size') && size' == size && R == arr[size' - 1]
    public static Object peek() {
        assert size > 0;
        return arr[(tail - 1 + length) % length];
    }

    //Pred: size > 0
    //Post: immutable(size') && size' == size - 1 && R == arr[size']
    public static Object remove() {
        assert size > 0;
        tail = (tail - 1 + length) % length;
        Object result = arr[tail];
        arr[tail] = null;
        size--;
        return result;
    }

    //Pred: x != null
    //Post: immutable(size') && size == size' && R: (R == -1 && !∃ pos in [0..size - 1]: arr[pos] == x
    // || R in [0..size - 1] && arr[R] == x && ∀ i < R && i >= 0 : arr[i] != x)
    public static int indexOf(Object x) {
        Objects.requireNonNull(x);
        if (size <= 0) {
            return -1;
        }
        for (int i = head, pos = 0; i != tail; i = (i + 1) % length, pos++) {
            if (arr[i].equals(x)) {
                return pos;
            }
        }
        return -1;
    }

    //Pred: x != null && size > 0
    //Post: immutable(size') && size' == size && R: (R == -1 && !∃ pos: arr[pos] == x
    // || arr[R] == x && ∀ i > R && i < size : arr[i] != x)
    public static int lastIndexOf(Object x) {
        Objects.requireNonNull(x);
        if (size <= 0) {
            return -1;
        }
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

    //Pred: element != null
    //Post: immutable(n) && size' == size + 1 && arr'[size] = element
    public static void enqueue(Object x) {
        Objects.requireNonNull(x);
        ensureCapacity(size + 1);
        arr[tail] = x;
        tail = (tail + 1) % length;
        size++;
    }

    //Pred: size > 0
    //Post: size' == size - 1 && immutable(size') && R == arr[head]
    public static Object dequeue() {
        assert size > 0;
        Object res = arr[head];
        arr[head] = null;
        head = (head + 1) % length;
        size--;
        return res;
    }

    //Pred: size > 0
    //Post: size' == size && immutable(size') && R: R == arr[0]
    public static Object element() {
        assert size > 0;
        return arr[head];
    }

    //Pred: true
    //Post: size' == 0
    public static void clear() {
        arr = new Object[2];
        head = 0;
        tail = 0;
        size = 0;
        length = 2;
    }

    //Pred: true
    //Post: R == size && size' == size && immutable(size')
    public static int size() {
        return size;
    }

    //Pred: true
    //Post: R == (size == 0) && size' == size && immutable(size')
    public static boolean isEmpty() {
        return size == 0;
    }

    //Pred: true
    //Post: immutable(size') && size' == size
    private static void ensureCapacity(int size) {
        if (arr.length <= size) {
            Object[] newArr = new Object[2 * length];
            int pos = head, npos = 0;
            while (pos != tail) {
                newArr[npos++] = arr[pos];
                pos = (pos + 1) % length;
            }
            head = 0;
            tail = npos;
            length *= 2;
            arr = newArr;
        }
    }
}
