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

import br.com.sis.entity.TipoDespesa;
import br.com.sis.repository.filter.TipoDespesaFilter;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class TipoDespesaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public TipoDespesa findById(Long id) {
		return manager.find(TipoDespesa.class, id);
	}
	
	public List<TipoDespesa> listAll(TipoDespesaFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<TipoDespesa> criteriaQuery = builder.createQuery(TipoDespesa.class);
		Root<TipoDespesa> root = criteriaQuery.from(TipoDespesa.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criarRestricoes(filter, builder, root));
		TypedQuery<TipoDespesa> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public TipoDespesa salvar(TipoDespesa tipoDespesa) {
		return manager.merge(tipoDespesa);
	}

	@Transactional
	public boolean remover(TipoDespesa tipoDespesa) {
		try {
			tipoDespesa = findById(tipoDespesa.getId());
			manager.remove(tipoDespesa);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("Registro não pode ser excluído.");
			return false;
		}
	}	
	
	public TipoDespesa findByDescricao(String descricao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<TipoDespesa> criteriaQuery = builder.createQuery(TipoDespesa.class);
		Root<TipoDespesa> root = criteriaQuery.from(TipoDespesa.class);
		criteriaQuery.select(root);
		criteriaQuery.where(builder.equal(builder.lower(root.get("descricao")), descricao.toLowerCase()));
		TypedQuery<TipoDespesa> query = manager.createQuery(criteriaQuery);
		try {
			return query.getSingleResult();
		} catch(NoResultException e) {
			return null;
		}
	}
	
	private Predicate[] criarRestricoes(TipoDespesaFilter filter, CriteriaBuilder builder, Root<TipoDespesa> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(filter.getDescricao()))
			predicates.add(builder.like(builder.lower(root.get("descricao")),
					"%" + filter.getDescricao().toLowerCase() + "%"));

		if (filter.getMantenedora() != null)
			predicates.add(builder.equal(root.get("mantenedora"), filter.getMantenedora()));

		return predicates.toArray(new Predicate[predicates.size()]);
	}	

}
