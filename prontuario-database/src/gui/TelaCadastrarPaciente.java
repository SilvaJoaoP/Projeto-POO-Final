package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import exception.CampoObrigatorioException;
import exception.EntidadeDuplicadaException;
import exception.FormatoInvalidoException;
import model.Paciente;
import service.PacienteService;

public class TelaCadastrarPaciente extends JDialog{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private PacienteService pacService;
    private TelaPrincipal main;
    private JPanel painelForm;
    private JPanel painelBotoes;
    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnSair;
    private JLabel lblNome;
    private JLabel lblCpf;
    private JTextField txfNome;
    private JTextField txfCpf;
    
    
    
    public TelaCadastrarPaciente(PacienteService pacService, TelaPrincipal main) {
        this.pacService = pacService;
        this.main = main;
        setSize(380, 200);
        setResizable(false);
        setTitle("Tela de Cadastro de Paciente");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //-----------------------------
        
        painelForm = new JPanel(new GridLayout(2, 1, 5, 5)); 
        
        // Painel para Nome
        JPanel painelNome = new JPanel();
        lblNome = new JLabel("Nome: ");
        txfNome = new JTextField(24); 
        painelNome.add(lblNome);
        painelNome.add(txfNome);
        
        // Painel para CPF
        JPanel painelCpf = new JPanel();
        lblCpf = new JLabel("CPF: ");
        txfCpf = new JTextField(24); 
        painelCpf.add(lblCpf);
        painelCpf.add(txfCpf);
        

        painelForm.add(painelNome);
        painelForm.add(painelCpf);
        
        add(painelForm, BorderLayout.CENTER);
        
        //------------------------------
        
        painelBotoes = new JPanel();
        btnSair = new JButton("Sair");
        btnLimpar = new JButton("Limpar");
        btnSalvar = new JButton("Salvar");
        
        
        btnSair.addActionListener(e -> fecharTela());
        btnLimpar.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(e -> addPaciente());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnSair);
        add(painelBotoes, BorderLayout.SOUTH);
        
        setModal(true);
        setLocationRelativeTo(main);
        setVisible(true);
    }
    
    private void addPaciente() {
        try {
            Paciente p = new Paciente(0L, txfCpf.getText(), txfNome.getText());
            pacService.adicionarPaciente(p);
            JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso");
            txfCpf.setText("");
            txfNome.setText("");
            main.loadTablePaciente();
            fecharTela();
        } catch (CampoObrigatorioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                    "Campo obrigatório", JOptionPane.ERROR_MESSAGE);
        } catch (FormatoInvalidoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                    "Formato inválido", JOptionPane.ERROR_MESSAGE);
        } catch (EntidadeDuplicadaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                    "Registro duplicado", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Erro ao cadastrar paciente: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void limparCampos() {
        txfCpf.setText("");
        txfNome.setText("");
        txfCpf.setEnabled(true);
    }
    
    private void fecharTela() {
        this.hide(); 
    }
}