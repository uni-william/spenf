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

import br.com.sis.entity.Empresa;
import br.com.sis.entity.Orcamento;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.OrcamentoRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.repository.filter.OrcamentoFilter;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class PagamentosPrevistosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private OrcamentoRepository orcamentoRepository;
	
	@Inject
	private EmpresaRepository empresaRepository;
	
	private Empresa mantenedora;
	
	@Getter
	@Setter
	private LocalDate dataInicio;

	@Getter
	@Setter
	private LocalDate dataFim;	
	
	@Getter
	private List<Orcamento> orcamentos = new  ArrayList<Orcamento>();
	
	public void inicializar() {
		EmpresaFilter filterMantenedora = new EmpresaFilter();
		filterMantenedora.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		List<Empresa> mantenedoras = empresaRepository.listAll(filterMantenedora);
		if (mantenedoras.size() > 0)
			mantenedora = mantenedoras.get(0);
		LocalDate date = LocalDate.now();
		dataInicio = date.with(TemporalAdjusters.firstDayOfMonth());
		dataFim = date.with(TemporalAdjusters.lastDayOfMonth());
		consultar();
	}
	
	public void consultar() {
		OrcamentoFilter filter = new OrcamentoFilter();
		filter.setMantenedora(mantenedora);
		filter.setDataPrevisaoPagamentoIni(dataInicio);
		filter.setDataPrevisaoPagamentoFim(dataFim);
		filter.setSomenteComPedido(true);
		filter.setSomenteComNota(true);
		orcamentos = orcamentoRepository.listAll(filter, "dataPrevisaoPagamento");
	}
	
	public BigDecimal getTotalItens() {
		BigDecimal total = BigDecimal.ZERO;
		for (Orcamento orcamento : this.orcamentos) {
			total = total.add(orcamento.getValorOrcamento());
		}	
		return total;
	}		

}
