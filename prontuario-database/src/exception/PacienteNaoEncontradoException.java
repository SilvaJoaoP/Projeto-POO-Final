package exception;

public class PacienteNaoEncontradoException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    private String cpf;
    
    public PacienteNaoEncontradoException(String cpf) {
        super("Paciente com CPF '" + cpf + "' não foi encontrado.");
        this.cpf = cpf;
    }
    
    public String getCpf() {
        return cpf;
    }
}