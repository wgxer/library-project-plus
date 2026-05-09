package lpp;

import java.io.Serializable;

public class LinkedList<T> implements Serializable {
    private Node<T> head;

    public LinkedList() {
        head = null;
    }

    public boolean isEmpty() {
        return head == null;
    }
    
    
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
    }

    public int size() {
        int count = 0;
        Node<T> current = head;
        while (current != null) {
            count++;
            current = current.getNext();
        }
        return count;
    }

    
    public T get(int index) {
        if (index < 0 || isEmpty()) return null;
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            if (current == null) return null;
            current = current.getNext();
        }
        return (current != null) ? current.getData() : null;
    }

    public boolean remove(int index) {
        if (isEmpty() || index < 0) return false;
        if (index == 0) {
            head = head.getNext();
            return true;
        }
        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            if (current == null || current.getNext() == null) return false;
            current = current.getNext();
        }
        if (current.getNext() == null) return false;
        current.setNext(current.getNext().getNext());
        return true;
    }
    
    public void set(int index, T data) {
        if (index < 0 || isEmpty()) return;
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            if (current.getNext() == null) return;
            current = current.getNext();
        }
        current.setData(data);
    }
}