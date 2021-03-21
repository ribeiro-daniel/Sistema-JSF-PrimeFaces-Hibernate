package br.com.primefaces.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.PieChartModel;
import org.primefaces.model.file.UploadedFile;

import com.google.gson.Gson;

import br.com.primefaces.dao.EmailDAO;
import br.com.primefaces.dao.GenericDAO;
import br.com.primefaces.model.Email;
import br.com.primefaces.model.UserPerson;
import br.com.primefaces.repository.DaoImpl;

@ViewScoped
@ManagedBean(name = "userPersonBean")
public class UserPersonBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private UserPerson user = new UserPerson();
	private GenericDAO<UserPerson> dao = new GenericDAO<UserPerson>();
	private List<UserPerson> usuarios = new ArrayList<UserPerson>();
	private DaoImpl daoImpl = new DaoImpl();
	private PieChartModel model;
	private EmailDAO emailDAO = new EmailDAO();
	private Email emailUser = new Email();
	private String usuarioPesquisado;

	public void pesquisarUsuario() {
		usuarios = daoImpl.pesquisarUsuario(usuarioPesquisado);
	}

	public void upload(FileUploadEvent imagem) {
		UploadedFile file = imagem.getFile();
		if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
			String img = "data:image/png;base64," + DatatypeConverter.printBase64Binary(imagem.getFile().getContent());
			user.setImagem(img);
		}
	}

	public void download() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownloadId = params.get("downloadid");

		UserPerson userPerson = daoImpl.pesquisar(Long.parseLong(fileDownloadId), UserPerson.class);

		new Base64();
		byte[] imagem = Base64.decode(userPerson.getImagem().split("\\,")[1]);

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		response.addHeader("Content-Disposition", "attachment; filename=download.png");
		response.setContentType("application/octet-stream");
		response.setContentLength(imagem.length);
		response.getOutputStream().write(imagem);
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
	}

	@PostConstruct
	public void carregar() {
		usuarios = dao.list(user);
		graficoSalario();
	}

	public void graficoSalario() {
		model = new PieChartModel();

		for (UserPerson userPerson : usuarios) {
			model.set(userPerson.getNome(), userPerson.getSalario());
		}

		model.setTitle("Salários");
		model.setLegendPosition("w");

	}

	public void addEmail() {
		emailUser.setUsuario(user);
		emailUser = emailDAO.updateMerge(emailUser);

		user.getEmails().add(emailUser);
		emailUser = new Email();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", "E-mail criado c/ sucesso!"));
	}

	public void removerEmail() {
		String codigo_email = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("codigoEmail");

		Email remover = new Email();
		remover.setId(Long.parseLong(codigo_email));
		emailDAO.delete(remover);

		user.getEmails().remove(remover);
		FacesContext.getCurrentInstance().addMessage("messages",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", "E-mail removido."));
	}

	public void salvar() {
		if (daoImpl.validaLoginDuplicado(user.getLogin()) != 0) {
			FacesContext.getCurrentInstance().addMessage("messages",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Login já existe."));
		} else {
			dao.persistir(user);
			user = new UserPerson();
			carregar();
			FacesContext.getCurrentInstance().addMessage("messages",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Criado c/ sucesso"));
		}
	}

	public void atualizar() {
		dao.updateMerge(user);
		user = new UserPerson();
		carregar();
		FacesContext.getCurrentInstance().addMessage("messages",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Registro atualizado."));
	}

	public String limpar() {
		user = new UserPerson();
		return "";
	}

	public String deletar() {
		dao.delete(user);
		user = new UserPerson();
		carregar();
		FacesContext.getCurrentInstance().addMessage("messages",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Registro deletado."));
		return "";
	}

	public String validar() {
		UserPerson userLogado = daoImpl.validaLogin(user.getLogin(), user.getSenha());

		if (userLogado != null) {
			// Adicionando usuário na sessão
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext external = context.getExternalContext();
			external.getSessionMap().put("usuario", userLogado);

			return "index.jsf";
		}

		return "";
	}

	public String pesquisaCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + user.getCep() + "/json/");
			try {
				URLConnection connection = url.openConnection();
				InputStream is = connection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

				String cep = null;
				StringBuilder jsonCep = new StringBuilder();

				while ((cep = br.readLine()) != null) {
					jsonCep.append(cep);
				}

				UserPerson userPersonAuxiliar = new Gson().fromJson(jsonCep.toString(), UserPerson.class);

				user.setCep(userPersonAuxiliar.getCep());
				user.setLogradouro(userPersonAuxiliar.getLogradouro());
				user.setLocalidade(userPersonAuxiliar.getLocalidade());
				user.setComplemento(userPersonAuxiliar.getComplemento());
				user.setBairro(userPersonAuxiliar.getBairro());
				user.setUf(userPersonAuxiliar.getUf());

			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return "";
	}

	public UserPerson getUser() {
		return user;
	}

	public void setUser(UserPerson user) {
		this.user = user;
	}

	public List<UserPerson> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<UserPerson> usuarios) {
		this.usuarios = usuarios;
	}

	public Email getEmailUser() {
		return emailUser;
	}

	public void setEmailUser(Email emailUser) {
		this.emailUser = emailUser;
	}

	public String getUsuarioPesquisado() {
		return usuarioPesquisado;
	}

	public void setUsuarioPesquisado(String usuarioPesquisado) {
		this.usuarioPesquisado = usuarioPesquisado;
	}

	public PieChartModel getModel() {
		return model;
	}

	public void setModel(PieChartModel model) {
		this.model = model;
	}
}
