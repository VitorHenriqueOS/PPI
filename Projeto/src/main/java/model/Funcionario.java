package model;

public class Funcionario {
    private int id;
    private String nome;
    private String turno;

    public Funcionario() {}
    public Funcionario(int id, String nome, String turno) {
        this.id = id;
        this.nome = nome;
        this.turno = turno;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
}