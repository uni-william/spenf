package br.com.sis.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.dto.ResumoPorPeriodo;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.OrcamentoRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ResumoPeriodoBean implements Serializable {

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
	private ResumoPorPeriodo totalOrcamentos;
	@Getter
	private ResumoPorPeriodo totalPedidos;
	@Getter
	private ResumoPorPeriodo totalNotas;
	@Getter
	private ResumoPorPeriodo totalPagamentos;

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
		totalOrcamentos = orcamentoRepository.somatorioTransacoes(mantenedora, dataInicio, dataFim, 1);
		totalOrcamentos.setTotal(totalOrcamentos.getTotal() != null ? totalOrcamentos.getTotal() : BigDecimal.ZERO);
		totalPedidos = orcamentoRepository.somatorioTransacoes(mantenedora, dataInicio, dataFim, 2);
		totalPedidos.setTotal(totalPedidos.getTotal() != null ? totalPedidos.getTotal() : BigDecimal.ZERO);
		totalNotas = orcamentoRepository.somatorioTransacoes(mantenedora, dataInicio, dataFim, 3);
		totalNotas.setTotal(totalNotas.getTotal() != null ? totalNotas.getTotal() : BigDecimal.ZERO);
		totalPagamentos = orcamentoRepository.somatorioTransacoes(mantenedora, dataInicio, dataFim, 4);
		totalPagamentos.setTotal(totalPagamentos.getTotal() != null ? totalPagamentos.getTotal() : BigDecimal.ZERO);
	}
}
