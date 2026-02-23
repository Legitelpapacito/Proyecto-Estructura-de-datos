package clases.java;

import java.util.Arrays;
import java.util.List;

public class MaquinaScript {

    private final PilaDatos pila = new PilaDatos();
    private final VerificadorFirma verificador = new VerificadorFirma();

    public void reset() {
        pila.clear();
    }

    public void ejecutar(List<Token> tokens, boolean trace) {

        for (Token token : tokens) {

            if (token.getOpcode() == OpCode.PUSHDATA) {
                pila.push(token.getData());
                if (trace) imprimirTrace(token);
                continue;
            }

            switch (token.getOpcode()) {

                case OP_0:  pila.push(new byte[]{0}); break;
                case OP_1:  pila.push(new byte[]{1}); break;
                case OP_2:  pila.push(new byte[]{2}); break;
                case OP_3:  pila.push(new byte[]{3}); break;
                case OP_4:  pila.push(new byte[]{4}); break;
                case OP_5:  pila.push(new byte[]{5}); break;
                case OP_6:  pila.push(new byte[]{6}); break;
                case OP_7:  pila.push(new byte[]{7}); break;
                case OP_8:  pila.push(new byte[]{8}); break;
                case OP_9:  pila.push(new byte[]{9}); break;
                case OP_10: pila.push(new byte[]{10}); break;
                case OP_11: pila.push(new byte[]{11}); break;
                case OP_12: pila.push(new byte[]{12}); break;
                case OP_13: pila.push(new byte[]{13}); break;
                case OP_14: pila.push(new byte[]{14}); break;
                case OP_15: pila.push(new byte[]{15}); break;
                case OP_16: pila.push(new byte[]{16}); break;

                case OP_DUP: {
                    byte[] top = pila.peek();
                    pila.push(Arrays.copyOf(top, top.length));
                    break;
                }

                case OP_DROP:
                    pila.pop();
                    break;

                case OP_HASH160: {
                    byte[] data = pila.pop();
                    byte[] hash160 = HashUtil.hash160Mock(data);
                    pila.push(hash160);
                    break;
                }

                case OP_EQUAL: {
                    byte[] a = pila.pop();
                    byte[] b = pila.pop();
                    pila.push(Arrays.equals(a, b) ? new byte[]{1} : new byte[]{0});
                    break;
                }

                case OP_EQUALVERIFY: {
                    byte[] x = pila.pop();
                    byte[] y = pila.pop();
                    if (!Arrays.equals(x, y)) {
                        throw new RuntimeException("OP_EQUALVERIFY fallo (no coincide el hash)");
                    }
                    break;
                }

                case OP_CHECKSIG: {
                    byte[] pubKey = pila.pop();
                    byte[] firma = pila.pop();
                    boolean ok = verificador.verificar(firma, pubKey);
                    pila.push(ok ? new byte[]{1} : new byte[]{0});
                    break;
                }

                default:
                    throw new RuntimeException("Opcode no soportado: " + token.getOpcode());
            }

            if (trace) imprimirTrace(token);
        }
    }

    private void imprimirTrace(Token token) {
        System.out.println("Ejecutado: " + token + " | Stack: " + pila.snapshot());
    }

    public boolean resultadoFinal() {
        return !pila.isEmpty() && HashUtil.esVerdadero(pila.peek());
    }
}