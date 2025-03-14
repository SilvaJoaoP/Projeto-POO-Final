package exception;

public class EntidadeDuplicadaException extends Exception {
    
    private static final long serialVersionUID = 1L;
    private String entidade;
    private String campo;
    private String valor;
    
    public EntidadeDuplicadaException(String entidade, String campo, String valor) {
        super("Já existe um(a) " + entidade + " com " + campo + " = '" + valor + "'");
        this.entidade = entidade;
        this.campo = campo;
        this.valor = valor;
    }
    
    public String getEntidade() {
        return entidade;
    }
    
    public String getCampo() {
        return campo;
    }
    
    public String getValor() {
        return valor;
    }
}