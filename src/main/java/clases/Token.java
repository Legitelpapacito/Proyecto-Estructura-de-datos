package clases;

/**
 * Representa un elemento del script.
 * Puede ser una operación (OpCode) o datos (PUSHDATA).
 */
public class Token {

    /** Operación asociada al token */
    private final OpCode opcode;

    /** Datos del token (si aplica) */
    private final byte[] data;

    /**
     * Constructor para un token de tipo operación.
     *
     * @param opcode operación del token
     */
    public Token(OpCode opcode) {
        this.opcode = opcode;
        this.data = null;
    }

    /**
     * Constructor para un token de tipo datos.
     *
     * @param data datos a almacenar en el token
     */
    public Token(byte[] data) {
        this.opcode = OpCode.PUSHDATA;
        this.data = data;
    }

    /**
     * Obtiene el opcode del token.
     *
     * @return operación asociada
     */
    public OpCode getOpcode() {
        return opcode;
    }

    /**
     * Obtiene los datos del token.
     *
     * @return arreglo de bytes con los datos
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Representación en texto del token.
     *
     * @return cadena representando el token
     */
    @Override
    public String toString() {
        if (opcode == OpCode.PUSHDATA) {
            return "PUSHDATA(0x" + HashUtil.aHex(data == null ? new byte[0] : data) + ")";
        }
        return opcode.name();
    }
}