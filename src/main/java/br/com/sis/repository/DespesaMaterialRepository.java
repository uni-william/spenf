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

import br.com.sis.entity.DespesaMaterial;
import br.com.sis.repository.filter.DespesaFilter;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class DespesaMaterialRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public DespesaMaterial findById(Long id) {
		return manager.find(DespesaMaterial.class, id);
	}

	public List<DespesaMaterial> listAll(DespesaFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<DespesaMaterial> criteriaQuery = builder.createQuery(DespesaMaterial.class);
		Root<DespesaMaterial> root = criteriaQuery.from(DespesaMaterial.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criarRestricoes(filter, builder, root));
		TypedQuery<DespesaMaterial> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public DespesaMaterial salvar(DespesaMaterial despesaMaterial) {
		return manager.merge(despesaMaterial);
	}

	@Transactional
	public boolean remover(DespesaMaterial despesaMaterial) {
		try {
			despesaMaterial = findById(despesaMaterial.getId());
			manager.remove(despesaMaterial);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("Resgistro não pode ser excluído.");
			return false;
		}
	}

	private Predicate[] criarRestricoes(DespesaFilter filter, CriteriaBuilder builder, Root<DespesaMaterial> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(filter.getDescricao()))
			predicates.add(builder.like(builder.lower(root.get("descricao")),
					"%" + filter.getDescricao().toLowerCase() + "%"));

		if (filter.getDataIni() != null)
			predicates.add(builder.greaterThanOrEqualTo(root.get("data"), filter.getDataIni()));

		if (filter.getDataFim() != null)
			predicates.add(builder.lessThanOrEqualTo(root.get("data"), filter.getDataFim()));

		if (filter.getMantenedora() != null)
			predicates.add(builder.equal(root.get("mantenedora"), filter.getMantenedora()));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
