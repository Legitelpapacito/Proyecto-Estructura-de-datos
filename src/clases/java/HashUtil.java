package clases.java;

import java.security.MessageDigest;
import java.util.Arrays;

public final class HashUtil {

    private HashUtil() {}

    /**
     * Version didactica de HASH160.
     * Bitcoin real usa RIPEMD160(SHA256(data)).
     * Para no depender de librerias externas, aqui se hace SHA-256 y se truncan
     * los primeros 20 bytes (160 bits). Es consistente para el proyecto (mock).
     */
    public static byte[] hash160Mock(byte[] data) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] digest = sha.digest(data);
            return Arrays.copyOfRange(digest, 0, 20);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo calcular hash", e);
        }
    }

    public static String aHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(Character.forDigit((b >> 4) & 0xF, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString();
    }

    public static byte[] deHex(String hex) {
        String h = hex.trim();
        if (h.startsWith("0x") || h.startsWith("0X")) h = h.substring(2);
        if (h.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex invalido (largo impar)");
        }
        int n = h.length() / 2;
        byte[] out = new byte[n];
        for (int i = 0; i < n; i++) {
            int hi = Character.digit(h.charAt(i * 2), 16);
            int lo = Character.digit(h.charAt(i * 2 + 1), 16);
            if (hi < 0 || lo < 0) throw new IllegalArgumentException("Hex invalido");
            out[i] = (byte) ((hi << 4) | lo);
        }
        return out;
    }

    public static boolean esVerdadero(byte[] data) {
        if (data == null || data.length == 0) return false;
        for (byte b : data) {
            if (b != 0) return true;
        }
        return false;
    }
}
