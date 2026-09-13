package container;

import java.util.Iterator;
import java.util.NoSuchElementException;
public class IntPriorityQueue implements Queue<Integer> {

    /** Tableau représentant le tas : le fils gauche de i est en 2*i+1. */
    private Integer[] heap;

    /** Nombre d'éléments effectivement présents dans le tas. */
    private int size;

    /**
     * Construit une file de priorité vide.
     *
     * @param capacity nombre d'éléments que la file peut initialement contenir
     * @throws IllegalArgumentException si la capacité est strictement négative
     */
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

    /**
     * Insère un élément dans le tas : il est placé en dernière feuille puis
     * remonté tant qu'il est plus prioritaire que son père. La capacité est
     * doublée si le tas est plein : l'insertion réussit donc toujours.
     *
     * @param e l'élément à insérer
     * @return toujours {@code true}
     */
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

    /**
     * Consulte la racine du tas, c'est-à-dire l'élément le plus prioritaire.
     *
     * @return l'élément de plus grande valeur
     * @throws NoSuchElementException si la file est vide
     */
    @Override
    public Integer element() {
        if (isEmpty()) {
            throw new NoSuchElementException("La file de priorité est vide.");
        }
        return heap[0];
    }

    /**
     * Retire la racine du tas : la dernière feuille la remplace puis est
     * redescendue tant qu'un de ses fils est plus prioritaire qu'elle.
     *
     * @return l'élément de plus grande valeur
     * @throws NoSuchElementException si la file est vide
     */
    @Override
    public Integer popElement() {
        Integer root = element();
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        siftDown(0);
        return root;
    }

    /**
     * Retourne un itérateur sur les éléments du tas. Conformément à la
     * spécification des itérateurs, aucun ordre de parcours n'est garanti :
     * le parcours suit ici l'ordre de stockage dans le tableau.
     *
     * @return un itérateur sur les éléments de la file
     */
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

    /**
     * Remonte l'élément d'indice donné jusqu'à ce que l'invariant du tas soit
     * rétabli.
     *
     * @param index indice de l'élément à remonter
     */
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

    /**
     * Redescend l'élément d'indice donné jusqu'à ce que l'invariant du tas soit
     * rétabli.
     *
     * @param index indice de l'élément à redescendre
     */
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

    /**
     * Échange le contenu de deux cases du tas.
     *
     * @param i indice de la première case
     * @param j indice de la seconde case
     */
    private void swap(int i, int j) {
        Integer tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    /**
     * Redimensionne le tableau représentant le tas.
     *
     * @param newCapacity nouvelle capacité, supérieure au nombre d'éléments
     */
    private void resize(int newCapacity) {
        Integer[] newHeap = new Integer[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }
}