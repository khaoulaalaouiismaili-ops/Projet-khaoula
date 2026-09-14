package container;

import java.util.Iterator;
import java.util.NoSuchElementException;
public class IntPriorityQueue implements Queue<Integer> {


    private Integer[] heap;
    private int size;


    public IntPriorityQueue(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("La capacité doit être positive ou nulle.");
        }
        this.heap = new Integer[Math.max(capacity, 1)];
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
    public boolean insertElement(Integer e) {
        if (size == heap.length) {
            resize(2 * heap.length);
        }
        heap[size] = e;
        size++;
        siftUp(size - 1);
        return true;
    }


    @Override
    public Integer element() {
        if (isEmpty()) {
            throw new NoSuchElementException("La file de priorité est vide.");
        }
        return heap[0];
    }

    @Override
    public Integer popElement() {
        Integer root = element();
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        siftDown(0);
        return root;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Fin du parcours atteinte.");
                }
                Integer value = heap[index];
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
        Integer tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }


    private void resize(int newCapacity) {
        Integer[] newHeap = new Integer[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }
}