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
        String scriptSig = firma + " " + pubKey;

        // scriptPubKey P2PKH
        String scriptPubKey =
                "OP_DUP OP_HASH160 " + pubKeyHashHex +
                " OP_EQUALVERIFY OP_CHECKSIG";

        InterpreteScript interprete = new InterpreteScript();
        ResultadoEjecucion resultado = interprete.validar(scriptSig, scriptPubKey, trace);

        System.out.println(resultado.getMensaje());
        System.out.println("Valido? " + resultado.esValido());
    }
}