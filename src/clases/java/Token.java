public class Token {

    private OpCode opcode;
    private byte[] data;

    public Token(OpCode opcode) {
        this.opcode = opcode;
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
}