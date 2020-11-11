package br.com.sis.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Despesa;
import br.com.sis.entity.Empresa;
import br.com.sis.entity.Orcamento;
import br.com.sis.entity.dto.FluxoDeCaixa;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.DespesaRepository;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.OrcamentoRepository;
import br.com.sis.repository.filter.DespesaFilter;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.repository.filter.OrcamentoFilter;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class FluxoDeCaixaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private OrcamentoRepository orcamentoRepository;
	
	@Inject
	private DespesaRepository despesaRepository;

	@Inject
	private EmpresaRepository empresaRepository;

	private Empresa mantenedora;
	
	@Getter
	private List<FluxoDeCaixa> fluxoDeCaixaLista = new ArrayList<FluxoDeCaixa>();  

	@Getter
	@Setter
	private LocalDate dataIni;

	@Getter
	@Setter
	private LocalDate dataFim;
	
	@Getter
	private BigDecimal totalReceita;
	@Getter
	private BigDecimal totalDespesa;

	public void inicializar() {
		EmpresaFilter filterMantenedora = new EmpresaFilter();
		filterMantenedora.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		List<Empresa> mantenedoras = empresaRepository.listAll(filterMantenedora);
		if (mantenedoras.size() > 0)
			mantenedora = mantenedoras.get(0);
		LocalDate date = LocalDate.now();
		dataIni = date.with(TemporalAdjusters.firstDayOfMonth());
		dataFim = date.with(TemporalAdjusters.lastDayOfMonth());
		consultar();
	}

	public void consultar() {
		totalDespesa = BigDecimal.ZERO;
		totalReceita = BigDecimal.ZERO;
		OrcamentoFilter orcamentoFilter = new OrcamentoFilter();
		orcamentoFilter.setMantenedora(mantenedora);
		orcamentoFilter.setSomenteComNota(true);
		orcamentoFilter.setSomenteComPedido(true);
		orcamentoFilter.setDataPagamentoIni(dataIni);
		orcamentoFilter.setDataPagamentoFim(dataFim);
		
		DespesaFilter despesaFilter = new DespesaFilter();
		despesaFilter.setMantenedora(mantenedora);
		despesaFilter.setDataPagtoIni(dataIni);
		despesaFilter.setDataPagtoFim(dataFim);
		List<Orcamento> orcamentos = orcamentoRepository.listAll(orcamentoFilter, "dataEfetivaPagamento");
		List<Despesa> despesas = despesaRepository.listAll(despesaFilter);
		fluxoDeCaixaLista.clear();
		orcamentos.forEach(orcamento -> {
			FluxoDeCaixa fluxoDeCaixa = new FluxoDeCaixa();
			fluxoDeCaixa.setData(orcamento.getDataEfetivaPagamento());
			fluxoDeCaixa.setValor(orcamento.getTotalComDesconto());
			fluxoDeCaixa.setTipo("R");
			fluxoDeCaixa.setDescricao(orcamento.getCliente().getNomeFantasia() + " - Pedido - " + orcamento.getPedidoCliente());
			totalReceita = totalReceita.add(orcamento.getTotalComDesconto());
			fluxoDeCaixaLista.add(fluxoDeCaixa);
		});
		despesas.forEach(despesa -> {
			FluxoDeCaixa fluxoDeCaixa = new FluxoDeCaixa();
			fluxoDeCaixa.setData(despesa.getData());
			fluxoDeCaixa.setValor(despesa.getValor());
			fluxoDeCaixa.setTipo("D");
			fluxoDeCaixa.setDescricao(despesa.getDescricao());
			totalDespesa = totalDespesa.add(despesa.getValor());
			fluxoDeCaixaLista.add(fluxoDeCaixa);			
		});
	}
}
