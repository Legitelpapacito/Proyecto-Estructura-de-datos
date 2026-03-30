package clases;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class MaquinaScript {

    private final PilaDatos pila = new PilaDatos();
    private final VerificadorFirma verificador = new VerificadorFirma();

    /**
     * Stack para control de flujo (IF/ELSE/ENDIF).
     * Cada frame indica si su bloque debe ejecutarse, considerando el padre.
     */
    private final Deque<IfFrame> ifStack = new ArrayDeque<>();

    private static final class IfFrame {
        final boolean parentEjecuta;
        final boolean condicion;
        boolean ejecutaAhora;

        IfFrame(boolean parentEjecuta, boolean condicion, boolean ejecutaAhora) {
            this.parentEjecuta = parentEjecuta;
            this.condicion = condicion;
            this.ejecutaAhora = ejecutaAhora;
        }
    }

    public void reset() {
        pila.clear();
        ifStack.clear();
    }

    private boolean seEjecuta() {
        // Si hay algún frame con ejecutaAhora=false, se omite el cuerpo
        for (IfFrame f : ifStack) {
            if (!f.ejecutaAhora) return false;
        }
        return true;
    }

    public void ejecutar(List<Token> tokens, boolean trace) {

        for (int i = 0; i < tokens.size(); i++) {

            Token token = tokens.get(i);
            OpCode op = token.getOpcode();

            // ---- Compatibilidad: PUSHDATAx toma el siguiente token como data ----
            if (op == OpCode.PUSHDATA || op == OpCode.PUSHDATA1 || op == OpCode.PUSHDATA2) {
                if (i + 1 >= tokens.size()) {
                    throw new RuntimeException(op + " requiere un dato después");
                }
                Token dataTok = tokens.get(i + 1);
                if (dataTok.getOpcode() != OpCode.PUSHDATA) {
                    throw new RuntimeException(op + " requiere un token de datos después");
                }

                boolean ejecutar = seEjecuta();
                if (ejecutar) {
                    pila.push(dataTok.getData());
                }

                if (trace) {
                    System.out.println((ejecutar ? "Ejecutado: " : "Saltado: ") + op +
                            " (data=0x" + HashUtil.aHex(dataTok.getData()) + ") | Stack: " + pila.snapshot());
                }

                i++; // saltar el dato consumido por PUSHDATAx
                continue;
            }

            // ---- Token de datos (PUSHDATA interno) ----
            if (op == OpCode.PUSHDATA) {
                boolean ejecutar = seEjecuta();
                if (ejecutar) {
                    pila.push(token.getData());
                }
                if (trace) imprimirTrace(token, ejecutar);
                continue;
            }

            // ---- Control de flujo SIEMPRE se procesa aunque se esté "saltando" ----
            if (op == OpCode.OP_IF || op == OpCode.OP_NOTIF || op == OpCode.OP_ELSE || op == OpCode.OP_ENDIF) {
                procesarFlujo(op);
                if (trace) {
                    System.out.println("Ejecutado: " + op + " | Stack: " + pila.snapshot());
                }
                continue;
            }

            // Si estamos dentro de una rama no ejecutable, no ejecutar opcodes normales
            boolean ejecutar = seEjecuta();
            if (!ejecutar) {
                if (trace) imprimirTrace(token, false);
                continue;
            }

            // ---- Ejecución normal ----
            switch (op) {

                // Constantes
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

                // Pila
                case OP_DUP: {
                    byte[] top = pila.peek();
                    pila.push(Arrays.copyOf(top, top.length));
                    break;
                }
                case OP_DROP:
                    pila.pop();
                    break;

                case OP_SWAP: {
                    byte[] a = pila.pop();
                    byte[] b = pila.pop();
                    pila.push(a);
                    pila.push(b);
                    break;
                }

                case OP_OVER: {
                    byte[] a = pila.pop();    // top
                    byte[] b = pila.pop();    // second
                    pila.push(b);
                    pila.push(a);
                    pila.push(Arrays.copyOf(b, b.length)); // copia del segundo
                    break;
                }

                // Lógica / comparación
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
                        throw new RuntimeException("OP_EQUALVERIFY falló (no coincide)");
                    }
                    break;
                }

                case OP_NOT: {
                    boolean v = HashUtil.esVerdadero(pila.pop());
                    pila.push(v ? new byte[]{0} : new byte[]{1});
                    break;
                }

                case OP_BOOLAND: {
                    boolean a = HashUtil.esVerdadero(pila.pop());
                    boolean b = HashUtil.esVerdadero(pila.pop());
                    pila.push((a && b) ? new byte[]{1} : new byte[]{0});
                    break;
                }

                case OP_BOOLOR: {
                    boolean a = HashUtil.esVerdadero(pila.pop());
                    boolean b = HashUtil.esVerdadero(pila.pop());
                    pila.push((a || b) ? new byte[]{1} : new byte[]{0});
                    break;
                }

                // Aritmética / comparación numérica (b op a)
                case OP_ADD: {
                    int a = HashUtil.aInt(pila.pop());
                    int b = HashUtil.aInt(pila.pop());
                    pila.push(HashUtil.deInt(b + a));
                    break;
                }

                case OP_SUB: {
                    int a = HashUtil.aInt(pila.pop());
                    int b = HashUtil.aInt(pila.pop());
                    pila.push(HashUtil.deInt(b - a));
                    break;
                }

                case OP_NUMEQUALVERIFY: {
                    int a = HashUtil.aInt(pila.pop());
                    int b = HashUtil.aInt(pila.pop());
                    if (a != b) throw new RuntimeException("OP_NUMEQUALVERIFY falló");
                    break;
                }

                case OP_LESSTHAN: {
                    int a = HashUtil.aInt(pila.pop());
                    int b = HashUtil.aInt(pila.pop());
                    pila.push((b < a) ? new byte[]{1} : new byte[]{0});
                    break;
                }

                case OP_GREATERTHAN: {
                    int a = HashUtil.aInt(pila.pop());
                    int b = HashUtil.aInt(pila.pop());
                    pila.push((b > a) ? new byte[]{1} : new byte[]{0});
                    break;
                }

                case OP_LESSTHANOREQUAL: {
                    int a = HashUtil.aInt(pila.pop());
                    int b = HashUtil.aInt(pila.pop());
                    pila.push((b <= a) ? new byte[]{1} : new byte[]{0});
                    break;
                }

                case OP_GREATERTHANOREQUAL: {
                    int a = HashUtil.aInt(pila.pop());
                    int b = HashUtil.aInt(pila.pop());
                    pila.push((b >= a) ? new byte[]{1} : new byte[]{0});
                    break;
                }

                // Control extra
                case OP_VERIFY: {
                    boolean ok = HashUtil.esVerdadero(pila.pop());
                    if (!ok) throw new RuntimeException("OP_VERIFY falló");
                    break;
                }

                case OP_RETURN:
                    throw new RuntimeException("OP_RETURN ejecutado (script falla)");

                // Hashes
                case OP_SHA256: {
                    byte[] data = pila.pop();
                    pila.push(HashUtil.sha256(data));
                    break;
                }

                case OP_HASH160: {
                    byte[] data = pila.pop();
                    pila.push(HashUtil.hash160Mock(data));
                    break;
                }

                case OP_HASH256: {
                    byte[] data = pila.pop();
                    pila.push(HashUtil.hash256(data));
                    break;
                }

                // Firmas (mock)
                case OP_CHECKSIG: {
                    byte[] pubKey = pila.pop();
                    byte[] firma = pila.pop();
                    boolean ok = verificador.verificar(firma, pubKey);
                    pila.push(ok ? new byte[]{1} : new byte[]{0});
                    break;
                }

                case OP_CHECKSIGVERIFY: {
                    byte[] pubKey = pila.pop();
                    byte[] firma = pila.pop();
                    boolean ok = verificador.verificar(firma, pubKey);
                    if (!ok) throw new RuntimeException("OP_CHECKSIGVERIFY falló");
                    break;
                }

                // Opcional avanzado: multisig (mock)
                case OP_CHECKMULTISIG: {
                    boolean ok = ejecutarCheckMultiSig();
                    pila.push(ok ? new byte[]{1} : new byte[]{0});
                    break;
                }

                case OP_CHECKMULTISIGVERIFY: {
                    boolean ok = ejecutarCheckMultiSig();
                    if (!ok) throw new RuntimeException("OP_CHECKMULTISIGVERIFY falló");
                    break;
                }

                default:
                    throw new RuntimeException("Opcode no soportado: " + op);
            }

            if (trace) imprimirTrace(token, true);
        }

        // Si quedan IF sin cerrar, es error
        if (!ifStack.isEmpty()) {
            throw new RuntimeException("Falta OP_ENDIF (hay IF sin cerrar)");
        }
    }

    private void procesarFlujo(OpCode op) {
        switch (op) {
            case OP_IF: {
                boolean parent = seEjecuta();
                if (parent) {
                    boolean cond = HashUtil.esVerdadero(pila.pop());
                    ifStack.push(new IfFrame(true, cond, cond));
                } else {
                    // si el padre NO ejecuta, no se evalúa condición (no se hace pop)
                    ifStack.push(new IfFrame(false, false, false));
                }
                break;
            }

            case OP_NOTIF: {
                boolean parent = seEjecuta();
                if (parent) {
                    boolean cond = HashUtil.esVerdadero(pila.pop());
                    boolean notCond = !cond;
                    ifStack.push(new IfFrame(true, notCond, notCond));
                } else {
                    ifStack.push(new IfFrame(false, false, false));
                }
                break;
            }

            case OP_ELSE: {
                if (ifStack.isEmpty()) throw new RuntimeException("OP_ELSE sin OP_IF");
                IfFrame f = ifStack.peek();
                // si parent ejecuta, se invierte la rama; si no, sigue sin ejecutar
                f.ejecutaAhora = f.parentEjecuta && !f.condicion;
                break;
            }

            case OP_ENDIF: {
                if (ifStack.isEmpty()) throw new RuntimeException("OP_ENDIF sin OP_IF");
                ifStack.pop();
                break;
            }

            default:
                throw new RuntimeException("Flujo no soportado: " + op);
        }
    }

    private boolean ejecutarCheckMultiSig() {
        // Stack (de abajo hacia arriba) típico:
        // dummy, sig1..sigm, m, pubKey1..pubKeyn, n
        // Pop en orden inverso:
        int n = HashUtil.aInt(pila.pop());
        if (n < 0) throw new RuntimeException("n inválido en multisig");

        List<byte[]> pubKeys = new ArrayList<>();
        for (int i = 0; i < n; i++) pubKeys.add(pila.pop());

        int m = HashUtil.aInt(pila.pop());
        if (m < 0 || m > n) throw new RuntimeException("m inválido en multisig");

        List<byte[]> firmas = new ArrayList<>();
        for (int i = 0; i < m; i++) firmas.add(pila.pop());

        // dummy por bug histórico de OP_CHECKMULTISIG
        pila.pop();

        // Mock: cada firma debe "verificar" contra algún pubKey
        List<byte[]> disponibles = new ArrayList<>(pubKeys);
        for (byte[] sig : firmas) {
            boolean match = false;
            for (int i = 0; i < disponibles.size(); i++) {
                if (verificador.verificar(sig, disponibles.get(i))) {
                    disponibles.remove(i);
                    match = true;
                    break;
                }
            }
            if (!match) return false;
        }
        return true;
    }

    private void imprimirTrace(Token token, boolean ejecutado) {
        System.out.println((ejecutado ? "Ejecutado: " : "Saltado: ") + token + " | Stack: " + pila.snapshot());
    }

    public boolean resultadoFinal() {
        return !pila.isEmpty() && HashUtil.esVerdadero(pila.peek());
    }
}
