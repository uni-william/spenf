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

@Named
@ViewScoped
public class ClientePesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmpresaRepository empresaRepository;

	private List<Empresa> empresas;

	private Empresa empresaSelecionada;

	private List<Empresa> mantenedoras;

	private EmpresaFilter filter;

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}

	public List<Empresa> getMantenedoras() {
		return mantenedoras;
	}

	public EmpresaFilter getFilter() {
		return filter;
	}

	public void setFilter(EmpresaFilter filter) {
		this.filter = filter;
	}

	public void inicializar() {
		EmpresaFilter filterMantenedora = new EmpresaFilter();
		filterMantenedora.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		mantenedoras = empresaRepository.listAll(filterMantenedora);
		filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.CLIENTE);
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
