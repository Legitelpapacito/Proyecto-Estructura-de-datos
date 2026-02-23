package clases.java;

import java.util.ArrayDeque;
import java.util.Deque;

public class PilaDatos {

    private Deque<byte[]> pila = new ArrayDeque<>();

    public void push(byte[] data) {
        pila.push(data);
    }

    public byte[] pop() {
        if (pila.isEmpty())
            throw new RuntimeException("Stack underflow");
        return pila.pop();
    }

    public byte[] peek() {
        if (pila.isEmpty())
            throw new RuntimeException("Stack vacío");
        return pila.peek();
    }

    public boolean isEmpty() {
        return pila.isEmpty();
    }

    public String snapshot() {
        return pila.toString();
    }
}