package gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Exame;
import service.ExameService;

public class TelaExcluirExame extends JDialog {

    private static final long serialVersionUID = 1L;
    
    private ExameService exameService;
    private TelaPrincipal main;
    private JPanel painelPesquisa;
    private JPanel painelForm;
    private JPanel painelBotoes;
    private JButton btnDeletar;
    private JButton btnLimpar;
    private JButton btnSair;
    private JLabel lblPesquisaId;
    private JTextField txfPesquisaId;
    private JButton btnBuscar;
    private JLabel lblDescricao;
    private JLabel lblData;
    private JLabel lblPaciente;
    private JTextField txfDescricao;
    private JTextField txfData;
    private JTextField txfPaciente;
    private Exame exameAtual;
    
    public TelaExcluirExame(ExameService exameService, TelaPrincipal main) {
        this.exameService = exameService;
        this.main = main;
        
        setSize(400, 200);
        setResizable(false);
        setTitle("Tela de Exclusão de Exame");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        painelPesquisa = new JPanel();
        lblPesquisaId = new JLabel("Digite o ID do exame:");
        txfPesquisaId = new JTextField(10);
        btnBuscar = new JButton("Buscar");
        
        painelPesquisa.add(lblPesquisaId);
        painelPesquisa.add(txfPesquisaId);
        painelPesquisa.add(btnBuscar);
        
        add(painelPesquisa, BorderLayout.NORTH);
        
        painelForm = new JPanel();
        lblDescricao = new JLabel("Descrição:");
        txfDescricao = new JTextField(24);
        txfDescricao.setEditable(false);
        
        lblData = new JLabel("Data do Exame:");
        txfData = new JTextField(24);
        txfData.setEditable(false);
        
        lblPaciente = new JLabel("Paciente:");
        txfPaciente = new JTextField(24);
        txfPaciente.setEditable(false);
        
        painelForm.add(lblDescricao);
        painelForm.add(txfDescricao);
        painelForm.add(lblData);
        painelForm.add(txfData);
        painelForm.add(lblPaciente);
        painelForm.add(txfPaciente);
        
        add(painelForm, BorderLayout.CENTER);
                
        painelBotoes = new JPanel();
        btnDeletar = new JButton("Deletar");
        btnLimpar = new JButton("Limpar");
        btnSair = new JButton("Sair");
        btnDeletar.setEnabled(false);
        
        btnDeletar.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparCampos());
        btnSair.addActionListener(e -> fecharTela());
        btnBuscar.addActionListener(e -> buscarExame());
                
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnSair);
        add(painelBotoes, BorderLayout.SOUTH);
        
        setLocationRelativeTo(main);
        setModal(true);
        setVisible(true);
    }
    
    private void buscarExame() {
        String idPesquisa = txfPesquisaId.getText().trim();
        if (idPesquisa.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, informe o ID do exame para busca", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Long id = Long.parseLong(idPesquisa);
            exameAtual = exameService.localizarExamePorId(id);
            
            if (exameAtual != null) {
                txfDescricao.setText(exameAtual.getDescricao());
                txfData.setText(exameAtual.getDataExame());
                
                if (exameAtual.getPaciente() != null) {
                    txfPaciente.setText(exameAtual.getPaciente().getNome() + 
                                      " (CPF: " + exameAtual.getPaciente().getCpf() + ")");
                } else {
                    txfPaciente.setText("Sem paciente associado");
                }
                
                btnDeletar.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Exame não encontrado com o ID informado", 
                    "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
                limparCampos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, informe um ID válido (número inteiro)", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluir() {
        if (exameAtual == null) {
            JOptionPane.showMessageDialog(this, 
                "Primeiro busque um exame para excluir", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir este exame?\n" +
            "Descrição: " + exameAtual.getDescricao() + "\n" +
            "Data: " + exameAtual.getDataExame(),
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            exameService.deletarExame(exameAtual);
            JOptionPane.showMessageDialog(this, "Exame excluído com sucesso!");
            main.loadTableExame();
            fecharTela();
        }
    }
    
    private void limparCampos() {
        txfDescricao.setText("");
        txfData.setText("");
        txfPaciente.setText("");
        btnDeletar.setEnabled(false);
        exameAtual = null;
    }
    
    private void fecharTela() {
        this.hide();
    }
}