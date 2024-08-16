package application.types;

import java.util.EmptyStackException;

public class CustomStack<T> {
    private Node<T> top;
    private int size;

    private static class Node<T> {
        private T data;
        private Node<T> next;
        
        public Node(T data) {
            this.data = data;
        }
    }

    public CustomStack() {
        top = null;
        size = 0;
    }

    public void push(T item) {
        Node<T> node = new Node<>(item);
        node.next = top;
        top = node;
        size++;
    }

    public T pop() {
        if (top == null) {
            throw new EmptyStackException();
        }
        T item = top.data;
        top = top.next;
        size--;
        return item;
    }

    public T peek() {
        if (top == null) {
            throw new EmptyStackException();
        }
        return top.data;
    }

    public boolean isEmpty() {
        if (size == 0 || size == 1) {
        	return true;
        } else {
        	return false;
        }
    }

    public int size() {
        return size;
    }

    public void clear() {
        top = null;
        size = 0;
    }
    
    public void display() {
        Node<T> current = top;
        System.out.println("Stack contents (top to bottom):");
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
        System.out.println();
    }
}
