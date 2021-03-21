package br.com.primefaces.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.primefaces.dao.GenericDAO;
import br.com.primefaces.model.Telephone;
import br.com.primefaces.model.UserPerson;
import br.com.primefaces.repository.DaoImpl;


@ViewScoped
@ManagedBean(name = "telephoneBean")
public class TelephoneBean {
	
	private DaoImpl dao = new DaoImpl();
	private Telephone telefone = new Telephone();
	private UserPerson userPerson = new UserPerson();
	private List<Telephone> listaTelefone = new ArrayList<Telephone>();
	private GenericDAO<Telephone> dao_telefone = new GenericDAO<Telephone>();

	
	@PostConstruct
	public void init() {
		String codigo_user = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigouser");
		userPerson = dao.pesquisar(Long.parseLong(codigo_user), UserPerson.class);
	}
	public String remover() {
		dao_telefone.delete(telefone);
		userPerson = dao.pesquisar(userPerson.getId(), UserPerson.class);
		telefone = new Telephone();	
		
		FacesContext.getCurrentInstance().addMessage
		(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Telefone excluído com sucesso!"));
		
		return "";
	}
	public String salvar() {
		telefone.setUsuario(userPerson);
		dao_telefone.persistir(telefone);
		telefone = new Telephone();
		userPerson = dao.pesquisar(userPerson.getId(), UserPerson.class);
		
		FacesContext.getCurrentInstance().addMessage
		(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
		"Informação: ", "Telefone cadastrado com sucesso!"));
		
		return "";
	}
	
	public UserPerson getUserPerson() {
		return userPerson;
	}

	public void setUserPerson(UserPerson userPerson) {
		this.userPerson = userPerson;
	}

	public Telephone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telephone telefone) {
		this.telefone = telefone;
	}
	public List<Telephone> getListaTelefone() {
		return listaTelefone;
	}

	public void setListaTelefone(List<Telephone> listaTelefone) {
		this.listaTelefone = listaTelefone;
	}
}
