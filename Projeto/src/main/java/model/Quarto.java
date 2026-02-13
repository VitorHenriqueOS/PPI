package model;

public class Quarto {
    private int numero;
    private int andar;
    private String status;
    private int idCategoria;

    public Quarto() {}

    public Quarto(int numero, int andar, String status, int idCategoria) {
        this.numero = numero;
        this.andar = andar;
        this.status = status;
        this.idCategoria = idCategoria;
    }
    

    // Getters e Setters
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public int getAndar() { return andar; }
    public void setAndar(int andar) { this.andar = andar; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    
    private String nomeCategoria;

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
}