package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.Exame;

public class TabelaExameModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private List<Exame> exames;
    private String[] colunas = {"ID", "Descrição", "Data do Exame", "CPF do Paciente"};
    
    public TabelaExameModel(List<Exame> itens) {
        this.exames = itens;
    }
    
    @Override
    public int getRowCount() {
        return exames.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }
    
    @Override
    public String getColumnName(int index) {
        return colunas[index];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Exame e = exames.get(rowIndex);
        return switch(columnIndex) {
            case 0 -> e.getId();
            case 1 -> e.getDescricao();
            case 2 -> e.getDataExame();
            case 3 -> e.getPaciente() != null ? e.getPaciente().getCpf() : "Não associado";
            default -> null;
        };
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1 || columnIndex == 2;
    }
    
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Exame e = exames.get(rowIndex);
        switch(columnIndex) {
            case 1:
                e.setDescricao((String) value);
                break;
            case 2:
                e.setDataExame((String) value);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    public Exame getExame(int rowIndex) {
        return exames.get(rowIndex);
    }
}