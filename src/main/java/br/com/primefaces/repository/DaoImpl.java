package br.com.primefaces.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.primefaces.dao.JPAUtil;
import br.com.primefaces.model.UserPerson;

public class DaoImpl implements DaoRepository {
	private EntityManager em = JPAUtil.getEntityManager();

	@Override
	public UserPerson validaLogin(String login, String senha) {
		UserPerson usuario = null;

		em.getTransaction().begin();
		usuario = (UserPerson) em
				.createQuery("select p from UserPerson p where p.login = '" + login + "' and p.senha = '" + senha + "'")
				.getSingleResult();

		em.getTransaction().commit();
		return usuario;
	}
	@Override
	public UserPerson pesquisar(Long id, Class<UserPerson> classe) {
		//Limpando objeto da memória e recarregando a tabela
		em.clear();
		UserPerson userPerson =  (UserPerson) em.createQuery("from " + classe.getName() + " where id = '" + id +"'").getSingleResult();
		return userPerson;
	}
	@Override
	public Long validaLoginDuplicado(String login) {
		em.getTransaction().begin();
		Long result = (Long) em.createQuery("select count(login) from UserPerson where login = '" + login + "'")
					.getSingleResult();
		em.getTransaction().commit();

		return result;
	}
	@Override
	public List<UserPerson> pesquisarUsuario(String nome) {
		Query query = em.createQuery("FROM UserPerson WHERE LOWER(nome) LIKE LOWER('%"+ nome +"%')");
		
		return query.getResultList();
	}
}
