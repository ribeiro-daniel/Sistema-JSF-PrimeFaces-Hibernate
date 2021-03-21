package br.com.primefaces.repository;

import java.util.List;

import br.com.primefaces.model.UserPerson;

public interface DaoRepository {
	public UserPerson validaLogin(String login, String senha);
	public UserPerson pesquisar(Long id, Class<UserPerson> classe);
	public Long validaLoginDuplicado(String login);
	public List<UserPerson> pesquisarUsuario(String nome);
}
