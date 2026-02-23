package clases.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InterpreteScriptTest {

    @Test
    void p2pkh_deberia_ser_valido() {
        String firma = "firma123";
        String pubKey = "clavePublica";
        String pubKeyHashHex = "0x" + HashUtil.aHex(HashUtil.hash160Mock(pubKey.getBytes()));

        String scriptSig = firma + " " + pubKey;
        String scriptPubKey = "OP_DUP OP_HASH160 " + pubKeyHashHex + " OP_EQUALVERIFY OP_CHECKSIG";

        InterpreteScript i = new InterpreteScript();
        ResultadoEjecucion r = i.validar(scriptSig, scriptPubKey, false);

        assertTrue(r.esValido(), r.getMensaje());
    }

    @Test
    void p2pkh_deberia_fallar_si_pubKeyHash_no_coincide() {
        String firma = "firma123";
        String pubKey = "clavePublica";
        String pubKeyHashMalo = "0x" + "00".repeat(20);

        String scriptSig = firma + " " + pubKey;
        String scriptPubKey = "OP_DUP OP_HASH160 " + pubKeyHashMalo + " OP_EQUALVERIFY OP_CHECKSIG";

        InterpreteScript i = new InterpreteScript();
        ResultadoEjecucion r = i.validar(scriptSig, scriptPubKey, false);

        assertFalse(r.esValido());
        assertTrue(r.getMensaje().toLowerCase().contains("equalverify"));
    }

    @Test
    void dup_con_pila_vacia_deberia_fallar() {
        InterpreteScript i = new InterpreteScript();
        ResultadoEjecucion r = i.validar("", "OP_DUP", false);

        assertFalse(r.esValido());
        assertTrue(r.getMensaje().toLowerCase().contains("vac"));
    }
}
