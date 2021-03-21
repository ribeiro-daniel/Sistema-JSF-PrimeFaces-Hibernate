package br.com.primefaces.dao;

import java.util.List;

import javax.persistence.EntityManager;

public class GenericDAO<T> {
	private EntityManager em = JPAUtil.getEntityManager();

	public T updateMerge(T entity) {

		em.getTransaction().begin();

		T retorno = em.merge(entity);

		em.getTransaction().commit();
		return retorno;
	}

	public void persistir(T entity) {

		em.getTransaction().begin();

		em.persist(entity);

		em.getTransaction().commit();
	}

	public List<T> list(T entity) {
		em.getTransaction().begin();

		List<T> result = em.createQuery("from " + entity.getClass().getName() + " order by id").getResultList();

		em.getTransaction().commit();
		return result;
	}

	public void delete(T entity) {
		Object id = JPAUtil.getPrimaryKey(entity);
		T object = (T) em.find(entity.getClass(), id);

		em.getTransaction().begin();
		em.remove(object);
		em.getTransaction().commit();
	}
}
