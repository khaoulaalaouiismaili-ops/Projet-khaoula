package container;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests unitaires de la file FIFO d'entiers {@link IntFIFO}. */
public class TestIntFIFO {

    @Test
    public void test_emptyCreation() {
        IntFIFO queue = new IntFIFO(10);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    public void test_negativeCapacityIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new IntFIFO(-1));
    }

    @Test
    public void test_insertElementReturnsTrue() {
        IntFIFO queue = new IntFIFO(1);
        assertTrue(queue.insertElement(1));
        // L'insertion réussit également lorsqu'un redimensionnement est nécessaire.
        assertTrue(queue.insertElement(2));
    }

    @Test
    public void test_insertElementUpdatesSize() {
        IntFIFO queue = new IntFIFO(10);
        queue.insertElement(42);
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        queue.insertElement(7);
        assertEquals(2, queue.size());
    }

    @Test
    public void test_elementDoesNotRemove() {
        IntFIFO queue = new IntFIFO(10);
        queue.insertElement(1);
        queue.insertElement(2);
        assertEquals(1, queue.element());
        assertEquals(1, queue.element());
        assertEquals(2, queue.size());
    }

    @Test
    public void test_popElementRespectsInsertionOrder() {
        IntFIFO queue = new IntFIFO(10);
        for (int i = 0; i < 5; i++) {
            queue.insertElement(i);
        }
        for (int i = 0; i < 5; i++) {
            assertEquals(i, queue.popElement());
        }
        assertTrue(queue.isEmpty());
    }

    @Test
    public void test_resizeKeepsOrder() {
        IntFIFO queue = new IntFIFO(2);
        for (int i = 0; i < 100; i++) {
            queue.insertElement(i);
        }
        assertEquals(100, queue.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, queue.popElement());
        }
    }

    @Test
    public void test_circularBufferWrapsAround() {
        IntFIFO queue = new IntFIFO(3);
        queue.insertElement(1);
        queue.insertElement(2);
        queue.insertElement(3);
        assertEquals(1, queue.popElement());
        assertEquals(2, queue.popElement());
        // Les insertions suivantes réutilisent le début du tableau.
        queue.insertElement(4);
        queue.insertElement(5);
        assertEquals(3, queue.size());
        assertEquals(3, queue.popElement());
        assertEquals(4, queue.popElement());
        assertEquals(5, queue.popElement());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void test_resizeAfterWrapAround() {
        IntFIFO queue = new IntFIFO(3);
        queue.insertElement(1);
        queue.insertElement(2);
        queue.insertElement(3);
        queue.popElement();
        queue.popElement();
        // La file est « à cheval » sur la fin du tableau avant redimensionnement.
        queue.insertElement(4);
        queue.insertElement(5);
        queue.insertElement(6);
        assertEquals(3, queue.popElement());
        assertEquals(4, queue.popElement());
        assertEquals(5, queue.popElement());
        assertEquals(6, queue.popElement());
    }

    @Test
    public void test_alternatingAddAndPop() {
        IntFIFO queue = new IntFIFO(4);
        for (int i = 0; i < 50; i++) {
            queue.insertElement(i);
            assertEquals(i, queue.popElement());
            assertTrue(queue.isEmpty());
        }
    }

    @Test
    public void test_popOnEmptyQueueThrows() {
        IntFIFO queue = new IntFIFO(5);
        assertThrows(NoSuchElementException.class, () -> queue.popElement());
    }

    @Test
    public void test_getOnEmptyQueueThrows() {
        IntFIFO queue = new IntFIFO(5);
        assertThrows(NoSuchElementException.class, () -> queue.element());
    }

    @Test
    public void test_iteratorFollowsInsertionOrder() {
        IntFIFO queue = new IntFIFO(3);
        queue.insertElement(1);
        queue.insertElement(2);
        queue.popElement();
        queue.insertElement(3);
        queue.insertElement(4);

        StringBuilder content = new StringBuilder();
        for (Integer element : queue) {
            content.append(element);
        }
        assertEquals("234", content.toString());
    }

    @Test
    public void test_iteratorOnEmptyQueue() {
        IntFIFO queue = new IntFIFO(5);
        Iterator<Integer> iterator = queue.iterator();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }
}