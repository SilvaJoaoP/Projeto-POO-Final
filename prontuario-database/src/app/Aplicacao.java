package app;

import javax.swing.SwingUtilities;

import dao.ExameDAO;
import dao.PacienteDAO;
import db.MySQLDatabaseConnection;
import gui.TelaPrincipal;
import service.ExameService;
import service.PacienteService;

public class Aplicacao {

    public static void main(String[] args) {
        MySQLDatabaseConnection dbConnection = new MySQLDatabaseConnection();
        
        PacienteDAO pacienteDAO = new PacienteDAO(dbConnection);
        ExameDAO exameDAO = new ExameDAO(dbConnection);
        
        PacienteService pacienteService = new PacienteService(pacienteDAO);
        ExameService exameService = new ExameService(exameDAO);
        
        SwingUtilities.invokeLater(() -> 
            new TelaPrincipal(pacienteService, exameService).setVisible(true)
        );
    }
}