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

import br.com.sis.entity.DespesaMaterial;
import br.com.sis.entity.Empresa;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.DespesaMaterialRepository;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.filter.DespesaFilter;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class DespesaMaterialPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmpresaRepository empresaRepository;	

	@Inject
	private DespesaMaterialRepository despesaMaterialRepository;

	@Getter
	private List<DespesaMaterial> despesaMaterials = new ArrayList<DespesaMaterial>();

	@Getter
	@Setter
	private DespesaMaterial despesaMaterialSelecionado;
	
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
		despesaMaterials = despesaMaterialRepository.listAll(filter);
	}

	public void excluir() {
		if (despesaMaterialRepository.remover(this.despesaMaterialSelecionado)) {
			FacesUtil.addInfoMessage("Registro excluído com sucesso");
			despesaMaterials = despesaMaterialRepository.listAll(filter);
		}
	}
	
	public BigDecimal getTotalItens() {
		BigDecimal total = BigDecimal.ZERO;
		for (DespesaMaterial despesaMaterial : this.despesaMaterials) {
			total = total.add(despesaMaterial.getValor());
		}
		
		return total;
	}	

}
