package clases.java;

public class VerificadorFirma {

    public boolean verificar(byte[] firma, byte[] pubKey) {
        return firma.length > 0 && pubKey.length > 0;
    }
}