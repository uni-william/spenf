package br.com.sis.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.validator.constraints.NotEmpty;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.sis.entity.ItemOrcamento;
import br.com.sis.entity.Orcamento;
import br.com.sis.repository.OrcamentoRepository;
import br.com.sis.service.OrcamentoService;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class PedidoCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private OrcamentoService orcamentoService;

	@Inject
	private OrcamentoRepository orcamentoRepository;

	@Getter
	@Setter
	private Orcamento orcamento;

	@Getter
	@Setter
	@NotEmpty
	private String numeroPedido;

	@Getter
	@Setter
	private LocalDate dataPedido;

	@Getter
	@Setter
	private LocalDate dataEntrega;

	@Getter
	private List<ItemOrcamento> itens;

	@Getter
	@Setter
	private StreamedContent file;

	public void inicializar() {
		if (this.orcamento != null) {
			itens = this.orcamento.getItensOrcamento();
			if (this.orcamento.getPedidoCliente() != null)
				this.numeroPedido = this.orcamento.getPedidoCliente();
			if (this.orcamento.getDataRecebimentoPedido() != null)
				this.dataPedido = this.orcamento.getDataRecebimentoPedido();
			else
				this.dataPedido = LocalDate.now();

			if (this.orcamento.getDataEntregaPedido() != null)
				this.dataEntrega = this.orcamento.getDataEntregaPedido();
			else
				this.dataEntrega = this.getDataPedido().plusDays(this.orcamento.getCliente().getPrazoEntrega());
		} else {
			FacesUtil.redirecionarPagina("Erro.xhtml");
		}

	}

	public void salvarPedido() {
		this.orcamento.setPedidoCliente(this.numeroPedido);
		this.orcamento.setDataRecebimentoPedido(this.dataPedido);
		this.orcamento.setDataEntregaPedido(this.dataEntrega);
		this.orcamento = orcamentoService.salvar(this.orcamento);
		FacesUtil.addInfoMessage("Pedido salvo com sucesso!");
	}

	public boolean isTemArquivo() {
		return this.orcamento.getNomeArquivo() != null;
	}

	public void carregarArquivo(FileUploadEvent event) {
		if (event.getFile() != null) {
			this.orcamento.setArquivo(event.getFile().getContent());
			this.orcamento.setNomeArquivo(event.getFile().getFileName());
			this.orcamento = orcamentoService.salvar(this.orcamento);
			FacesUtil.addInfoMessage("Arquivo anexado com sucesso!");
		} else {
			FacesUtil.addErroMessage("Sem arquivo para salvar");
		}

	}

	public void baixarPedido() {
		gerarImagemDisco();
		String nomeDownload = this.orcamento.getNomeArquivo();
		file = DefaultStreamedContent.builder()
                .contentType("application/pdf")
                .name(nomeDownload)
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/files/pedido.pdf"))
                .build();
	}

	private void gerarImagemDisco() {
		this.orcamento = orcamentoRepository.findById(this.orcamento.getId());
		String local = FacesUtil.localArquivos();
		String nome = "pedido.pdf";
		Path localArquivo = FileSystems.getDefault().getPath(local, "");
		local = local + File.separator + File.separator + nome;
		System.out.println(local);
		try {
			Path fotoTemp = localArquivo.resolve(nome);
			Files.write(fotoTemp, orcamento.getArquivo());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
