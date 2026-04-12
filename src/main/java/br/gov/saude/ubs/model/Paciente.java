package br.gov.saude.ubs.model;

public class Paciente {
    private Integer id;
    private String nome;
    private String cpf;
    private String cns;
    private String dataNascimento;
    private String nomeMae;
    
    // Endereço detalhado
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String complemento;
    private String cep;
    
    // 3 campos de telefone
    private String telPrincipal;   // Celular
    private String telSecundario;  // Recado
    private String telFixo;        // Residencial/Trabalho

    // Construtor vazio (necessário para algumas bibliotecas)
    public Paciente() {}

    // Construtor completo
    public Paciente(Integer id, String nome, String cpf, String cns, String dataNascimento, String nomeMae,
                    String logradouro, String numero, String bairro, String cidade, String complemento,
                    String cep, String telPrincipal, String telSecundario, String telFixo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.cns = cns;
        this.dataNascimento = dataNascimento;
        this.nomeMae = nomeMae;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.complemento = complemento;
        this.cep = cep;
        this.telPrincipal = telPrincipal;
        this.telSecundario = telSecundario;
        this.telFixo = telFixo;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getCns() { return cns; }
    public void setCns(String cns) { this.cns = cns; }

    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getNomeMae() { return nomeMae; }
    public void setNomeMae(String nomeMae) { this.nomeMae = nomeMae; }

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getTelPrincipal() { return telPrincipal; }
    public void setTelPrincipal(String telPrincipal) { this.telPrincipal = telPrincipal; }

    public String getTelSecundario() { return telSecundario; }
    public void setTelSecundario(String telSecundario) { this.telSecundario = telSecundario; }

    public String getTelFixo() { return telFixo; }
    public void setTelFixo(String telFixo) { this.telFixo = telFixo; }
    
}