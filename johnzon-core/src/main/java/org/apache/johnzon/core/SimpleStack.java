package org.apache.johnzon.core;

class SimpleStack<T> {

    private Element<T> head;

    void push(final T element) {

        final Element<T> tmp = new Element<T>();
        tmp.payload = element;
        tmp.previous = head;
        head = tmp;

    }

    T pop() {

        final T tmp = head.payload;
        head = head.previous;
        return tmp;

    }

    T peek() {
        return head.payload;
    }

    boolean isEmpty() {
        return head == null;
    }

    private static class Element<T> {

        public Element() {

        }

        Element<T> previous;
        T payload;

    }

}
