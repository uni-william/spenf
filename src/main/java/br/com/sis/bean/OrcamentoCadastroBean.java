package br.com.sis.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.primefaces.event.SelectEvent;

import br.com.sis.convert.ItemOrcamentoConverter;
import br.com.sis.entity.Empresa;
import br.com.sis.entity.ItemOrcamento;
import br.com.sis.entity.Orcamento;
import br.com.sis.entity.Servico;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.report.ExecutorRelatorio;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.OrcamentoRepository;
import br.com.sis.repository.ServicoRepository;
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
	private OrcamentoRepository orcamentoRepository;

	@Inject
	private EmpresaRepository empresaRepository;

	@Inject
	private ServicoRepository servicoRepository;
	@Getter
	private ItemOrcamentoConverter itemOrcamentoConverter;

	@Getter
	private List<Empresa> mantenedoras;

	@Getter
	private List<Empresa> clientes;

	@Getter
	@Setter
	private ItemOrcamento itemOrcamento = new ItemOrcamento();

	@Getter
	@Setter
	private List<ItemOrcamento> itensOrcamento = new ArrayList<ItemOrcamento>();


	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;

	public void inicializar() {
		EmpresaFilter filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		this.mantenedoras = empresaRepository.listAll(filter);
		if (orcamento == null) {
			novoOrcamento();
			aoSelelecionarMantenedora();
		} else {
			aoSelelecionarMantenedora();
			this.itensOrcamento = orcamento.getItensOrcamento();
		}

	}

	private void novoOrcamento() {
		orcamento = new Orcamento();
		orcamento.setMantenedora(mantenedoras.get(0));
		orcamento.setDataOrcamento(LocalDate.now());
	}

	public List<Servico> completeServico(String descricao) {
		List<Servico> lista = servicoRepository.toAutoComplete(orcamento.getMantenedora(), descricao);
		return lista;
	}

	public void salvar() {
		orcamento.setItensOrcamento(this.itensOrcamento);
		orcamento.setValorOrcamento(this.getTotalItens());
		if (StringUtils.isEmpty(orcamento.getPedidoCliente()))
			orcamento.setPedidoCliente(null);
		orcamento = orcamentoService.salvar(orcamento);
		this.itensOrcamento = orcamento.getItensOrcamento();
		FacesUtil.addInfoMessage("Orçamento salvo com sucesso!");
	}

	public void aoSelelecionarMantenedora() {
		EmpresaFilter filterCliente = new EmpresaFilter();
		filterCliente.setTipoEmpresa(TipoEmpresa.CLIENTE);
		filterCliente.setMantenedora(orcamento.getMantenedora());
		clientes = empresaRepository.listAll(filterCliente);
		if (clientes.size() > 0)
			orcamento.setCliente(clientes.get(0));
	}

	public void onItemSelect(SelectEvent<String> event) {
		this.itemOrcamento.setDescricao(this.itemOrcamento.getServico().getDescricao());
		this.itemOrcamento.setValor(this.itemOrcamento.getServico().getValor());
		this.itemOrcamento.setOrcamento(this.orcamento);
		if (isPodeInserir())
			this.itensOrcamento.add(this.itemOrcamento);
		this.itemOrcamento = new ItemOrcamento();
	}

	public boolean isPodeInserir() {
		for (ItemOrcamento it : this.itensOrcamento) {
			if (it.getServico().getId().equals(itemOrcamento.getServico().getId()))
				return false;
		}
		return true;
	}

	public void removerItem(ItemOrcamento item) {
		itensOrcamento.remove(item);
	}

	public boolean isEditando() {
		return this.orcamento.getId() != null;
	}

	public BigDecimal getTotalItens() {
		BigDecimal total = BigDecimal.ZERO;
		for (ItemOrcamento it : this.itensOrcamento) {
			total = total.add(it.getValorTotal());
		}
		;
		return total;
	}


	public void cancelarOrcamento() {
		Orcamento orcamentoSalvo = orcamentoRepository.findById(orcamento.getId());
		orcamentoSalvo.setCancelado(true);
		orcamento = orcamentoService.salvar(orcamentoSalvo);
	}

	public boolean isDisableEnviarEmail() {
		return this.orcamento.isCancelado() || !this.isEditando();
	}

	public boolean isDisableSalvar() {
		return this.orcamento.isCancelado();
	}

	public boolean isDisableCancelar() {
		return this.orcamento.isCancelado() || !this.isEditando();
	}

	public boolean isDisableEmitir() {
		return this.orcamento.isCancelado() || !this.isEditando();
	}

	public boolean isCancelado() {
		return this.orcamento.isCancelado();
	}

	@SuppressWarnings("rawtypes")
	public void handleReturn(SelectEvent event) {
		Servico servico = (Servico) event.getObject();
		this.itemOrcamento.setServico(servico);
		this.itemOrcamento.setDescricao(this.itemOrcamento.getServico().getDescricao());
		this.itemOrcamento.setValor(this.itemOrcamento.getServico().getValor());
		this.itemOrcamento.setOrcamento(this.orcamento);
		this.itensOrcamento.add(this.itemOrcamento);
		this.itemOrcamento = new ItemOrcamento();
	}

	public void emitirOrcamento() {
		orcamento = orcamentoService.salvar(orcamento);
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("pId", orcamento.getId());
		String nomeRel = "Orcamento_" + numeroFormatado(orcamento.getId()) + ".pdf";
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/orcamento.jasper", this.response, parametros,
				nomeRel);

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		facesContext.responseComplete();

		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErroMessage("A execução do relatório não retornou dados.");
		}
	}

	private String numeroFormatado(Long id) {
		return String.format("%06d", id);
	}

}
