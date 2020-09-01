package br.com.sis.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.Servico;
import br.com.sis.repository.filter.ServicoFilter;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class ServicoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Servico findById(Long id) {
		return manager.find(Servico.class, id);
	}

	public List<Servico> listAll(ServicoFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Servico> criteriaQuery = builder.createQuery(Servico.class);
		Root<Servico> root = criteriaQuery.from(Servico.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criarRestricoes(filter, builder, root));
		TypedQuery<Servico> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public List<Servico> toAutoComplete(Empresa mantenedora, String descricao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Servico> criteriaQuery = builder.createQuery(Servico.class);
		Root<Servico> root = criteriaQuery.from(Servico.class);
		criteriaQuery.select(root);
		criteriaQuery.where(builder.equal(root.get("mantenedora"), mantenedora),
				builder.like(builder.lower(root.get("descricao")), descricao.toLowerCase() + "%"));
		TypedQuery<Servico> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	

	public Servico salvar(Servico servico) {
		return manager.merge(servico);
	}

	@Transactional
	public boolean remover(Servico servico) {
		try {
			servico = findById(servico.getId());
			manager.remove(servico);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("Serviço não pode ser excluído.");
			return false;
		}
	}

	private Predicate[] criarRestricoes(ServicoFilter filter, CriteriaBuilder builder, Root<Servico> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(filter.getDescricao()))
			predicates.add(builder.like(builder.lower(root.get("descricao")),
					"%" + filter.getDescricao().toLowerCase() + "%"));

		if (filter.getMantenedora() != null)
			predicates.add(builder.equal(root.get("mantenedora"), filter.getMantenedora()));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
