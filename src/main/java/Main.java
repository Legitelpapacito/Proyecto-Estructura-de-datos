package main.java;

import clases.java.HashUtil;
import clases.java.InterpreteScript;
import clases.java.ResultadoEjecucion;

public class Main {

    public static void main(String[] args) {

        boolean trace = false;
        for (String a : args) {
            if ("--trace".equalsIgnoreCase(a)) trace = true;
        }

        // Datos MOCK (en la fase 1 se simula la firma)
        String firma = "firma123";
        String pubKey = "clavePublica";

        // HASH160 (mock) del pubKey, representado en hex para que sea comparable byte-a-byte
        String pubKeyHashHex = "0x" + HashUtil.aHex(HashUtil.hash160Mock(pubKey.getBytes()));

        // scriptSig: empuja firma y pubKey
        String scriptSig = "OP_1";
        String scriptPubKey = "OP_IF OP_2 OP_3 OP_ADD OP_ELSE OP_0 OP_ENDIF";

        InterpreteScript interprete = new InterpreteScript();
        ResultadoEjecucion resultado = interprete.validar(scriptSig, scriptPubKey, trace);

        System.out.println(resultado.getMensaje());
        System.out.println("Valido? " + resultado.esValido());
    }
}