package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.Servico;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.ServicoRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.repository.filter.ServicoFilter;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ServicoPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmpresaRepository empresaRepository;	

	@Inject
	private ServicoRepository servicoRepository;

	@Getter
	private List<Servico> servicos;

	@Getter
	@Setter
	private Servico servicoSelecionado;
	
	@Getter
	private List<Empresa> mantenedoras;

	@Getter
	@Setter
	private ServicoFilter filter;

	public void inicializar() {
		EmpresaFilter filterMantenedora = new EmpresaFilter();
		filterMantenedora.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		mantenedoras = empresaRepository.listAll(filterMantenedora);
		filter = new ServicoFilter();
		pesquisar();
	}
	
	public void pesquisar() {
		servicos = servicoRepository.listAll(filter);
	}

	public void excluir() {
		if (servicoRepository.remover(this.servicoSelecionado)) {
			FacesUtil.addInfoMessage("Servico excluída com sucesso");
			servicos = servicoRepository.listAll(filter);
		}
	}

}
