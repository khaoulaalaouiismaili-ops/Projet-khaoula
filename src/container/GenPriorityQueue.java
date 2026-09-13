package container;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * File de priorité générique implémentée au moyen d'un tas (arbre binaire
 * complet stocké dans un tableau).
 *
 * <p>La priorité des éléments est donnée par leur ordre naturel : le type
 * stocké doit donc implémenter l'interface {@link Comparable}.
 * {@link #popElement()} retourne toujours le plus grand élément présent.</p>
 *
 * <p>Invariant de classe : pour tout indice {@code i} de {@code [0, size[},
 * {@code heap[i].compareTo(heap[2*i+1]) >= 0} et
 * {@code heap[i].compareTo(heap[2*i+2]) >= 0} lorsque ces fils existent.</p>
 *
 * @param <E> type des éléments stockés, comparable à lui-même
 */
public class GenPriorityQueue<E extends Comparable<? super E>> implements Queue<E> {

    /** Tableau représentant le tas : le fils gauche de i est en 2*i+1. */
    private E[] heap;

    /** Nombre d'éléments effectivement présents dans le tas. */
    private int size;

    /**
     * Construit une file de priorité vide.
     *
     * @param capacity nombre d'éléments que la file peut initialement contenir
     * @throws IllegalArgumentException si la capacité est strictement négative
     */
    public GenPriorityQueue(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("La capacité doit être positive ou nulle.");
        }
        // Une capacité minimale de 1 évite un tableau de taille nulle.
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

    /**
     * Insère un élément dans le tas : il est placé en dernière feuille puis
     * remonté tant qu'il est plus prioritaire que son père. La capacité est
     * doublée si le tas est plein : l'insertion réussit donc toujours.
     *
     * @param e l'élément à insérer
     * @return toujours {@code true}
     */
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

    /**
     * Consulte la racine du tas, c'est-à-dire l'élément le plus prioritaire.
     *
     * @return le plus grand élément de la file
     * @throws NoSuchElementException si la file est vide
     */
    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException("La file de priorité est vide.");
        }
        return heap[0];
    }

    /**
     * Retire la racine du tas : la dernière feuille la remplace puis est
     * redescendue tant qu'un de ses fils est plus prioritaire qu'elle.
     *
     * @return le plus grand élément de la file
     * @throws NoSuchElementException si la file est vide
     */
    @Override
    public E popElement() {
        E root = element();
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        siftDown(0);
        return root;
    }

    /**
     * Retourne un itérateur sur les éléments de la file. Conformément à la
     * spécification des itérateurs, aucun ordre de parcours n'est garanti :
     * le parcours suit ici l'ordre de stockage dans le tableau.
     *
     * @return un itérateur sur les éléments de la file
     */
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

    /**
     * Remonte l'élément d'indice donné jusqu'à rétablir l'invariant du tas.
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
     * Redescend l'élément d'indice donné jusqu'à rétablir l'invariant du tas.
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
        E tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    /**
     * Redimensionne le tableau représentant le tas.
     *
     * @param newCapacity nouvelle capacité, supérieure au nombre d'éléments
     */
    private void resize(int newCapacity) {
        E[] newHeap = newArray(newCapacity);
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    /**
     * Crée un tableau générique. Java ne permettant pas d'instancier
     * directement un tableau de type paramétré (effacement de type), on passe
     * par un tableau du type effacé de {@code E}, converti ensuite.
     *
     * <p>Le paramètre {@code E} étant borné par {@link Comparable}, son type
     * effacé est {@code Comparable} et non {@code Object} : il faut donc créer
     * un tableau de {@code Comparable}. Créer ici un tableau d'{@link Object}
     * compilerait, mais provoquerait une {@link ClassCastException} à
     * l'exécution lors de l'affectation au champ {@code heap}.</p>
     *
     * <p>La conversion est sûre car le tableau reste encapsulé dans la classe
     * et n'est jamais exposé à l'extérieur.</p>
     *
     * @param capacity taille du tableau à créer
     * @param <T>      type des éléments du tableau
     * @return un tableau de la taille demandée
     */
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<? super T>> T[] newArray(int capacity) {
        return (T[]) new Comparable<?>[capacity];
    }
}