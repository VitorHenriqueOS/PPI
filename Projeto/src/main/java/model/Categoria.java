package model;

import java.math.BigDecimal;

public class Categoria {
    private int id;
    private BigDecimal preco;
    private String nome;
    private int capacidade;
    private String tipoCama;

    public Categoria() {}

    public Categoria(int id, BigDecimal preco, String nome, int capacidade, String tipoCama) {
        this.id = id;
        this.preco = preco;
        this.nome = nome;
        this.capacidade = capacidade;
        this.tipoCama = tipoCama;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getCapacidade() { return capacidade; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }

    public String getTipoCama() { return tipoCama; }
    public void setTipoCama(String tipoCama) { this.tipoCama = tipoCama; }
}