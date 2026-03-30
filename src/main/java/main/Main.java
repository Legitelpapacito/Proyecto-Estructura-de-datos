package main;

import clases.HashUtil;
import clases.InterpreteScript;
import clases.ResultadoEjecucion;

public class Main {

    public static void main(String[] args) {

        boolean trace = false;
        for (String a : args) {
            if ("--trace".equalsIgnoreCase(a)) trace = true;
        }

        String firma = "firma123";
        String pubKey = "clavePublica";
        String pubKeyHashHex = "0x" + HashUtil.aHex(HashUtil.hash160Mock(pubKey.getBytes()));

        InterpreteScript interprete = new InterpreteScript();

        // 🔹 DEMO 1: P2PKH válido
        ResultadoEjecucion r1 = interprete.validar(
                firma + " " + pubKey,
                "OP_DUP OP_HASH160 " + pubKeyHashHex + " OP_EQUALVERIFY OP_CHECKSIG",
                trace
        );
        System.out.println("P2PKH válido: " + r1.esValido());

        // 🔹 DEMO 2: P2PKH inválido
        ResultadoEjecucion r2 = interprete.validar(
                firma + " " + pubKey,
                "OP_DUP OP_HASH160 0x0000000000000000000000000000000000000000 OP_EQUALVERIFY OP_CHECKSIG",
                trace
        );
        System.out.println("P2PKH inválido: " + r2.esValido());

        // 🔹 DEMO 3: IF / ELSE
        ResultadoEjecucion r3 = interprete.validar(
                "OP_1",
                "OP_IF OP_2 OP_3 OP_ADD OP_ELSE OP_0 OP_ENDIF",
                trace
        );
        System.out.println("Condicional IF: " + r3.esValido());
    }
}