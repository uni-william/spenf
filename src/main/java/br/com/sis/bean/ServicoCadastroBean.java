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
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.service.ServicoService;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ServicoCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Servico servico;

	@Inject
	private ServicoService servicoService;
	
	@Inject
	private EmpresaRepository empresaRepository;	
	
	@Getter
	private List<Empresa> mantenedoras;
	

	public void inicializar() {
		EmpresaFilter filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		this.mantenedoras = empresaRepository.listAll(filter);
		
		if (servico == null) {
			servico = new Servico();		
		}
	}

	public void salvar() {
		servico = servicoService.salvar(servico);
		FacesUtil.addInfoMessage("Serviço salva com sucesso!");
	}

	public boolean isEditando() {
		return this.servico.getId() != null;
	}

}
