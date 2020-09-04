package br.com.sis.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.com.sis.convert.ItemOrcamentoConverter;
import br.com.sis.entity.Empresa;
import br.com.sis.entity.ItemOrcamento;
import br.com.sis.entity.Orcamento;
import br.com.sis.entity.Servico;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
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
	

	public void inicializar() {
		EmpresaFilter filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		this.mantenedoras = empresaRepository.listAll(filter);
		if (orcamento == null) {
			orcamento = new Orcamento();
			orcamento.setMantenedora(mantenedoras.get(0));
			orcamento.setDataOrcamento(LocalDate.now());
		} else {
			this.itensOrcamento = orcamento.getItensOrcamento();
		}
		aoSelelecionarMantenedora();
		orcamento.setPrazoEntrega(orcamento.getDataOrcamento().plusDays(orcamento.getCliente().getPrazoEntrega()));
		orcamento.setPrazoPagamento(orcamento.getDataOrcamento().plusDays(orcamento.getCliente().getPrazoPagamento()));
		orcamento.setValidadeOrcamento(orcamento.getDataOrcamento().plusDays(orcamento.getCliente().getValidadeProposta()));
	}
	
	public List<Servico> completeServico(String descricao) {
		List<Servico> lista = servicoRepository.toAutoComplete(orcamento.getMantenedora(), descricao);
		return  lista;
	}

	public void salvar() {		
		orcamento.setItensOrcamento(this.itensOrcamento);
		orcamento.setValorOrcamento(this.getTotalItens());
		orcamento = orcamentoService.salvar(orcamento);
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
		for (ItemOrcamento it : itensOrcamento) {
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
			total = total.add(it.getValortotal());
		};
		return total;
	}

}
