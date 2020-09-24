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
	private LocalDate dataIni;
	@Getter
	@Setter	
	private LocalDate dataFim;
	
	@Getter
	@Setter
	private int tipoData = 1;

	@Getter
	@Setter
	private OrcamentoFilter filter;

	public void inicializar() {
		EmpresaFilter filterMantenedora = new EmpresaFilter();
		filterMantenedora.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		mantenedoras = empresaRepository.listAll(filterMantenedora);
		LocalDate date = LocalDate.now();
		dataIni = date.with(TemporalAdjusters.firstDayOfMonth());
		dataFim = date.with(TemporalAdjusters.lastDayOfMonth());
		filter = new OrcamentoFilter();
		if (mantenedoras.size() > 0) {
			filter.setMantenedora(mantenedoras.get(0));
			aoSelelecionarMantenedora();
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
		filter.setDataPedidoIni(null);
		filter.setDataPedidoFim(null);
		filter.setDataNotaIni(null);
		filter.setDataNotaFim(null);
		filter.setDataPrevisaoPagamentoIni(null);
		filter.setDataPrevisaoPagamentoFim(null);
		filter.setDataPagamentoIni(null);
		filter.setDataPagamentoFim(null);
		String ordem = null;
		if (tipoData == 1) {
			filter.setDataPedidoIni(this.getDataIni());
			filter.setDataPedidoFim(this.getDataFim());
			ordem = "dataRecebimentoPedido";
		} else if (tipoData == 2) {
			filter.setDataNotaIni(this.getDataIni());
			filter.setDataNotaFim(this.getDataFim());
			ordem = "dataEmissaoNota";
		} else if (tipoData == 3) {
			filter.setDataPrevisaoPagamentoIni(this.getDataIni());
			filter.setDataPrevisaoPagamentoFim(this.getDataFim());
			ordem = "dataPrevisaoPagamento";
		} else if (tipoData == 4) {
			filter.setDataPagamentoIni(this.getDataIni());
			filter.setDataPagamentoFim(this.getDataFim());
			ordem = "dataEfetivaPagamento";
		}
		
		filter.setSomenteComPedido(true);		
		orcamentos = orcamentoRepository.listAll(filter, ordem);
	}

}
