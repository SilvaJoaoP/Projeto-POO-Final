package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.ParseException;

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
import javax.swing.SwingUtilities;
import javax.swing.text.MaskFormatter;

import model.Exame;
import model.Paciente;
import service.ExameService;
import service.PacienteService;

public class TelaCadastrarExame extends JDialog {

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
    
    private JTextField txfDescricao;
    private JFormattedTextField txfDataExame;
    private JComboBox<Paciente> cmbPaciente;
    
    public TelaCadastrarExame(TelaPrincipal main, ExameService exameService, PacienteService pacienteService) {
        this.main = main;
        this.exameService = exameService;
        this.pacienteService = pacienteService;
        
        setSize(500, 200);
        setResizable(false);
        setTitle("Tela de Cadastro de Exame");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //-----------------------------
        
        painelForm = new JPanel(new GridLayout(3,1,5,10));
        
        lblDescricao = new JLabel("Descrição: ");
        lblDataExame = new JLabel("Data do Exame (dd/mm/aaaa): ");
        lblPaciente = new JLabel("Paciente: ");
        
        txfDescricao = new JTextField(24);
        
        try {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
            txfDataExame = new JFormattedTextField(dateFormatter);

            Dimension dateDimension = new Dimension(100, txfDataExame.getPreferredSize().height);
            txfDataExame.setPreferredSize(dateDimension);
        } catch (ParseException e) {
            txfDataExame = new JFormattedTextField();
            txfDataExame.setPreferredSize(new Dimension(100, txfDataExame.getPreferredSize().height));
        }
        
        cmbPaciente = new JComboBox<>();
        cmbPaciente.setMaximumRowCount(6);
        cmbPaciente.setPreferredSize(new Dimension(txfDescricao.getPreferredSize().width, 
                                                  cmbPaciente.getPreferredSize().height));
        
        cmbPaciente.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
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
        
        //------------------------------
        
        painelBotoes = new JPanel();
        btnSair = new JButton("Sair");
        btnLimpar = new JButton("Limpar");
        btnSalvar = new JButton("Salvar");
        
        btnSair.addActionListener(e -> fecharTela());
        btnLimpar.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(e -> addExame());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnSair);
        add(painelBotoes, BorderLayout.SOUTH);
        
        setModal(true);
        setLocationRelativeTo(main);
        setVisible(true);
    }
    
    private void carregarPacientes() {
        cmbPaciente.removeAllItems();
        
        for (Paciente paciente : pacienteService.getPacientes()) {
            cmbPaciente.addItem(paciente);
        }

        if (cmbPaciente.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                    "Não há pacientes cadastrados. Cadastre um paciente primeiro.", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            fecharTela();
        }
    }
    
    private void addExame() {
        if (txfDescricao.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha a descrição do exame.", 
                    "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String dataExame = txfDataExame.getText().trim();
        if (dataExame.contains("_") || dataExame.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, informe a data do exame corretamente.", 
                    "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Paciente pacienteSelecionado = (Paciente) cmbPaciente.getSelectedItem();
        if (pacienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um paciente.", 
                    "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Exame e = new Exame();
        e.setId(0L);
        e.setDescricao(txfDescricao.getText());
        e.setDataExame(dataExame);
        e.setPaciente(pacienteSelecionado);
        
        exameService.adicionarExame(e);
        
        JOptionPane.showMessageDialog(this, "Exame cadastrado com sucesso");
        
        main.loadTableExame();
        
        fecharTela();
    }
    
    private void limparCampos() {
        txfDescricao.setText("");
        txfDataExame.setText("");
        if (cmbPaciente.getItemCount() > 0) {
            cmbPaciente.setSelectedIndex(0);
        }
    }
    
    private void fecharTela() {
        setVisible(false);
        SwingUtilities.invokeLater(() -> {
            hide();
        });
    }    
   
    public boolean isCadastroRealizado() {
        return false; 
    }
}