package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.Orcamento;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.service.OrcamentoService;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class OrcamentoCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Orcamento orcamento;

	@Inject
	private OrcamentoService orcamentoService;
	
	@Inject
	private EmpresaRepository empresaRepository;	
	
	@Getter
	private List<Empresa> mantenedoras;
	
	@Getter
	private List<Empresa> clientes;	
	

	public void inicializar() {
		EmpresaFilter filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		this.mantenedoras = empresaRepository.listAll(filter);
		if (orcamento == null) {
			orcamento = new Orcamento();		
		}
		aoSelelecionarMantenedora();
	}

	public void salvar() {
		orcamento = orcamentoService.salvar(orcamento);
		FacesUtil.addInfoMessage("Orçamento salvo com sucesso!");
	}
	
	public void aoSelelecionarMantenedora() {
		EmpresaFilter filterCliente = new EmpresaFilter();
		filterCliente.setTipoEmpresa(TipoEmpresa.CLIENTE);
		filterCliente.setMantenedora(orcamento.getMantenedora());
		clientes = empresaRepository.listAll(filterCliente);
	}	

	public boolean isEditando() {
		return this.orcamento.getId() != null;
	}

}
