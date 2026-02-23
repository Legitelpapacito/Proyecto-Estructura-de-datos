package clases.java;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PilaDatos {

    private final Deque<byte[]> pila = new ArrayDeque<>();

    public void push(byte[] data) {
        pila.push(data);
    }

    public byte[] pop() {
        if (pila.isEmpty()) {
            throw new RuntimeException("Stack underflow");
        }
        return pila.pop();
    }

    public byte[] peek() {
        if (pila.isEmpty()) {
            throw new RuntimeException("Stack vacio");
        }
        return pila.peek();
    }

    public boolean isEmpty() {
        return pila.isEmpty();
    }

    public int size() {
        return pila.size();
    }

    public void clear() {
        pila.clear();
    }


    public String snapshot() {
        List<String> partes = new ArrayList<>();
        for (byte[] item : pila) {
            if (item == null) {
                partes.add("null");
            } else {
                partes.add("0x" + HashUtil.aHex(item));
            }
        }
        return partes.toString();
    }
}