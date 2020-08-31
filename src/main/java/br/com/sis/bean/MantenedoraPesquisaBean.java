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
public class MantenedoraPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmpresaRepository empresaRepository;

	private List<Empresa> empresas;

	private Empresa empresaSelecionada;
	
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

	public void inicializar() {
		filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		empresas = empresaRepository.listMantenedorasAll(filter);
	}
	
	public void excluir() {
		if (empresaRepository.remover(this.empresaSelecionada)) {
			FacesUtil.addInfoMessage("Empresa excluída com sucesso");
			empresas = empresaRepository.listMantenedorasAll(filter);
		}
	}

}
