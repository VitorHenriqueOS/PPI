package model;

public class Funcionario {
    private int id;
    private String nome;
    private String turno;
<<<<<<< HEAD
    private boolean ativo; // Novo atributo para Soft Delete

    public Funcionario() {}
    
    public Funcionario(int id, String nome, String turno, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.turno = turno;
        this.ativo = ativo;
=======

    public Funcionario() {}
    public Funcionario(int id, String nome, String turno) {
        this.id = id;
        this.nome = nome;
        this.turno = turno;
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
<<<<<<< HEAD
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
=======
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
}