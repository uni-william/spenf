package br.com.sis.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Colaborador;
import br.com.sis.repository.filter.ColaboradorFilter;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class ColaboradorRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Colaborador findById(Long id) {
		return manager.find(Colaborador.class, id);
	}

	public List<Colaborador> listAll(ColaboradorFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Colaborador> criteriaQuery = builder.createQuery(Colaborador.class);
		Root<Colaborador> root = criteriaQuery.from(Colaborador.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criarRestricoes(filter, builder, root));
		TypedQuery<Colaborador> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public Colaborador salvar(Colaborador colaborador) {
		return manager.merge(colaborador);
	}

	@Transactional
	public boolean remover(Colaborador colaborador) {
		try {
			colaborador = findById(colaborador.getId());
			manager.remove(colaborador);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("Colaborador não pode ser excluído.");
			return false;
		}
	}

	public Colaborador findByCpf(String cpf) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Colaborador> criteriaQuery = builder.createQuery(Colaborador.class);
		List<Predicate> predicates = new ArrayList<>();
		Root<Colaborador> root = criteriaQuery.from(Colaborador.class);
		predicates.add(builder.equal(root.get("cpf"), cpf));
		criteriaQuery.select(root);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Colaborador> query = manager.createQuery(criteriaQuery);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Colaborador findByEmail(String email) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Colaborador> criteriaQuery = builder.createQuery(Colaborador.class);
		List<Predicate> predicates = new ArrayList<>();
		Root<Colaborador> root = criteriaQuery.from(Colaborador.class);
		predicates.add(builder.equal(root.get("email"), email));
		criteriaQuery.select(root);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Colaborador> query = manager.createQuery(criteriaQuery);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}	

	private Predicate[] criarRestricoes(ColaboradorFilter filter, CriteriaBuilder builder, Root<Colaborador> root) {
		List<Predicate> predicates = new ArrayList<>();
		if (!StringUtils.isEmpty(filter.getNome()))
			predicates.add(builder.like(builder.lower(root.get("nome")),
					"%" + filter.getNome().toLowerCase() + "%"));
		if (!StringUtils.isEmpty(filter.getCpf()))
			predicates.add(builder.equal(root.get("cpf"), filter.getCpf()));
		if (filter.getMantenedora() != null)
			predicates.add(builder.equal(root.get("mantenedora"), filter.getMantenedora()));
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
