package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Paciente;
import service.PacienteService;

public class TelaAtualizarPaciente extends JDialog {

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
	private JPanel painelPesquisa;
	private JLabel lblPesquisaCpf;
	private JTextField txfPesquisaCpf;
	private JTextField txfNome;
	private JTextField txfCpf;
	private Paciente pacienteAtual;
	private JButton btnBuscar;
	
	public TelaAtualizarPaciente(PacienteService pacService, TelaPrincipal main) {
		this.pacService = pacService;
		this.main = main;
		setSize(720,480);
		setResizable(false);
		setTitle("Tela de Atualização de Paciente");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		//-----------------------------------
		
		painelPesquisa = new JPanel();
		lblPesquisaCpf = new JLabel("Digite o CPF do paciente:");
		txfPesquisaCpf = new JTextField(20);
		btnBuscar = new JButton("Buscar");
		
		painelPesquisa.add(lblPesquisaCpf);
		painelPesquisa.add(txfPesquisaCpf);
		painelPesquisa.add(btnBuscar);
		
		add(painelPesquisa, BorderLayout.NORTH);
		
		//-----------------------------------
		
		painelForm = new JPanel();
		lblCpf = new JLabel("CPF:");
		txfCpf = new JTextField(20);
		lblNome = new JLabel("Nome:");
		txfNome = new JTextField(20);
		
		painelForm.add(lblCpf);
		painelForm.add(txfCpf);
		painelForm.add(lblNome);
		painelForm.add(txfNome);
		
		add(painelForm, BorderLayout.CENTER);
		
		//------------------------------------
		
		painelBotoes = new JPanel();
		
		btnSair = new JButton("Sair");
		btnLimpar = new JButton("Limpar");
		btnSalvar = new JButton("Salvar");
		btnSalvar.setEnabled(false);

		btnSair.addActionListener(e -> fecharTela());
		btnLimpar.addActionListener(e -> limparCampos());
		btnSalvar.addActionListener(e -> salvarAlteracoes());
		btnBuscar.addActionListener(e -> buscarPaciente());
		
		painelBotoes.add(btnSalvar);
		painelBotoes.add(btnLimpar);
		painelBotoes.add(btnSair);
		add(painelBotoes, BorderLayout.SOUTH);
		setModal(true);
		setVisible(true);

	}
	
	private void buscarPaciente() {
		String cpfPesquisa = txfPesquisaCpf.getText().trim();
        if (cpfPesquisa.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, informe o CPF para busca", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        pacienteAtual = pacService.findByCpf(cpfPesquisa);
        if (pacienteAtual != null) {
            txfCpf.setText(pacienteAtual.getCpf());
        	txfNome.setText(pacienteAtual.getNome());
            btnSalvar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Paciente não encontrado com o CPF informado", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            limparCampos();
        }
    }
	
	private void salvarAlteracoes() {
        if (pacienteAtual == null) {
            JOptionPane.showMessageDialog(this, 
                "Primeiro busque um paciente para atualizar", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String novoCpf = txfCpf.getText().trim();
        String novoNome = txfNome.getText().trim();
        
        if (novoCpf.isEmpty() || novoNome.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Cpf e nome não podem estar vazios", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        pacienteAtual.setCpf(novoCpf);
        pacienteAtual.setNome(novoNome);
        
        pacService.atualizarPaciente(pacienteAtual);
        main.atualizarTabela();
        fecharTela();
        
	}
	
	private void limparCampos() {
        txfCpf.setText("");
        txfNome.setText("");
        txfCpf.setEnabled(true);
        btnSalvar.setEnabled(false);
        pacienteAtual = null;
    }
	
	private void fecharTela() {
		this.hide();
	}

}
