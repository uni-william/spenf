package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.TipoDespesa;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.TipoDespesaRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.repository.filter.TipoDespesaFilter;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class TipoDespesaPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmpresaRepository empresaRepository;	

	@Inject
	private TipoDespesaRepository tipoDespesaRepository;

	@Getter
	private List<TipoDespesa> tipoDespesas;

	@Getter
	@Setter
	private TipoDespesa tipoDespesaSelecionado;
	
	@Getter
	private List<Empresa> mantenedoras;

	@Getter
	@Setter
	private TipoDespesaFilter filter;

	public void inicializar() {
		EmpresaFilter filterMantenedora = new EmpresaFilter();
		filterMantenedora.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		mantenedoras = empresaRepository.listAll(filterMantenedora);
		filter = new TipoDespesaFilter();
		if (mantenedoras.size() > 0)
			filter.setMantenedora(mantenedoras.get(0));
		pesquisar();
	}
	
	public void pesquisar() {
		tipoDespesas = tipoDespesaRepository.listAll(filter);
	}

	public void excluir() {
		if (tipoDespesaRepository.remover(this.tipoDespesaSelecionado)) {
			FacesUtil.addInfoMessage("Registro excluído com sucesso");
			tipoDespesas = tipoDespesaRepository.listAll(filter);
		}
	}

}
