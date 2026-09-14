package container;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class GenPriorityQueue<E extends Comparable<? super E>> implements Queue<E> {

    private E[] heap;
    private int size;
    public GenPriorityQueue(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("La capacité doit être positive ou nulle.");
        }
        this.heap = newArray(Math.max(capacity, 1));
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean insertElement(E e) {
        if (size == heap.length) {
            resize(2 * heap.length);
        }
        heap[size] = e;
        size++;
        siftUp(size - 1);
        return true;
    }

    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException("La file de priorité est vide.");
        }
        return heap[0];
    }

    @Override
    public E popElement() {
        E root = element();
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        siftDown(0);
        return root;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Fin du parcours atteinte.");
                }
                E value = heap[index];
                index++;
                return value;
            }
        };
    }

    private void siftUp(int index) {
        int current = index;
        while (current > 0) {
            int parent = (current - 1) / 2;
            if (heap[current].compareTo(heap[parent]) <= 0) {
                return;
            }
            swap(current, parent);
            current = parent;
        }
    }

    private void siftDown(int index) {
        int current = index;
        while (true) {
            int left = 2 * current + 1;
            int right = left + 1;
            int largest = current;

            if (left < size && heap[left].compareTo(heap[largest]) > 0) {
                largest = left;
            }
            if (right < size && heap[right].compareTo(heap[largest]) > 0) {
                largest = right;
            }
            if (largest == current) {
                return;
            }
            swap(current, largest);
            current = largest;
        }
    }

    private void swap(int i, int j) {
        E tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    private void resize(int newCapacity) {
        E[] newHeap = newArray(newCapacity);
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }


    @SuppressWarnings("unchecked")
    private static <T extends Comparable<? super T>> T[] newArray(int capacity) {
        return (T[]) new Comparable<?>[capacity];
    }
}