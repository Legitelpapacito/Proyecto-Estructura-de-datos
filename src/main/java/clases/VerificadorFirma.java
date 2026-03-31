package clases;

import java.nio.charset.StandardCharsets;

/**
 * Clase que se encarga de verificar firmas.
 * En este caso es una validación simple (didáctica).
 */
public class VerificadorFirma {

    /**
     * Verifica si una firma y una clave pública son válidas.
     *
     * @param firma firma a verificar
     * @param pubKey clave pública
     * @return true si ambos valores son válidos, false en caso contrario
     */
    public boolean verificar(byte[] firma, byte[] pubKey) {
        if (firma == null || pubKey == null) return false;
        if (firma.length == 0 || pubKey.length == 0) return false;

        String f = new String(firma, StandardCharsets.UTF_8).trim();
        String p = new String(pubKey, StandardCharsets.UTF_8).trim();
        return !f.isEmpty() && !p.isEmpty();
    }
}