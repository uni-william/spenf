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

import br.com.sis.entity.Orcamento;
import br.com.sis.repository.filter.OrcamentoFilter;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class OrcamentoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Orcamento findById(Long id) {
		return manager.find(Orcamento.class, id);
	}

	public List<Orcamento> listAll(OrcamentoFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Orcamento> criteriaQuery = builder.createQuery(Orcamento.class);
		Root<Orcamento> root = criteriaQuery.from(Orcamento.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criarRestricoes(filter, builder, root));
		TypedQuery<Orcamento> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public Orcamento salvar(Orcamento orcamento) {
		return manager.merge(orcamento);
	}

	@Transactional
	public boolean remover(Orcamento orcamento) {
		try {
			orcamento = findById(orcamento.getId());
			manager.remove(orcamento);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("Orçamento não pode ser excluído.");
			return false;
		}
	}

	private Predicate[] criarRestricoes(OrcamentoFilter filter, CriteriaBuilder builder, Root<Orcamento> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (filter.getMantenedora() != null)
			predicates.add(builder.equal(root.get("mantenedora"), filter.getMantenedora()));

		if (filter.getCliente() != null)
			predicates.add(builder.equal(root.get("cliente"), filter.getCliente()));
		
		if (filter.getDataCricaoInicio() != null)
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataOrcamento"), filter.getDataCricaoInicio()));
		
		if (filter.getDataCricaoInicio() != null)
			predicates.add(builder.lessThanOrEqualTo(root.get("dataOrcamento"), filter.getDataCricaoFim()));
		
		if (filter.getDataPrevisaoPagamentoIni() != null)
			predicates.add(builder.greaterThanOrEqualTo(root.get("prazoPagamento"), filter.getDataPrevisaoPagamentoIni()));
		
		if (filter.getDataPrevisaoPagamentoFim() != null)
			predicates.add(builder.lessThanOrEqualTo(root.get("prazoPagamento"), filter.getDataPrevisaoPagamentoFim()));		
		
		if (filter.isSomenteComPedido())
			predicates.add(builder.isNotNull(root.get("pedidoCliente")));
		
		if (filter.isEmAbertos())
			predicates.add(builder.isNull(root.get("diaPagamento")));
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
