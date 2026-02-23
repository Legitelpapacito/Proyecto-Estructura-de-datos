package clases.java;

import java.util.ArrayList;
import java.util.List;

public class Tokenizador {

    public List<Token> tokenizar(String script) {

        List<Token> tokens = new ArrayList<>();
        String[] partes = script.split(" ");

        for (String p : partes) {

            if (p.startsWith("OP_")) {
                tokens.add(new Token(OpCode.valueOf(p)));
            } else {
                tokens.add(new Token(p.getBytes()));
            }
        }

        return tokens;
    }
}