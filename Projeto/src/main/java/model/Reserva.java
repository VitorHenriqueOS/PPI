package model;

import java.sql.Date;

public class Reserva {
    private int id;
    private Date dataInicio;
    private Date dataFim;
    private String cpf; // FK Hospede
    private int numero; // FK Quarto

    public Reserva() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getDataInicio() { return dataInicio; }
    public void setDataInicio(Date dataInicio) { this.dataInicio = dataInicio; }
    public Date getDataFim() { return dataFim; }
    public void setDataFim(Date dataFim) { this.dataFim = dataFim; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
}