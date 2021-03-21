package br.com.primefaces.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
public class UserPerson  implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER ,cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Telephone> telefones;
	
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY , orphanRemoval = true, cascade = CascadeType.REMOVE)
	private List<Email> emails;
	
	@NotBlank(message = "Login ou senha inválidos.")
	@NotEmpty
	private String login;
	
	@NotBlank
	@NotEmpty
	private String nome;
	
	@NotBlank
	@NotEmpty
	private String sobrenome;
	
	@Lob
	private String imagem;
	private String profissao;
	
	@NotBlank
	@NotEmpty
	private String senha;
	
	@NotBlank
	@NotEmpty
	private String sexo;
	
	@NotBlank
	@NotEmpty
	private String cep;
	
	@NotBlank
	@NotEmpty
	private String logradouro;
	
	@NotBlank
	@NotEmpty
	private String complemento;
	
	@NotBlank
	@NotEmpty
	private String bairro;
	
	@NotBlank
	@NotEmpty
	private String localidade;
	
	@NotBlank
	@NotEmpty
	private String uf;
	
	private Double salario;
		
	
	public Long getId() {
		return id;
	}
	public String getLogin() {
		return login;
	}
	public String getSenha() {
		return senha;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserPerson other = (UserPerson) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getNome() {
		return nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public String getProfissao() {
		return profissao;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}
	public List<Telephone> getTelefones() {
		return telefones;
	}
	public void setTelefones(List<Telephone> telefones) {
		this.telefones = telefones;
	}
	public String getCep() {
		return cep;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public String getComplemento() {
		return complemento;
	}
	public String getBairro() {
		return bairro;
	}
	public String getLocalidade() {
		return localidade;
	}
	public String getUf() {
		return uf;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public Double getSalario() {
		return salario;
	}
	public void setSalario(Double salario) {
		this.salario = salario;
	}
	public List<Email> getEmails() {
		return emails;
	}
	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
}
