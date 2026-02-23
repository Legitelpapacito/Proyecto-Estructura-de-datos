package clases.java;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

public class MaquinaScript {

    private PilaDatos pila = new PilaDatos();
    private VerificadorFirma verificador = new VerificadorFirma();

    public void ejecutar(List<Token> tokens, boolean trace) throws Exception {

        for (Token token : tokens) {

            if (token.getOpcode() == OpCode.PUSHDATA) {
                pila.push(token.getData());
            } else {

                switch (token.getOpcode()) {

                    case OP_DUP:
                        pila.push(pila.peek());
                        break;

                    case OP_DROP:
                        pila.pop();
                        break;

                    case OP_HASH160:
                        byte[] data = pila.pop();
                        MessageDigest sha = MessageDigest.getInstance("SHA-256");
                        byte[] hash = sha.digest(data);
                        pila.push(hash);
                        break;

                    case OP_EQUAL:
                        byte[] a = pila.pop();
                        byte[] b = pila.pop();
                        pila.push(Arrays.equals(a, b) ? new byte[]{1} : new byte[]{0});
                        break;

                    case OP_EQUALVERIFY:
                        byte[] x = pila.pop();
                        byte[] y = pila.pop();
                        if (!Arrays.equals(x, y))
                            throw new RuntimeException("OP_EQUALVERIFY falló");
                        break;

                    case OP_CHECKSIG:
                        byte[] pubKey = pila.pop();
                        byte[] firma = pila.pop();
                        boolean ok = verificador.verificar(firma, pubKey);
                        pila.push(ok ? new byte[]{1} : new byte[]{0});
                        break;

                    default:
                        throw new RuntimeException("Opcode no soportado");
                }
            }

            if (trace) {
                System.out.println("Stack: " + pila.snapshot());
            }
        }
    }

    public boolean resultadoFinal() {
        return !pila.isEmpty() && pila.peek()[0] == 1;
    }
}