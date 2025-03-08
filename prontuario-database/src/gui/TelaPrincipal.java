package gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import model.Exame;
import model.Paciente;
import service.ExameService;
import service.PacienteService;

public class TelaPrincipal extends JFrame{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private JMenuBar barraMenu;
    private JMenu menuPaciente;
    private JMenu menuExame;
    private JMenuItem menuItemAdicionarPaciente;
    private JMenuItem menuItemAtualizarPaciente;
    private JMenuItem menuItemExcluirPaciente;
    private JMenuItem menuItemAdicionarExame;
    private JMenuItem menuItemAtualizarExame;
    private JMenuItem menuItemExcluirExame;
    private JScrollPane scrollPanePacientes;
    private JScrollPane scrollPaneExames;
    private JTable tablePacientes;
    private JTable tableExames;
    private PacienteService pacService;
    private ExameService exameService;
    private JTabbedPane tabbed;
    
    
    public TelaPrincipal(PacienteService pacService, ExameService exameService) {
        this.pacService = pacService;
        this.exameService = exameService;
        setTitle("Gerência de Prontuários");
        setSize(720,480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //------------------------------------
        
        barraMenu = new JMenuBar();
        
        // Menu Paciente é aqui
        menuPaciente = new JMenu("Paciente");
        barraMenu.add(menuPaciente);
        //--------
        menuItemAdicionarPaciente = new JMenuItem("Adicionar");
        menuItemAtualizarPaciente = new JMenuItem("Atualizar");
        menuItemExcluirPaciente = new JMenuItem("Excluir");
        menuItemAdicionarPaciente.addActionListener(e -> new TelaCadastrarPaciente(pacService, this));
        menuItemAtualizarPaciente.addActionListener(e -> new TelaAtualizarPaciente(pacService, this));
        menuItemExcluirPaciente.addActionListener(e -> new TelaExcluirPaciente(pacService, this));
        
        menuPaciente.add(menuItemAdicionarPaciente);
        menuPaciente.add(menuItemAtualizarPaciente);
        menuPaciente.add(menuItemExcluirPaciente);
        
        // Menu Exame é aqui
        menuExame = new JMenu("Exame");
        barraMenu.add(menuExame);
        //--------
        menuItemAdicionarExame = new JMenuItem("Adicionar");
        menuItemAtualizarExame = new JMenuItem("Atualizar");
        menuItemExcluirExame = new JMenuItem("Excluir");
        menuItemAdicionarExame.addActionListener(e -> abrirTelaCadastrarExame());
        menuItemAtualizarExame.addActionListener(e -> new TelaAtualizarExame(exameService, pacService, this));
        menuItemExcluirExame.addActionListener(e -> new TelaExcluirExame(exameService, this));
        
        menuExame.add(menuItemAdicionarExame);
        menuExame.add(menuItemAtualizarExame);
        menuExame.add(menuItemExcluirExame);
        
        add(barraMenu, BorderLayout.NORTH);
        
        // Ações padrão
        //-------------------------------------
        
        tablePacientes = new JTable();
        tableExames = new JTable();
        
        scrollPanePacientes = new JScrollPane(tablePacientes);
        scrollPaneExames = new JScrollPane(tableExames);
        
        tabbed = new JTabbedPane();
        tabbed.addTab("Pacientes", scrollPanePacientes);
        tabbed.addTab("Exames", scrollPaneExames);
        add(tabbed, BorderLayout.CENTER);
        
        loadTablePaciente();
        loadTableExame();
        setVisible(true);
    }
    

    private void abrirTelaCadastrarExame() {
        TelaCadastrarExame dialog = new TelaCadastrarExame(this, exameService, pacService);
        dialog.setVisible(true);
        
        if (dialog.isCadastroRealizado()) {
            loadTableExame();
        }
    }
    
    protected void loadTablePaciente() {
        List<Paciente> itens = pacService.getPacientes();
        System.out.println(itens);
        tablePacientes.setModel(new TabelaPacienteModel(itens));
    }
    
    protected void loadTableExame() {
        List<Exame> itens = exameService.getExames();
        tableExames.setModel(new TabelaExameModel(itens));
    }

    public void atualizarTabelaPacientes() {
        List<Paciente> itens = pacService.getPacientes();
        TabelaPacienteModel modelo = new TabelaPacienteModel(itens);
        tablePacientes.setModel(modelo);
    }
    
    public void atualizarTabelaExames() {
        List<Exame> itens = exameService.getExames();
        TabelaExameModel modelo = new TabelaExameModel(itens);
        tableExames.setModel(modelo);
    }
}