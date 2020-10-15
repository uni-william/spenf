package br.com.sis.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.dto.SomatorioDepesaPorPeriodo;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.DespesaRepository;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.filter.DespesaFilter;
import br.com.sis.repository.filter.EmpresaFilter;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class SomatorioDespesaPeriodoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private DespesaRepository despesaRepository;

	@Inject
	private EmpresaRepository empresaRepository;

	@Getter
	@Setter
	private DespesaFilter despesaFilter;
	
	@Getter
	private List<SomatorioDepesaPorPeriodo> depesasPorPeriodo = new ArrayList<SomatorioDepesaPorPeriodo>();

	public void inicializar() {
		despesaFilter = new DespesaFilter();
		EmpresaFilter filterMantenedora = new EmpresaFilter();
		filterMantenedora.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		List<Empresa> mantenedoras = empresaRepository.listAll(filterMantenedora);
		if (mantenedoras.size() > 0)
			despesaFilter.setMantenedora(mantenedoras.get(0));
		LocalDate date = LocalDate.now();
		despesaFilter.setDataIni(date.with(TemporalAdjusters.firstDayOfMonth()));
		despesaFilter.setDataFim(date.with(TemporalAdjusters.lastDayOfMonth()));
		consultar();
	}

	public void consultar() {
		depesasPorPeriodo = despesaRepository.despesaPorPeriodo(despesaFilter);
	}
}
