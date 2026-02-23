package clases.java;

import java.util.List;

public class InterpreteScript {

    private Tokenizador tokenizador = new Tokenizador();
    private MaquinaScript maquina = new MaquinaScript();

    public ResultadoEjecucion validar(String scriptSig,
                                      String scriptPubKey,
                                      boolean trace) {

        try {

            List<Token> tokensSig = tokenizador.tokenizar(scriptSig);
            maquina.ejecutar(tokensSig, trace);

            List<Token> tokensPub = tokenizador.tokenizar(scriptPubKey);
            maquina.ejecutar(tokensPub, trace);

            boolean valido = maquina.resultadoFinal();

            return new ResultadoEjecucion(valido,
                    valido ? "Script válido" : "Script inválido");

        } catch (Exception e) {
            return new ResultadoEjecucion(false, e.getMessage());
        }
    }
}