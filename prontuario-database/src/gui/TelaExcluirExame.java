package gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import exception.CampoObrigatorioException;
import exception.ExameNaoEncontradoException;
import exception.FormatoInvalidoException;
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
        try {
            String idPesquisa = txfPesquisaId.getText().trim();
            if (idPesquisa.isEmpty()) {
                throw new CampoObrigatorioException("ID do exame");
            }

            try {
                Long id = Long.parseLong(idPesquisa);
                exameAtual = exameService.localizarExamePorId(id);
                
                txfDescricao.setText(exameAtual.getDescricao());
                txfData.setText(exameAtual.getDataExame());
                
                if (exameAtual.getPaciente() != null) {
                    txfPaciente.setText(exameAtual.getPaciente().getNome() + 
                                    " (CPF: " + exameAtual.getPaciente().getCpf() + ")");
                } else {
                    txfPaciente.setText("Sem paciente associado");
                }
                
                btnDeletar.setEnabled(true);
            } catch (NumberFormatException e) {
                throw new FormatoInvalidoException("ID do exame", "número inteiro");
            }
        } catch (CampoObrigatorioException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Campo obrigatório", 
                JOptionPane.ERROR_MESSAGE);
        } catch (FormatoInvalidoException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Formato inválido", 
                JOptionPane.ERROR_MESSAGE);
        } catch (ExameNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Exame não encontrado", 
                JOptionPane.WARNING_MESSAGE);
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao buscar exame: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void excluir() {
        try {
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
        } catch (ExameNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Exame não encontrado", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao excluir exame: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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