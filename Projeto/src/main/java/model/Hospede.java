package model;

import java.sql.Date;

public class Hospede {
    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    private Date dataNascimento;
<<<<<<< HEAD
    private boolean ativo; // Novo atributo para Soft Delete

    public Hospede() {}

    public Hospede(String cpf, String nome, String email, String telefone, Date dataNascimento, boolean ativo) {
=======

    public Hospede() {}

    public Hospede(String cpf, String nome, String email, String telefone, Date dataNascimento) {
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
<<<<<<< HEAD
        this.ativo = ativo;
=======
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
    }

    // Getters e Setters
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }
<<<<<<< HEAD

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
=======
>>>>>>> 462dee34ddf02cabdd4d76dbd8a9eed41cac3b14
}