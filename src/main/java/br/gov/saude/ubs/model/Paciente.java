package br.gov.saude.ubs.model;

public class Paciente {
    private Integer id;
    private String nome;
    private String cns; // Cartão Nacional de Saúde
    private String dataNascimento;

    // Construtor vazio (necessário para algumas bibliotecas)
    public Paciente() {}

    // Construtor completo
    public Paciente(Integer id, String nome, String cns, String dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.cns = cns;
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCns() { return cns; }
    public void setCns(String cns) { this.cns = cns; }

    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
}