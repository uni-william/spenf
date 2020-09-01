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
import br.com.sis.repository.OrcamentoRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.repository.filter.OrcamentoFilter;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class OrcamentoPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmpresaRepository empresaRepository;	

	@Inject
	private OrcamentoRepository orcamentoRepository;

	@Getter
	private List<Orcamento> orcamentos;

	@Getter
	@Setter
	private Orcamento orcamentoSelecionado;
	
	@Getter
	private List<Empresa> mantenedoras;
	
	@Getter
	private List<Empresa> clientes;
	

	@Getter
	@Setter
	private OrcamentoFilter filter;

	public void inicializar() {
		EmpresaFilter filterMantenedora = new EmpresaFilter();
		filterMantenedora.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		mantenedoras = empresaRepository.listAll(filterMantenedora);
		filter = new OrcamentoFilter();
		aoSelelecionarMantenedora();
		pesquisar();
	}
	
	public void aoSelelecionarMantenedora() {
		EmpresaFilter filterCliente = new EmpresaFilter();
		filterCliente.setTipoEmpresa(TipoEmpresa.CLIENTE);
		filterCliente.setMantenedora(filter.getMantenedora());
		clientes = empresaRepository.listAll(filterCliente);
	}
	
	public void pesquisar() {
		orcamentos = orcamentoRepository.listAll(filter);
	}

}
