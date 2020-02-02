package ru.otus.task;

import java.util.*;

public class DIYArrayList<T> implements List<T> {
    private final int SIZE_GROWTH = 10;
    private int actualSize = 0;
    private Object[] elements;

    public DIYArrayList() {
        elements = new Object[SIZE_GROWTH];
    }

    public DIYArrayList(int maxSize) {
        elements = new Object[maxSize];
        actualSize = maxSize;
    }

    @Override
    public int size() {
        return actualSize;
    }

    @Override
    public boolean isEmpty() {
        return actualSize == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("contains");
    }

    @Override
    public boolean containsAll(Collection<?> elements) {
        throw new UnsupportedOperationException("containsAll");
    }

    @Override
    public Iterator<T> iterator() {
        return new DIYIterator();
    }

    private class DIYIterator implements Iterator<T> {
        Integer lastVisitedIndex;
        int cursor;

        DIYIterator() {
            cursor = 0;
        }

        public boolean hasNext() {
            return cursor < actualSize;
        }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException("No more elements in the DIYArrayList");
            lastVisitedIndex = cursor++;
            return (T) elements[lastVisitedIndex];
        }
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DIYArrayListIterator();
    }

    private class DIYArrayListIterator extends DIYIterator implements ListIterator<T> {

        DIYArrayListIterator() {
        }

        DIYArrayListIterator(int index) {
            checkBounds(index, "get list iterator");
            cursor = index;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        public boolean hasPrevious() {
            return (cursor < actualSize) && (cursor > 0);
        }

        public T previous() {
            if (!hasPrevious()) throw new NoSuchElementException("No previous elements in the DIYArrayList");
            lastVisitedIndex = --cursor;
            return (T) elements[lastVisitedIndex];
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return (cursor - 1);
        }

        public void set(T value) {
            if (lastVisitedIndex == null)
                throw new IllegalStateException("Can't execute set as no next() or previous() were called since iterator initialization, add() or remove() call");
            elements[lastVisitedIndex] = value;
        }

        public void add(T value) {
            throw new UnsupportedOperationException("add");
        }
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new DIYArrayListIterator(index);
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOfRange(elements, 0, actualSize);
    }

    @Override
    public <E> E[] toArray(E[] a) {
        throw new UnsupportedOperationException("toArray generic");
    }

    @Override
    public boolean add(T element) {
        if (actualSize == elements.length) {
            int newSize = elements.length + SIZE_GROWTH;
            elements = Arrays.copyOf(elements, newSize);
        }
        elements[actualSize] = element;
        actualSize++;
        return true;
    }

    @Override
    public void add(int index, T value) {
        throw new UnsupportedOperationException("add at index");
    }

    @Override
    public boolean addAll(Collection<? extends T> elements) {
        throw new UnsupportedOperationException("addAll");
    }

    @Override
    public boolean addAll(int insertAtIndex, Collection<? extends T> elements) {
        throw new UnsupportedOperationException("addAll at index");
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException("remove at index");
    }

    @Override
    public boolean remove(Object element) {
        throw new UnsupportedOperationException("remove");
    }

    @Override
    public boolean removeAll(Collection<?> elements) {
        throw new UnsupportedOperationException("removeAll");
    }

    @Override
    public boolean retainAll(Collection<?> elements) {
        throw new UnsupportedOperationException("retainAll");
    }

    @Override
    public void clear() {
        actualSize = 0;
    }

    @Override
    public T get(int index) {
        checkBounds(index, "get element");
        return (T) elements[index];
    }

    @Override
    public T set(int index, T value) {
        checkBounds(index, "set element");
        T previousValue = (T) elements[index];
        elements[index] = value;
        return previousValue;
    }

    @Override
    public int indexOf(Object element) {
        throw new UnsupportedOperationException("indexOf");
    }

    @Override
    public int lastIndexOf(Object element) {
        throw new UnsupportedOperationException("lastIndexOf");
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("subList");
    }

    public String toString() {
        return String.format("DIYArrayList:\n%s\n", Arrays.toString(Arrays.copyOfRange(elements, 0, actualSize)));
    }

    private void checkBounds(int index, String operation) {
        if ((index < 0) || (index >= actualSize))
            throw new ArrayIndexOutOfBoundsException(String.format("Trying to %s at index %d from DIYArrayList of size %d", operation, index, actualSize));
    }
}
