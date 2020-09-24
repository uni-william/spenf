package br.com.sis.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.Despesa;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.DespesaRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.repository.filter.DespesaFilter;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class DespesaPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmpresaRepository empresaRepository;	

	@Inject
	private DespesaRepository despesaRepository;

	@Getter
	private List<Despesa> despesas;

	@Getter
	@Setter
	private Despesa despesaSelecionado;
	
	@Getter
	private List<Empresa> mantenedoras;

	@Getter
	@Setter
	private DespesaFilter filter;

	public void inicializar() {
		EmpresaFilter filterMantenedora = new EmpresaFilter();
		filterMantenedora.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		mantenedoras = empresaRepository.listAll(filterMantenedora);
		filter = new DespesaFilter();
		if (mantenedoras.size() > 0)
			filter.setMantenedora(mantenedoras.get(0));
		LocalDate date = LocalDate.now();
		filter.setDataIni(date.with(TemporalAdjusters.firstDayOfMonth()));
		filter.setDataFim(date.with(TemporalAdjusters.lastDayOfMonth()));		
		pesquisar();
	}
	
	public void pesquisar() {
		despesas = despesaRepository.listAll(filter);
	}

	public void excluir() {
		if (despesaRepository.remover(this.despesaSelecionado)) {
			FacesUtil.addInfoMessage("Despesa excluída com sucesso");
			despesas = despesaRepository.listAll(filter);
		}
	}

}
