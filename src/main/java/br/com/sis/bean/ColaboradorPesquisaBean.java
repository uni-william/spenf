package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.Colaborador;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.ColaboradorRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.repository.filter.ColaboradorFilter;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ColaboradorPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmpresaRepository empresaRepository;	

	@Inject
	private ColaboradorRepository colaboradorRepository;

	@Getter
	private List<Colaborador> colaboradores;

	@Getter
	@Setter
	private Colaborador colaboradorSelecionado;
	
	@Getter
	private List<Empresa> mantenedoras;

	@Getter
	@Setter
	private ColaboradorFilter filter;

	public void inicializar() {
		EmpresaFilter filterMantenedora = new EmpresaFilter();
		filterMantenedora.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		mantenedoras = empresaRepository.listAll(filterMantenedora);
		filter = new ColaboradorFilter();
		if (mantenedoras.size() > 0)
			filter.setMantenedora(mantenedoras.get(0));
	}
	
	public void pesquisar() {
		colaboradores = colaboradorRepository.listAll(filter);
	}

	public void excluir() {
		if (colaboradorRepository.remover(this.colaboradorSelecionado)) {
			FacesUtil.addInfoMessage("Colaborador excluído com sucesso");
			colaboradores = colaboradorRepository.listAll(filter);
		}
	}

}
