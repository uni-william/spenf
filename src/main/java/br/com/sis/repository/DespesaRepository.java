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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Despesa;
import br.com.sis.entity.TipoDespesa;
import br.com.sis.entity.dto.SomatorioDepesaPorPeriodo;
import br.com.sis.repository.filter.DespesaFilter;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class DespesaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Despesa findById(Long id) {
		return manager.find(Despesa.class, id);
	}

	public List<Despesa> listAll(DespesaFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Despesa> criteriaQuery = builder.createQuery(Despesa.class);
		Root<Despesa> root = criteriaQuery.from(Despesa.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criarRestricoes(filter, builder, root));
		TypedQuery<Despesa> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public Despesa salvar(Despesa despesa) {
		return manager.merge(despesa);
	}

	@Transactional
	public boolean remover(Despesa despesa) {
		try {
			despesa = findById(despesa.getId());
			manager.remove(despesa);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("Despesa não pode ser excluída.");
			return false;
		}
	}

	public List<SomatorioDepesaPorPeriodo> despesaPorPeriodo(DespesaFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<SomatorioDepesaPorPeriodo> criteriaQuery = builder.createQuery(SomatorioDepesaPorPeriodo.class);
		Root<Despesa> root = criteriaQuery.from(Despesa.class);
		Join<Despesa, TipoDespesa> tipoJoin = root.join("tipoDespesa", JoinType.INNER);
		criteriaQuery.select(builder.construct(SomatorioDepesaPorPeriodo.class, tipoJoin.get("descricao"),
				builder.sum(root.get("valor")), builder.count(root.get("id"))));
		criteriaQuery.where(criarRestricoes(filter, builder, root));
		criteriaQuery.groupBy(tipoJoin.get("descricao"));
		TypedQuery<SomatorioDepesaPorPeriodo> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	private Predicate[] criarRestricoes(DespesaFilter filter, CriteriaBuilder builder, Root<Despesa> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(filter.getDescricao()))
			predicates.add(builder.like(builder.lower(root.get("descricao")),
					"%" + filter.getDescricao().toLowerCase() + "%"));

		if (filter.getDataIni() != null)
			predicates.add(builder.greaterThanOrEqualTo(root.get("data"), filter.getDataIni()));

		if (filter.getDataFim() != null)
			predicates.add(builder.lessThanOrEqualTo(root.get("data"), filter.getDataFim()));
		
		if (filter.getDataPagtoIni() != null)
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataPagamento"), filter.getDataPagtoIni()));

		if (filter.getDataPagtoFim() != null)
			predicates.add(builder.lessThanOrEqualTo(root.get("dataPagamento"), filter.getDataPagtoFim()));		

		if (filter.getMantenedora() != null)
			predicates.add(builder.equal(root.get("mantenedora"), filter.getMantenedora()));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
