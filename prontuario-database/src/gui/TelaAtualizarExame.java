package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.text.ParseException;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import exception.CampoObrigatorioException;
import exception.ExameNaoEncontradoException;
import exception.FormatoInvalidoException;
import model.Exame;
import model.Paciente;
import service.ExameService;
import service.PacienteService;

public class TelaAtualizarExame extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private ExameService exameService;
    private PacienteService pacienteService;
    private TelaPrincipal main;
    private JPanel painelForm;
    private JPanel painelBotoes;
    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnSair;
    private JLabel lblDescricao;
    private JLabel lblDataExame;
    private JLabel lblPaciente;
    private JPanel painelPesquisa;
    private JLabel lblPesquisaId;
    private JTextField txfPesquisaId;
    private JTextField txfDescricao;
    private JFormattedTextField txfDataExame;
    private JComboBox<Paciente> cmbPaciente;
    private Exame exameAtual;
    private JButton btnBuscar;
    
    public TelaAtualizarExame(ExameService exameService, PacienteService pacienteService, TelaPrincipal main) {
        this.exameService = exameService;
        this.pacienteService = pacienteService;
        this.main = main;
        setSize(720,200);
        setResizable(false);
        setTitle("Tela de Atualização de Exame");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //-----------------------------------
        
        painelPesquisa = new JPanel();
        lblPesquisaId = new JLabel("Digite o ID do exame:");
        txfPesquisaId = new JTextField(10);
        btnBuscar = new JButton("Buscar");
        
        painelPesquisa.add(lblPesquisaId);
        painelPesquisa.add(txfPesquisaId);
        painelPesquisa.add(btnBuscar);
        
        add(painelPesquisa, BorderLayout.NORTH);
        
        //-----------------------------------
        
        painelForm = new JPanel();
        lblDescricao = new JLabel("Descrição:");
        txfDescricao = new JTextField(24);
        lblDataExame = new JLabel("Data do Exame (dd/mm/aaaa):");
        
        try {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
            txfDataExame = new JFormattedTextField(dateFormatter);
            txfDataExame.setColumns(20);
        } catch (ParseException e) {
            txfDataExame = new JFormattedTextField();
            txfDataExame.setColumns(20);
        }
        
        lblPaciente = new JLabel("Paciente:");
        cmbPaciente = new JComboBox<>();
        
        cmbPaciente.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Paciente) {
                    Paciente paciente = (Paciente) value;
                    setText(paciente.getNome() + " (CPF: " + paciente.getCpf() + ")");
                }
                return this;
            }
        });
        
        carregarPacientes();
        
        painelForm.add(lblDescricao);
        painelForm.add(txfDescricao);
        painelForm.add(lblDataExame);
        painelForm.add(txfDataExame);
        painelForm.add(lblPaciente);
        painelForm.add(cmbPaciente);
        
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
        btnBuscar.addActionListener(e -> buscarExame());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnSair);
        add(painelBotoes, BorderLayout.SOUTH);
        
        setLocationRelativeTo(main);
        setModal(true);
        setVisible(true);
    }
    
    private void carregarPacientes() {
        List<Paciente> pacientes = pacienteService.getPacientes();
        
        if (pacientes != null && !pacientes.isEmpty()) {
            for (Paciente p : pacientes) {
                cmbPaciente.addItem(p);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Não há pacientes cadastrados no sistema.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
        }
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
                txfDataExame.setText(exameAtual.getDataExame());
                
                for (int i = 0; i < cmbPaciente.getItemCount(); i++) {
                    Paciente p = cmbPaciente.getItemAt(i);
                    if (p.getId().equals(exameAtual.getPaciente().getId())) {
                        cmbPaciente.setSelectedIndex(i);
                        break;
                    }
                }
                
                btnSalvar.setEnabled(true);
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
    
    private void salvarAlteracoes() {
        try {
            if (exameAtual == null) {
                JOptionPane.showMessageDialog(this, 
                    "Primeiro busque um exame para atualizar", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            exameAtual.setDescricao(txfDescricao.getText().trim());
            exameAtual.setDataExame(txfDataExame.getText().trim());
            exameAtual.setPaciente((Paciente) cmbPaciente.getSelectedItem());
            
            exameService.atualizarExame(exameAtual);
            JOptionPane.showMessageDialog(this, "Exame atualizado com sucesso!");
            main.atualizarTabelaExames();
            fecharTela();
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
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar exame: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void limparCampos() {
        txfDescricao.setText("");
        txfDataExame.setText("");
        if (cmbPaciente.getItemCount() > 0) {
            cmbPaciente.setSelectedIndex(0);
        }
        btnSalvar.setEnabled(false);
        exameAtual = null;
    }
    
    private void fecharTela() {
        this.hide(); 
    }
}