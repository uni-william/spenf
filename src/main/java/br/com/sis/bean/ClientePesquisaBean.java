package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ClientePesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmpresaRepository empresaRepository;

	@Getter
	private List<Empresa> empresas;

	@Getter
	@Setter
	private Empresa empresaSelecionada;

	@Getter
	private List<Empresa> mantenedoras;

	@Getter
	@Setter
	private EmpresaFilter filter;

	public void inicializar() {
		EmpresaFilter filterMantenedora = new EmpresaFilter();
		filterMantenedora.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		mantenedoras = empresaRepository.listAll(filterMantenedora);
		filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.CLIENTE);
		if (mantenedoras.size() > 0)
			filter.setMantenedora(mantenedoras.get(0));
		pesquisar();
	}
	
	public void pesquisar() {
		empresas = empresaRepository.listAll(filter);
	}

	public void excluir() {
		if (empresaRepository.remover(this.empresaSelecionada)) {
			FacesUtil.addInfoMessage("Empresa excluída com sucesso");
			empresas = empresaRepository.listAll(filter);
		}
	}

}
