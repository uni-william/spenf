package br.com.sis.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class PedidoPesquisaBean implements Serializable {

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
		LocalDate date = LocalDate.now();
		if (mantenedoras.size() > 0) {
			filter.setMantenedora(mantenedoras.get(0));
			aoSelelecionarMantenedora();
			filter.setDataPedidoIni(date.with(TemporalAdjusters.firstDayOfMonth()));
			filter.setDataPedidoFim(date.with(TemporalAdjusters.lastDayOfMonth()));
			filter.setSomenteComPedido(true);
			pesquisar();
		} else {
			FacesUtil.addWarnMessage("Não existe Mantenedora cadastrada. Verifique!");
		}
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
