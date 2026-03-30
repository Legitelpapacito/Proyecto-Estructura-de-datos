package clases.java;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;

public final class HashUtil {

    private HashUtil() {}

    /**
     * Versión didáctica de HASH160.
     * Bitcoin real usa RIPEMD160(SHA256(data)).
     * Para no depender de librerías externas, aquí se hace SHA-256 y se truncan
     * los primeros 20 bytes (160 bits). Es consistente para el proyecto (mock).
     */
    public static byte[] hash160Mock(byte[] data) {
        return Arrays.copyOfRange(sha256(data), 0, 20);
    }

    /** SHA-256 estándar (para OP_SHA256). */
    public static byte[] sha256(byte[] data) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            return sha.digest(data);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo calcular SHA-256", e);
        }
    }

    /** HASH256 = SHA256(SHA256(data)) (para OP_HASH256). */
    public static byte[] hash256(byte[] data) {
        return sha256(sha256(data));
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
            throw new IllegalArgumentException("Hex inválido (largo impar)");
        }
        int n = h.length() / 2;
        byte[] out = new byte[n];
        for (int i = 0; i < n; i++) {
            int hi = Character.digit(h.charAt(i * 2), 16);
            int lo = Character.digit(h.charAt(i * 2 + 1), 16);
            if (hi < 0 || lo < 0) throw new IllegalArgumentException("Hex inválido");
            out[i] = (byte) ((hi << 4) | lo);
        }
        return out;
    }

    /**
     * Interpretación booleana estilo Script (simplificada):
     * - null o vacío => false
     * - si todos los bytes son 0 => false
     * - si hay al menos un byte distinto de 0 => true
     */
    public static boolean esVerdadero(byte[] data) {
        if (data == null || data.length == 0) return false;
        for (byte b : data) {
            if (b != 0) return true;
        }
        return false;
    }

    /**
     * Conversión didáctica byte[] -> int para operaciones aritméticas.
     * - vacío => 0
     * - 1 byte => unsigned 0..255 (suficiente para OP_0..OP_16)
     * - 4 bytes => int big-endian
     * - otros tamaños => se toman últimos 4 bytes (big-endian) por simplicidad
     */
    public static int aInt(byte[] data) {
        if (data == null || data.length == 0) return 0;
        if (data.length == 1) return data[0] & 0xFF;

        byte[] tmp;
        if (data.length == 4) {
            tmp = data;
        } else if (data.length > 4) {
            tmp = Arrays.copyOfRange(data, data.length - 4, data.length);
        } else {
            // 2 o 3 bytes: pad a 4 (big-endian)
            tmp = new byte[4];
            System.arraycopy(data, 0, tmp, 4 - data.length, data.length);
        }
        return ByteBuffer.wrap(tmp).getInt();
    }

    /**
     * Conversión int -> byte[] para empujar a la pila.
     * Para valores pequeños 0..255 se usa 1 byte; si no, 4 bytes big-endian.
     */
    public static byte[] deInt(int value) {
        if (value >= 0 && value <= 255) {
            return new byte[]{ (byte) value };
        }
        return ByteBuffer.allocate(4).putInt(value).array();
    }
}
