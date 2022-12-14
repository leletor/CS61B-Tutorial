package synthesizer;

import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.capacity = capacity;
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        last = last % rb.length;
        rb[last] = x;
        fillCount += 1;
        last += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        first = first % capacity;
        T t = rb[first];
        rb[first] = null;
        first = first + 1;
        fillCount = fillCount - 1;
        return t;
    }


    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer underflow");
        }
        first = first % rb.length;
        return rb[first];
    }

    @Override
    public Iterator<T> iterator() {
        return new BufferIterator();
    }

    private class BufferIterator implements Iterator<T> {
        private int ptr;
        private int num;

        public BufferIterator() {
            ptr = first;
            num = 0;
        }

        @Override
        public boolean hasNext() {
            return num < fillCount;
        }

        @Override
        public T next() {
                T t = rb[ptr];
                if (ptr == capacity - 1) {
                    ptr = 0;
                    num += 1;
                } else {
                    ptr += 1;
                    num += 1;
                }
                return t;
        }

    }
}
