package clases.java;

import java.util.List;

public class InterpreteScript {

    private final Tokenizador tokenizador = new Tokenizador();
    private final MaquinaScript maquina = new MaquinaScript();

    public ResultadoEjecucion validar(String scriptSig,
                                      String scriptPubKey,
                                      boolean trace) {

        try {

            maquina.reset();

            List<Token> tokensSig = tokenizador.tokenizar(scriptSig);
            maquina.ejecutar(tokensSig, trace);

            List<Token> tokensPub = tokenizador.tokenizar(scriptPubKey);
            maquina.ejecutar(tokensPub, trace);

            boolean valido = maquina.resultadoFinal();

            return new ResultadoEjecucion(valido,
                    valido ? "Script valido" : "Script invalido (resultado final es false)");

        } catch (Exception e) {
            return new ResultadoEjecucion(false, "Error: " + e.getMessage());
        }
    }
}
