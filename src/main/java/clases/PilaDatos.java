package clases;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Clase que representa una pila de datos.
 * Se usa durante la ejecución del script.
 */
public class PilaDatos {

    /** Estructura interna de la pila */
    private final Deque<byte[]> pila = new ArrayDeque<>();

    /**
     * Agrega un elemento a la pila.
     *
     * @param data dato a insertar
     */
    public void push(byte[] data) {
        pila.push(data);
    }

    /**
     * Saca el elemento superior de la pila.
     *
     * @return elemento removido
     */
    public byte[] pop() {
        if (pila.isEmpty()) {
            throw new RuntimeException("Stack underflow");
        }
        return pila.pop();
    }

    /**
     * Consulta el elemento superior sin eliminarlo.
     *
     * @return elemento en la cima
     */
    public byte[] peek() {
        if (pila.isEmpty()) {
            throw new RuntimeException("Stack vacio");
        }
        return pila.peek();
    }

    /**
     * Indica si la pila está vacía.
     *
     * @return true si no hay elementos
     */
    public boolean isEmpty() {
        return pila.isEmpty();
    }

    /**
     * Devuelve el tamaño de la pila.
     *
     * @return número de elementos
     */
    public int size() {
        return pila.size();
    }

    /**
     * Limpia completamente la pila.
     */
    public void clear() {
        pila.clear();
    }

    /**
     * Devuelve una representación en texto de la pila.
     *
     * @return contenido de la pila en formato hexadecimal
     */
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