package clases.java;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Tokenizador {

    public List<Token> tokenizar(String script) {

        List<Token> tokens = new ArrayList<>();
        if (script == null) return tokens;

        String limpio = script.trim();
        if (limpio.isEmpty()) return tokens;

        String[] partes = limpio.split("\\s+");

        for (String p : partes) {

            if (p.startsWith("OP_")) {
                tokens.add(new Token(OpCode.valueOf(p)));
                continue;
            }

            // Formato <texto> => bytes UTF-8 (util para firma/pubKey mock)
            if (p.startsWith("<") && p.endsWith(">") && p.length() >= 2) {
                String inner = p.substring(1, p.length() - 1);
                tokens.add(new Token(inner.getBytes(StandardCharsets.UTF_8)));
                continue;
            }

            // Formato 0xA1B2... => bytes reales
            if (p.startsWith("0x") || p.startsWith("0X")) {
                tokens.add(new Token(HashUtil.deHex(p)));
                continue;
            }

            // Por defecto: tomar el token como texto UTF-8
            tokens.add(new Token(p.getBytes(StandardCharsets.UTF_8)));
        }

        return tokens;
    }
}