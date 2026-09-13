package container;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * File de type FIFO (first in / first out) pour des entiers, implémentée au
 * moyen d'un buffer circulaire.
 *
 * <p>Les éléments sont stockés dans un tableau dont la fin est considérée comme
 * connectée au début : l'indice suivant {@code tab.length - 1} est l'indice
 * {@code 0}. Ajouter ou retirer un élément ne provoque donc aucun déplacement
 * des éléments déjà présents ; ces deux opérations s'effectuent en temps
 * constant (hors redimensionnement).</p>
 *
 * <p>Invariant de classe : les {@code size} éléments de la file occupent les
 * cases {@code (begin + i) % elements.length} pour {@code i} dans
 * {@code [0, size[}, et {@code 0 <= size <= elements.length}.</p>
 */
public class IntFIFO implements Queue<Integer> {

    /** Tableau circulaire contenant les éléments de la file. */
    private Integer[] elements;

    /** Indice du premier élément de la file (le prochain à sortir). */
    private int begin;

    /** Nombre d'éléments effectivement présents dans la file. */
    private int size;

    /**
     * Construit une file vide.
     *
     * @param capacity nombre d'éléments que la file peut initialement contenir
     * @throws IllegalArgumentException si la capacité est strictement négative
     */
    public IntFIFO(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("La capacité doit être positive ou nulle.");
        }
        // Une capacité minimale de 1 évite un tableau de taille nulle (modulo 0).
        this.elements = new Integer[Math.max(capacity, 1)];
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

    /**
     * Insère un élément en fin de file. La capacité est doublée si la file est
     * pleine : l'insertion réussit donc toujours.
     *
     * @param e l'élément à insérer
     * @return toujours {@code true}
     */
    @Override
    public boolean insertElement(Integer e) {
        if (size == elements.length) {
            resize(2 * elements.length);
        }
        elements[indexOf(size)] = e;
        size++;
        return true;
    }

    /**
     * Consulte le plus ancien élément inséré sans le retirer.
     *
     * @return le premier élément de la file
     * @throws NoSuchElementException si la file est vide
     */
    @Override
    public Integer element() {
        if (isEmpty()) {
            throw new NoSuchElementException("La file est vide.");
        }
        return elements[begin];
    }

    /**
     * Retire et retourne le plus ancien élément inséré.
     *
     * @return le premier élément de la file
     * @throws NoSuchElementException si la file est vide
     */
    @Override
    public Integer popElement() {
        Integer first = element();
        elements[begin] = null; // libère la référence pour le ramasse-miettes
        begin = (begin + 1) % elements.length;
        size--;
        return first;
    }

    /**
     * Retourne un itérateur parcourant les éléments du plus ancien au plus
     * récent.
     *
     * @return un itérateur sur les éléments de la file
     */
    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            /** Nombre d'éléments déjà parcourus. */
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

    /**
     * Traduit un rang logique dans la file en indice réel du tableau.
     *
     * @param rank rang de l'élément dans la file (0 pour le plus ancien)
     * @return l'indice correspondant dans le tableau circulaire
     */
    private int indexOf(int rank) {
        return (begin + rank) % elements.length;
    }

    /**
     * Redimensionne le buffer circulaire en replaçant le premier élément à
     * l'indice 0.
     *
     * @param newCapacity nouvelle capacité, supérieure au nombre d'éléments
     */
    private void resize(int newCapacity) {
        Integer[] newElements = new Integer[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[indexOf(i)];
        }
        elements = newElements;
        begin = 0;
    }
}
 