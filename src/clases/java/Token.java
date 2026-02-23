package clases.java;

public class Token {

    private final OpCode opcode;
    private final byte[] data;

    public Token(OpCode opcode) {
        this.opcode = opcode;
        this.data = null;
    }

    public Token(byte[] data) {
        this.opcode = OpCode.PUSHDATA;
        this.data = data;
    }

    public OpCode getOpcode() {
        return opcode;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        if (opcode == OpCode.PUSHDATA) {
            return "PUSHDATA(0x" + HashUtil.aHex(data == null ? new byte[0] : data) + ")";
        }
        return opcode.name();
    }
}