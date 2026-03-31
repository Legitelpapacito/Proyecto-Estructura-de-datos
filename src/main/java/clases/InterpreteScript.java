package clases;

import java.util.List;

/**
 * Clase que se encarga de interpretar y validar scripts.
 * Ejecuta primero el scriptSig y luego el scriptPubKey.
 */
public class InterpreteScript {

    /** Tokenizador para separar el script en tokens */
    private final Tokenizador tokenizador = new Tokenizador();

    /** Máquina que ejecuta los scripts */
    private final MaquinaScript maquina = new MaquinaScript();

    /**
     * Valida un script completo.
     *
     * @param scriptSig script de entrada
     * @param scriptPubKey script de verificación
     * @param trace si se quiere ver la ejecución paso a paso
     * @return resultado de la ejecución
     */
    public ResultadoEjecucion validar(String scriptSig,String scriptPubKey,boolean trace) {

        try {
            maquina.reset();

            List<Token> tokensSig = tokenizador.tokenizar(scriptSig);
            maquina.ejecutar(tokensSig, trace);

            List<Token> tokensPub = tokenizador.tokenizar(scriptPubKey);
            maquina.ejecutar(tokensPub, trace);

            boolean valido = maquina.resultadoFinal();
            return new ResultadoEjecucion(valido,
                    valido ? "Script válido" : "Script inválido (resultado final es false)");

        } catch (Exception e) {
            return new ResultadoEjecucion(false, "Error: " + e.getMessage());
        }
    }
}