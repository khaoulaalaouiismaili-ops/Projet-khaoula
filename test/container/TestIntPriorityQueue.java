package container;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestIntPriorityQueue {
    private static final int[] VALUES = {5, 1, 9, 3, 9, 0, 7, 2, 8, 6, 4};

    @Test
    public void test_emptyCreation() {
        IntPriorityQueue queue = new IntPriorityQueue(10);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    public void test_negativeCapacityIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new IntPriorityQueue(-5));
    }

    @Test
    public void test_insertElementReturnsTrue() {
        IntPriorityQueue queue = new IntPriorityQueue(1);
        assertTrue(queue.insertElement(1));
        // L'insertion réussit également lorsqu'un redimensionnement est nécessaire.
        assertTrue(queue.insertElement(2));
    }

    @Test
    public void test_insertElementUpdatesSize() {
        IntPriorityQueue queue = new IntPriorityQueue(10);
        queue.insertElement(3);
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
    }

    @Test
    public void test_elementReturnsHighestPriority() {
        IntPriorityQueue queue = new IntPriorityQueue(10);
        queue.insertElement(3);
        assertEquals(3, queue.element());
        queue.insertElement(12);
        assertEquals(12, queue.element());
        queue.insertElement(7);
        assertEquals(12, queue.element());
        // element ne retire aucun élément.
        assertEquals(3, queue.size());
    }

    @Test
    public void test_popElementFollowsDecreasingOrder() {
        IntPriorityQueue queue = new IntPriorityQueue(VALUES.length);
        for (int value : VALUES) {
            queue.insertElement(value);
        }
        int[] expected = VALUES.clone();
        Arrays.sort(expected);
        for (int i = expected.length - 1; i >= 0; i--) {
            assertEquals(expected[i], queue.popElement());
        }
        assertTrue(queue.isEmpty());
    }

    @Test
    public void test_resizeKeepsHeapValid() {
        IntPriorityQueue queue = new IntPriorityQueue(1);
        for (int i = 0; i < 200; i++) {
            queue.insertElement((i * 37) % 200);
        }
        assertEquals(200, queue.size());
        int previous = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            int current = queue.popElement();
            assertTrue(current <= previous);
            previous = current;
        }
    }

    @Test
    public void test_duplicatedValuesAreAllKept() {
        IntPriorityQueue queue = new IntPriorityQueue(10);
        queue.insertElement(4);
        queue.insertElement(4);
        queue.insertElement(4);
        assertEquals(3, queue.size());
        assertEquals(4, queue.popElement());
        assertEquals(4, queue.popElement());
        assertEquals(4, queue.popElement());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void test_sortedInsertionsAreHandled() {
        IntPriorityQueue increasing = new IntPriorityQueue(10);
        IntPriorityQueue decreasing = new IntPriorityQueue(10);
        for (int i = 0; i < 10; i++) {
            increasing.insertElement(i);
            decreasing.insertElement(9 - i);
        }
        for (int i = 9; i >= 0; i--) {
            assertEquals(i, increasing.popElement());
            assertEquals(i, decreasing.popElement());
        }
    }

    @Test
    public void test_addAfterPopKeepsHeapValid() {
        IntPriorityQueue queue = new IntPriorityQueue(4);
        queue.insertElement(10);
        queue.insertElement(5);
        assertEquals(10, queue.popElement());
        queue.insertElement(20);
        queue.insertElement(1);
        assertEquals(20, queue.popElement());
        assertEquals(5, queue.popElement());
        assertEquals(1, queue.popElement());
    }

    @Test
    public void test_popOnEmptyQueueThrows() {
        IntPriorityQueue queue = new IntPriorityQueue(5);
        assertThrows(NoSuchElementException.class, () -> queue.popElement());
    }

    @Test
    public void test_getOnEmptyQueueThrows() {
        IntPriorityQueue queue = new IntPriorityQueue(5);
        assertThrows(NoSuchElementException.class, () -> queue.element());
    }

    @Test
    public void test_iteratorVisitsEveryElementOnce() {
        IntPriorityQueue queue = new IntPriorityQueue(4);
        for (int value : VALUES) {
            queue.insertElement(value);
        }
        int[] visited = new int[VALUES.length];
        int count = 0;
        for (Integer element : queue) {
            visited[count] = element;
            count++;
        }
        assertEquals(VALUES.length, count);

        // L'ordre de parcours n'est pas spécifié : on compare les multi-ensembles.
        int[] expected = VALUES.clone();
        Arrays.sort(expected);
        Arrays.sort(visited);
        assertArrayEqualsAsSortedCopy(expected, visited);
    }

    @Test
    public void test_iteratorOnEmptyQueue() {
        IntPriorityQueue queue = new IntPriorityQueue(5);
        Iterator<Integer> iterator = queue.iterator();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    /**
     * Compare deux tableaux déjà triés.
     *
     * @param expected tableau attendu
     * @param actual   tableau obtenu
     */
    private void assertArrayEqualsAsSortedCopy(int[] expected, int[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}
