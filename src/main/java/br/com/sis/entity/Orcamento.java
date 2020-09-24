package br.com.sis.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "orcamentos")

public class Orcamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "mantenedora_id")
	private Empresa mantenedora;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Empresa cliente;

	@Column(length = 120)
	private String solicitante;

	@Column(length = 120)
	private String responsavel;

	private LocalDate dataOrcamento;

	@Column(length = 20)
	private String pedidoCliente;

	private LocalDate dataRecebimentoPedido;
	private LocalDate dataEntregaPedido;

	@Column(precision = 10, scale = 2)
	private BigDecimal valorOrcamento;

	private boolean cancelado = false;

	private Long numeroNfse;
	@Column(length = 20)
	private String serieNfse;

	private LocalDate dataEmissaoNota;
	private LocalDate dataPrevisaoPagamento;
	private LocalDate dataEfetivaPagamento;

	@OneToMany(mappedBy = "orcamento", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	List<ItemOrcamento> itensOrcamento = new ArrayList<ItemOrcamento>();

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "emails_orcamento", joinColumns = @JoinColumn(name = "orcamento_id"))
	@Column(name = "emails_orcamento")
	private List<String> emailsOrcamento = new ArrayList<String>();

	public String getIdFormatted() {
		if (this.getId() != null)
			return String.format("%06d", this.getId());
		return null;
	}

	@Transient
	public String getDataOrcamentoFormatted() {
		if (this.getDataOrcamento() != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			return this.getDataOrcamento().format(formatter);
		}

		return null;
	}

	@Transient
	public boolean isJaEntregue() {
		return this.getDataEmissaoNota() != null;
	}

	@Transient
	public String getStatusEntrega() {
		if (this.getDataEmissaoNota() != null)
			return "Entregue";
		else {
			if (this.getDataEntregaPedido().isAfter(LocalDate.now()))
				return "No prazo";
			else if (this.getDataEntregaPedido().isBefore(LocalDate.now()))
				return "Atrasada";
			else
				return "Entregar hoje";
		}
	}
	
	@Transient
	public String getEntregaCor() {
		if (this.getDataEmissaoNota() != null)
			return "black";
		else {
			if (this.getDataEntregaPedido().isAfter(LocalDate.now()))
				return "green";
			else if (this.getDataEntregaPedido().isBefore(LocalDate.now()))
				return "red";
			else
				return "blue";
		}		
	}

	@Transient
	public String getStatusPagamento() {
		if (this.getDataEmissaoNota() != null) {
			if (this.getDataEfetivaPagamento() != null)
				return "Pago";
			else {
				if (this.getDataPrevisaoPagamento().isBefore(LocalDate.now()))
					return "Vencido";
				else if (this.getDataPrevisaoPagamento().isAfter(LocalDate.now()))
					return "A vencer";
				else
					return "Vencendo hoje";
			}
		} else
			return null;
	}
	
	@Transient
	public boolean isJaPago() {
		return this.getDataEfetivaPagamento() != null;
	}	
	
	@Transient
	public String getPagamentoCor() {
		if (this.getDataEmissaoNota() != null) {
			if (this.getDataEfetivaPagamento() != null)
				return "black";
			else {
				if (this.getDataPrevisaoPagamento().isBefore(LocalDate.now()))
					return "red";
				else if (this.getDataPrevisaoPagamento().isAfter(LocalDate.now()))
					return "green";
				else
					return "blue";
			}
		} else
			return "black";		
	}
	

	@Transient
	public String getCanceladoDescription() {
		if (this.cancelado)
			return "CANCELADO";
		else
			return null;
	}

}
