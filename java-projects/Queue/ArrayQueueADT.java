package queue;

/*
    Model: a[0]..a[size - 1] && size > 0 || size == 0
    Invariant: size > 0 && for i in [0..size - 1] : a[i] != null || size == 0
    Let immutable(n) == ∀ i in [0..n - 1] arr'[i] == arr[i]
 */

import java.util.Objects;

public class ArrayQueueADT {
    private Object[] arr = new Object[2];
    private int size = 0, length = 2, head = 0, tail = 0;

    //Pred: element != null && queue != null
    //Post: immutable(size') && size' == size + 1 && arr'[0] == element
    public static void push(ArrayQueueADT queue, Object x) {
        Objects.requireNonNull(x);
        Objects.requireNonNull(queue);
        ensureCapacity(queue,queue.size + 1);
        queue.head = (queue.length + queue.head - 1) % queue.length;
        queue.arr[queue.head] = x;
        queue.size++;
    }

    //Pred: size > 0 && queue != null
    //Post: immutable(size') && size' == size && R == arr[size' - 1]
    public static Object peek(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;
        return queue.arr[(queue.tail - 1 + queue.length) % queue.length];
    }

    //Pred: size > 0 && queue != null
    //Post: immutable(size') && size' == size - 1 && R == arr[size']
    public static Object remove(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;
        queue.tail = (queue.tail - 1 + queue.length) % queue.length;
        Object result = queue.arr[queue.tail];
        queue.arr[queue.tail] = null;
        queue.size--;
        return result;
    }

    //Pred: x != null && queue != null
//Post: immutable(size') && size == size' && R: (R == -1 && !∃ pos in [0..size - 1]: arr[pos] == x
// || R in [0..size - 1] && arr[R] == x && ∀ i < R && i >= 0 : arr[i] != x)
    public static int indexOf(ArrayQueueADT queue, Object x) {
        Objects.requireNonNull(queue);
        Objects.requireNonNull(x);
        if (queue.size <= 0) {
            return -1;
        }
        for (int i = queue.head, pos = 0; i != queue.tail; i = (i + 1) % queue.length, pos++) {
            if (queue.arr[i].equals(x)) {
                return pos;
            }
        }
        return -1;
    }

    //Pred: x != null && size > 0 && queue != null
    //Post: immutable(size') && size' == size && R: (R == -1 && !∃ pos: arr[pos] == x
    // || arr[R] == x && ∀ i > R && i < size : arr[i] != x)
    public static int lastIndexOf(ArrayQueueADT queue, Object x) {
        Objects.requireNonNull(queue);
        Objects.requireNonNull(x);
        if (queue.size <= 0) {
            return -1;
        }
        int i = (queue.tail - 1 + queue.length) % queue.length, pos = queue.size - 1;
        while (i != (queue.head - 1 + queue.length) % queue.length) {
            if (queue.arr[i].equals(x)) {
                return pos;
            }
            pos--;
            i = (i - 1 + queue.length) % queue.length;
        }
        return -1;
    }

    //Pred: element != null && queue != null
    //Post: immutable(n) && size' == size + 1 && arr'[size] = element
    public static void enqueue(ArrayQueueADT queue, Object x) {
        Objects.requireNonNull(queue);
        Objects.requireNonNull(x);
        ensureCapacity(queue, queue.size + 1);
        queue.arr[queue.tail] = x;
        queue.tail = (queue.tail + 1) % queue.length;
        queue.size++;
    }

    //Pred: size > 0 && queue != null
    //Post: size' == size - 1 && immutable(size') && R == arr[head]
    public static Object dequeue(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;
        Object res = queue.arr[queue.head];
        queue.arr[queue.head] = null;
        queue.head = (queue.head + 1) % queue.length;
        queue.size--;
        return res;
    }

    //Pred: size > 0 && queue != null
    //Post: size' == size && immutable(size') && R: R == arr[0]
    public static Object element(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;
        return queue.arr[queue.head];
    }

    //Pred: queue != null
    //Post: size' = 0
    public static void clear(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        queue.arr = new Object[2];
        queue.head = 0;
        queue.tail = 0;
        queue.size = 0;
        queue.length = 2;
    }

    //Pred: queue != null
    //Post: R == size && size' == size && immutable(size')
    public static int size(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        return queue.size;
    }

    //Pred: queue != null
    //Post: R == (size == 0) && size' == size && immutable(size')
    public static boolean isEmpty(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        return queue.size == 0;
    }

    //Pred: queue != null
    //Post: immutable(size') && size' == size
    private static void ensureCapacity(ArrayQueueADT queue, int size) {
        Objects.requireNonNull(queue);
        if (queue.arr.length <= size) {
            Object[] newArr = new Object[2 * queue.length];
            int pos = queue.head, npos = 0;
            while (pos != queue.tail) {
                newArr[npos++] = queue.arr[pos];
                pos = (pos + 1) % queue.length;
            }
            queue.head = 0;
            queue.tail = npos;
            queue.length = 2 * queue.length;
            queue.arr = newArr;
        }
    }

    //Pred: true
    //Post: Q = new ArrayQueueADT
    public static ArrayQueueADT create() {
        ArrayQueueADT queueADT = new ArrayQueueADT();
        queueADT.arr = new Object[2];
        return queueADT;
    }
}
