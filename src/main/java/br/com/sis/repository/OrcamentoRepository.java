package br.com.sis.repository;

import java.io.Serializable;
import java.time.LocalDate;
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
import br.com.sis.entity.Orcamento;
import br.com.sis.entity.dto.ResumoPorPeriodo;
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

	public List<Orcamento> listAll(OrcamentoFilter filter, String ordem) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Orcamento> criteriaQuery = builder.createQuery(Orcamento.class);
		Root<Orcamento> root = criteriaQuery.from(Orcamento.class);
		root.fetch("mantenedora");
		root.fetch("cliente");
		criteriaQuery.select(root);
		criteriaQuery.where(criarRestricoes(filter, builder, root));
		if (ordem != null)
			criteriaQuery.orderBy(builder.asc(root.get(ordem)));
		TypedQuery<Orcamento> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public Orcamento salvar(Orcamento orcamento) {
		return manager.merge(orcamento);
	}
	
	public ResumoPorPeriodo somatorioTransacoes(Empresa mantenedora, LocalDate dataInicio, LocalDate dataFim, long tipo) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoPorPeriodo> criteriaQuery = builder.createQuery(ResumoPorPeriodo.class);
		Root<Orcamento> root = criteriaQuery.from(Orcamento.class);
		criteriaQuery.select(builder.construct(ResumoPorPeriodo.class,
				builder.sum(root.get("valorOrcamento")),
				builder.count(root.get("id"))));
		
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(root.get("mantenedora"), mantenedora));
		/*
		 tipo = 1 - Orçamentos gerados (campo dataOrcamento)
		 tipo = 2 - Pedidos gerados (campo dataRecebimentoPedido)
		 tipo = 3 - Pedidos com nota (campo dataEmissaoNota)
		 tipo = 4 - Previsão de pagamento (campo dataPrevisaoPagamento)
		 tipo = 5 - Total de pedidos recebidos no periodo (campo dataEfetivaPagamento)
		 */
		if (tipo == 1)
			predicates.add(builder.between(root.get("dataOrcamento"), dataInicio, dataFim));
		else if (tipo == 2)
			predicates.add(builder.between(root.get("dataRecebimentoPedido"), dataInicio, dataFim));
		else if (tipo == 3)
			predicates.add(builder.between(root.get("dataEmissaoNota"), dataInicio, dataFim));
		else if (tipo == 4)
			predicates.add(builder.between(root.get("dataPrevisaoPagamento"), dataInicio, dataFim));		
		else
			predicates.add(builder.between(root.get("dataEfetivaPagamento"), dataInicio, dataFim));
		
		criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
		TypedQuery<ResumoPorPeriodo> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
		
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
		
		if (filter.getNumeroOrcamento() != null)
			predicates.add(builder.equal(root.get("id"), filter.getNumeroOrcamento()));

		if (filter.getMantenedora() != null)
			predicates.add(builder.equal(root.get("mantenedora"), filter.getMantenedora()));

		if (filter.getCliente() != null)
			predicates.add(builder.equal(root.get("cliente"), filter.getCliente()));
		
		if (filter.getDataCricaoIni() != null)
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataOrcamento"), filter.getDataCricaoIni()));
		
		if (filter.getDataCricaoFim() != null)
			predicates.add(builder.lessThanOrEqualTo(root.get("dataOrcamento"), filter.getDataCricaoFim()));
		
		if (filter.getDataPedidoIni() != null)
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataRecebimentoPedido"), filter.getDataPedidoIni()));
		
		if (filter.getDataPedidoFim() != null)
			predicates.add(builder.lessThanOrEqualTo(root.get("dataRecebimentoPedido"), filter.getDataPedidoFim()));
		
		if (filter.getDataNotaIni() != null)
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataEmissaoNota"), filter.getDataNotaIni()));
		
		if (filter.getDataNotaFim() != null)
			predicates.add(builder.lessThanOrEqualTo(root.get("dataEmissaoNota"), filter.getDataNotaFim()));
		
		if (filter.getDataPrevisaoPagamentoIni() != null)
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataPrevisaoPagamento"), filter.getDataPrevisaoPagamentoIni()));
		
		if (filter.getDataPrevisaoPagamentoFim() != null)
			predicates.add(builder.lessThanOrEqualTo(root.get("dataPrevisaoPagamento"), filter.getDataPrevisaoPagamentoFim()));
		
		if (filter.getDataPagamentoIni() != null)
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataEfetivaPagamento"), filter.getDataPagamentoIni()));
		
		if (filter.getDataPagamentoFim() != null)
			predicates.add(builder.lessThanOrEqualTo(root.get("dataEfetivaPagamento"), filter.getDataPagamentoFim()));		
		
		if (filter.isSomenteComPedido())
			predicates.add(builder.isNotNull(root.get("pedidoCliente")));
		else
			predicates.add(builder.isNull(root.get("pedidoCliente")));
		
		if (filter.isEmAbertos())
			predicates.add(builder.isNull(root.get("dataEfetivaPagamento")));
		
		if (!StringUtils.isEmpty(filter.getPedido()))
			predicates.add(builder.equal(root.get("pedidoCliente"), filter.getPedido()));
		
		if (filter.isSomenteComNota())
			predicates.add(builder.isNotNull(root.get("numeroNfse")));
		
		if (filter.getNumeroNota() != null)
			predicates.add(builder.equal(root.get("numeroNfse"), filter.getNumeroNota()));
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
