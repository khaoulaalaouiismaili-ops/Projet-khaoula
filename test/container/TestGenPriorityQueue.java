package container;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests unitaires de la file de priorité générique {@link GenPriorityQueue}. */
public class TestGenPriorityQueue {

    @Test
    public void test_emptyCreation() {
        GenPriorityQueue<Integer> queue = new GenPriorityQueue<>(10);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    public void test_integerOrdering() {
        GenPriorityQueue<Integer> queue = new GenPriorityQueue<>(2);
        int[] values = {4, 17, 2, 9, 11, 0};
        for (int value : values) {
            queue.insertElement(value);
        }
        assertEquals(17, queue.popElement());
        assertEquals(11, queue.popElement());
        assertEquals(9, queue.popElement());
        assertEquals(4, queue.popElement());
        assertEquals(2, queue.popElement());
        assertEquals(0, queue.popElement());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void test_stringOrdering() {
        GenPriorityQueue<String> queue = new GenPriorityQueue<>(2);
        queue.insertElement("banane");
        queue.insertElement("cerise");
        queue.insertElement("abricot");
        queue.insertElement("datte");
        assertEquals("datte", queue.popElement());
        assertEquals("cerise", queue.popElement());
        assertEquals("banane", queue.popElement());
        assertEquals("abricot", queue.popElement());
    }

    @Test
    public void test_userDefinedComparableType() {
        GenPriorityQueue<Task> queue = new GenPriorityQueue<>(4);
        queue.insertElement(new Task("écrire", 2));
        queue.insertElement(new Task("compiler", 8));
        queue.insertElement(new Task("relire", 5));
        assertEquals("compiler", queue.popElement().getName());
        assertEquals("relire", queue.popElement().getName());
        assertEquals("écrire", queue.popElement().getName());
    }

    @Test
    public void test_elementDoesNotRemove() {
        GenPriorityQueue<Integer> queue = new GenPriorityQueue<>(10);
        queue.insertElement(3);
        queue.insertElement(8);
        assertEquals(8, queue.element());
        assertEquals(2, queue.size());
    }

    @Test
    public void test_resizeKeepsHeapValid() {
        GenPriorityQueue<Integer> queue = new GenPriorityQueue<>(1);
        for (int i = 0; i < 100; i++) {
            queue.insertElement((i * 13) % 100);
        }
        assertEquals(100, queue.size());
        for (int i = 99; i >= 0; i--) {
            assertEquals(i, queue.popElement());
        }
    }

    @Test
    public void test_popOnEmptyQueueThrows() {
        GenPriorityQueue<String> queue = new GenPriorityQueue<>(5);
        assertThrows(NoSuchElementException.class, () -> queue.popElement());
    }

    @Test
    public void test_iteratorVisitsEveryElementOnce() {
        GenPriorityQueue<Integer> queue = new GenPriorityQueue<>(4);
        int expectedSum = 0;
        for (int i = 1; i <= 20; i++) {
            queue.insertElement(i);
            expectedSum += i;
        }
        int count = 0;
        int sum = 0;
        for (Integer element : queue) {
            count++;
            sum += element;
        }
        assertEquals(20, count);
        assertEquals(expectedSum, sum);
    }

    @Test
    public void test_iteratorOnEmptyQueue() {
        GenPriorityQueue<Integer> queue = new GenPriorityQueue<>(5);
        assertFalse(queue.iterator().hasNext());
    }

    /** Type utilisateur comparable servant de support aux tests. */
    private static final class Task implements Comparable<Task> {

        private final String name;
        private final int priority;

        Task(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

        String getName() {
            return name;
        }

        @Override
        public int compareTo(Task other) {
            return Integer.compare(this.priority, other.priority);
        }
    }
}
