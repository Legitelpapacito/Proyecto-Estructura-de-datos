package clases.java;

import java.nio.charset.StandardCharsets;

public class VerificadorFirma {

    public boolean verificar(byte[] firma, byte[] pubKey) {
        if (firma == null || pubKey == null) return false;
        if (firma.length == 0 || pubKey.length == 0) return false;

        String f = new String(firma, StandardCharsets.UTF_8).trim();
        String p = new String(pubKey, StandardCharsets.UTF_8).trim();
        return !f.isEmpty() && !p.isEmpty();
    }
}
