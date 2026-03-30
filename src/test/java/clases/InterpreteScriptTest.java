package clases;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
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

    //Opcodes exito y falla
    @Test
    void op_add_deberia_funcionar() {
        InterpreteScript i = new InterpreteScript();

        ResultadoEjecucion r = i.validar(
            "OP_2 OP_3",
            "OP_ADD OP_5 OP_EQUAL",
            false
        );

        assertTrue(r.esValido());
    }

    @Test
    void op_add_deberia_fallar() {
        InterpreteScript i = new InterpreteScript();

        ResultadoEjecucion r = i.validar(
            "OP_2 OP_3",
            "OP_ADD OP_6 OP_EQUAL",
            false
        );

        assertFalse(r.esValido());
    }

    //Casos borde
    @Test
    void op_dup_con_pila_vacia_deberia_fallar() {
        InterpreteScript i = new InterpreteScript();

        ResultadoEjecucion r = i.validar("", "OP_DUP", false);

        assertFalse(r.esValido());
    }

    @Test
    void op_drop_con_pila_vacia_deberia_fallar() {
        InterpreteScript i = new InterpreteScript();

        ResultadoEjecucion r = i.validar("", "OP_DROP", false);

        assertFalse(r.esValido());
    }

    @Test
    void op_checksig_sin_datos_deberia_fallar() {
        InterpreteScript i = new InterpreteScript();

        ResultadoEjecucion r = i.validar("", "OP_CHECKSIG", false);

        assertFalse(r.esValido());
    }

    //If verdadero
    @Test
    void if_deberia_ejecutar_true() {
        InterpreteScript i = new InterpreteScript();

        ResultadoEjecucion r = i.validar(
            "OP_1",
            "OP_IF OP_2 OP_ELSE OP_3 OP_ENDIF OP_2 OP_EQUAL",
            false
        );

        assertTrue(r.esValido());
    }

    //if falso
    @Test
    void if_deberia_ejecutar_else() {
        InterpreteScript i = new InterpreteScript();

        ResultadoEjecucion r = i.validar(
            "OP_0",
            "OP_IF OP_2 OP_ELSE OP_3 OP_ENDIF OP_3 OP_EQUAL",
            false
        );

        assertTrue(r.esValido());
    }

    //if anidado
    @Test
    void if_anidado_deberia_funcionar() {
        InterpreteScript i = new InterpreteScript();

        ResultadoEjecucion r = i.validar(
            "OP_1",
            "OP_IF OP_1 OP_IF OP_2 OP_ELSE OP_3 OP_ENDIF OP_ENDIF OP_2 OP_EQUAL",
            false
        );

        assertTrue(r.esValido());
    }


    //if sin endif
    @Test
    void if_sin_endif_deberia_fallar() {
        InterpreteScript i = new InterpreteScript();

        ResultadoEjecucion r = i.validar(
            "OP_1",
            "OP_IF OP_2",
            false
        );

        assertFalse(r.esValido());
    }

    //P2PKH
    @Test
    void p2pkh_valido() {
        String firma = "firma123";
        String pubKey = "clavePublica";
        String hash = "0x" + HashUtil.aHex(HashUtil.hash160Mock(pubKey.getBytes()));

        InterpreteScript i = new InterpreteScript();

        ResultadoEjecucion r = i.validar(
            firma + " " + pubKey,
            "OP_DUP OP_HASH160 " + hash + " OP_EQUALVERIFY OP_CHECKSIG",
            false
        );

        assertTrue(r.esValido());
    }

    //Incorrecto
    @Test
    void p2pkh_invalido() {
        String firma = "firma123";
        String pubKey = "clavePublica";
        String hashMalo = "0x" + "00".repeat(20);

        InterpreteScript i = new InterpreteScript();

        ResultadoEjecucion r = i.validar(
            firma + " " + pubKey,
            "OP_DUP OP_HASH160 " + hashMalo + " OP_EQUALVERIFY OP_CHECKSIG",
            false
        );

        assertFalse(r.esValido());
    }
    
}
