package container;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class IntFIFO implements Queue<Integer> {
    private Integer[] elements;
    private int begin;
    private int size;


    public IntFIFO(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("La capacité doit être positive ou nulle.");
        }
        this.elements = new Integer[capacity];
        this.begin = 0;
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
        if (size == elements.length) {
            resize(2 * elements.length);
        }
        elements[indexOf(size)] = e;
        size++;
        return true;
    }


    @Override
    public Integer element() {
        if (isEmpty()) {
            throw new NoSuchElementException("La file est vide.");
        }
        return elements[begin];
    }


    @Override
    public Integer popElement() {
        Integer first = element();
        elements[begin] = null; // libère la référence pour le ramasse-miettes
        begin = (begin + 1) % elements.length;
        size--;
        return first;
    }


    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            private int visited = 0;

            @Override
            public boolean hasNext() {
                return visited < size;
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Fin du parcours atteinte.");
                }
                Integer value = elements[indexOf(visited)];
                visited++;
                return value;
            }
        };
    }


    private int indexOf(int rank) {
        return (begin + rank) % elements.length;
    }


    private void resize(int newCapacity) {
        Integer[] newElements = new Integer[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[indexOf(i)];
        }
        elements = newElements;
        begin = 0;
    }
}
 