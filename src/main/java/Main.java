package main.java;

import java.security.MessageDigest;

public class Main {

    public static void main(String[] args) throws Exception {

        String firma = "firma123";
        String pubKey = "clavePublica";

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha.digest(pubKey.getBytes());
        String pubKeyHash = new String(hash);

        String scriptSig = firma + " " + pubKey;

        String scriptPubKey =
                "OP_DUP OP_HASH160 " + pubKeyHash +
                " OP_EQUALVERIFY OP_CHECKSIG";

        InterpreteScript interprete = new InterpreteScript();

        ResultadoEjecucion resultado =
                interprete.validar(scriptSig, scriptPubKey, true);

        System.out.println("Resultado final: " + resultado.getMensaje());
    }
}