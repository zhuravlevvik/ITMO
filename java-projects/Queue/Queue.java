package queue;

import java.util.function.Predicate;

/*
    Model: a[0]..a[size - 1] && size > 0 || size == 0
    Invariant: size > 0 && for i in [0..size - 1] : a[i] != null || size == 0
    Let immutable(n) == ∀ i in [0..n - 1] arr'[i] == arr[i]
    Let contains(arr, x): ∃ pos in [0..arr.size - 1]: arr[pos] == x
    Let numNotSatisfy(predicate): R: ∃ {j_i} i in [0..R - 1] ∀ i 0 <= j_(i - 1) < j_i <= size  && !predicate.test(arr[j_i])
    Let numConsistByPred(predicate): R: ∀ i in [0..R - 1] predicate.test(arr[i]) && (i + 1 == size || !predicate.test(arr[i + 1])
 */

public interface Queue{

    //Pred: element != null
    //Post: immutable(size') && size' == size + 1 && arr'[0] == element
    void push(Object element);

    //Pred: size > 0
    //Post: size' == size - 1 && immutable(size') && R == arr[head]
    Object dequeue();

    //Pred: size > 0
    //Post: size' == size && immutable(size') && R: R == arr[0]
    Object element();

    //Pred: size > 0
    //Post: immutable(size') && size' == size - 1 && R == arr[size']
    Object remove();

    //Pred: size > 0
    //Post: immutable(size') && size' == size && R == arr[size' - 1]
    Object peek();

    //Pred: element != null
    //Post: immutable(size') && size' == size + 1 && arr'[size] = element
    void enqueue(Object element);

    //Pred: x != null
    //Post: immutable(size') && size == size' && R: (R == -1 && !∃ pos in [0..size - 1]: arr[pos] == x
    // || R in [0..size - 1] && arr[R] == x && ∀ i < R && i >= 0 : arr[i] != x)
    int indexOf(Object element);

    //Pred: x != null && size > 0
    //Post: immutable(size') && size' == size && R: (R == -1 && !∃ pos: arr[pos] == x
    // || arr[R] == x && ∀ i > R && i < size : arr[i] != x)
    int lastIndexOf(Object element);

    //Pred: true
    //Post: size' == 0
    void clear();

    //Pred: true
    //Post: R == size && size' == size && immutable(size')
    int size();

    //Pred: true
    //Post: R == (size == 0) && size' == size && immutable(size')
    boolean isEmpty();

    //Pred: true
    //Post: size' == size - numNotSatisfy(!predicate)
    // && ∃ {j_i} for i in [0..size' - 1]: 0 <= j_0 < .. < j_(size' - 1) < size && ∀ i in [0..size' - 1] !predicate.test(arr[j_i])
    void removeIf(Predicate<Object> predicate);

    //Pred: true
    //Post: size' == size - numNotSatisfy(predicate)
    // && ∃ {j_i} for i in [0..size' - 1]: 0 <= j_0 < .. < j_(size' - 1) < size && ∀ i in [0..size' - 1] predicate.test(arr[j_i])
    void retainIf(Predicate<Object> predicate);

    //Pred: true
    //Post: immutable(size') && size' == numConsistByPred(predicate)
    void takeWhile(Predicate<Object> predicate);

    //Pred: true
    // let num = numConsistByPred(predicate)
    //Post: size' == size - num && ∀ i in [0..num - 1]: arr'[i] = arr[num + i]
    void dropWhile(Predicate<Object> predicate);
}
