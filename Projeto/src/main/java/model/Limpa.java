package model;

import java.sql.Date;

public class Limpa {
    private int id;
    private Date data;
    private String obs;
    private int numeroQuarto; // FK Quarto
    private int idFuncionario; // FK Funcionario (IDF no banco)

    public Limpa() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }
    public String getObs() { return obs; }
    public void setObs(String obs) { this.obs = obs; }
    public int getNumeroQuarto() { return numeroQuarto; }
    public void setNumeroQuarto(int numeroQuarto) { this.numeroQuarto = numeroQuarto; }
    public int getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(int idFuncionario) { this.idFuncionario = idFuncionario; }
}