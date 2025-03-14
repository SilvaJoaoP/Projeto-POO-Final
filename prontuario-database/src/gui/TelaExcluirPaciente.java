package gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import exception.CampoObrigatorioException;
import exception.PacienteNaoEncontradoException;
import model.Paciente;
import service.ExameService;
import service.PacienteService;

public class TelaExcluirPaciente extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PacienteService pacService;
	private ExameService exameService;
	private TelaPrincipal main;
	private JPanel painelForm;
	private JPanel painelBotoes;
	private JButton btnDeletar;
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
	
	public TelaExcluirPaciente(PacienteService pacService, ExameService exameService, TelaPrincipal main) {
		this.pacService = pacService;
		this.exameService = exameService;
		this.main = main;
		
		setSize(580, 200);
		setResizable(false);
		setTitle("Tela de Exclusão de Paciente");
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
		btnDeletar = new JButton("Deletar");
		btnDeletar.setEnabled(false);
		
		btnSair.addActionListener(e -> fecharTela());
		btnLimpar.addActionListener(e -> limparCampos());
		btnDeletar.addActionListener(e -> excluir());
		btnBuscar.addActionListener(e -> buscarPaciente());
				
		painelBotoes.add(btnDeletar);
		painelBotoes.add(btnLimpar);
		painelBotoes.add(btnSair);
		add(painelBotoes, BorderLayout.SOUTH);
		
		setLocationRelativeTo(main);
		setModal(true);
		setVisible(true);
	}
	
	private void buscarPaciente() {
		try {
			String cpfPesquisa = txfPesquisaCpf.getText().trim();
			
			Paciente pacienteEncontrado = pacService.findByCpf(cpfPesquisa);
			
			if (pacienteEncontrado != null) {
				pacienteAtual = pacienteEncontrado;
				txfCpf.setText(pacienteAtual.getCpf());
				txfNome.setText(pacienteAtual.getNome());
				btnDeletar.setEnabled(true);
			} else {
				JOptionPane.showMessageDialog(this, 
					"Paciente não encontrado com o CPF informado", 
					"Aviso", 
					JOptionPane.WARNING_MESSAGE);
				limparCampos();
			}
		} catch (CampoObrigatorioException e) {
			JOptionPane.showMessageDialog(this, 
				e.getMessage(), 
				"Campo obrigatório", 
				JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, 
				"Erro ao buscar paciente: " + e.getMessage(), 
				"Erro", 
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    }
	
	private void excluir() {
		try {
			if (pacienteAtual == null) {
				JOptionPane.showMessageDialog(this, 
					"Primeiro busque um paciente para excluir", 
					"Erro", 
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (exameService != null && !exameService.getExamesPorPaciente(pacienteAtual.getId()).isEmpty()) {
				JOptionPane.showMessageDialog(this,
					"Este paciente possui exames cadastrados.\n" +
					"Exclua todos os exames deste paciente antes de excluí-lo.",
					"Não é possível excluir",
					JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			int confirmacao = JOptionPane.showConfirmDialog(this,
				"Tem certeza que deseja excluir este paciente?\n" +
				"Nome: " + pacienteAtual.getNome() + "\n" +
				"CPF: " + pacienteAtual.getCpf(),
				"Confirmar exclusão",
				JOptionPane.YES_NO_OPTION);
				
			if (confirmacao == JOptionPane.YES_OPTION) {
				pacService.deletarPaciente(pacienteAtual);
				JOptionPane.showMessageDialog(this, "Paciente excluído com sucesso!");
				main.atualizarTabelaPacientes();
				fecharTela();
			}
		} catch (PacienteNaoEncontradoException e) {
			JOptionPane.showMessageDialog(this, 
				e.getMessage(), 
				"Paciente não encontrado", 
				JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			if (e.getMessage() != null && (
				   e.getMessage().contains("foreign key") 
				|| e.getMessage().contains("constraint") 
				|| e.getMessage().contains("restrição")
				|| e.getMessage().toLowerCase().contains("integrity"))) {
				
				JOptionPane.showMessageDialog(this, 
					"Não é possível excluir este paciente porque existem exames associados a ele.\n" +
					"Exclua todos os exames deste paciente antes de excluí-lo.",
					"Operação não permitida", 
					JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, 
					"Erro ao excluir paciente: " + e.getMessage(), 
					"Erro", 
					JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}
	
	private void limparCampos() {
        txfCpf.setText("");
        txfNome.setText("");
        txfCpf.setEnabled(true);
        btnDeletar.setEnabled(false);
        pacienteAtual = null;
    }
	
	private void fecharTela() {
		this.hide(); 
	}
}