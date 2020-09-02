package br.com.sis.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	
	@Column(length = 60)
	private String solicitante;
	
	@Column(length = 60)
	private String responsavel;
	
	private LocalDate dataOrcamento;
	
	private LocalDate prazoEntrega;
	
	private LocalDate prazoPagamento;
	
	@Column(length = 20)
	private String pedidoCliente;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal valorOrcamento;
	
	private LocalDate diaEntrega;
	
	private LocalDate diaPagamento;
	
	private boolean cancelado = false;
	
	private Long numeroNfse;
	
	@OneToMany(mappedBy = "orcamento", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	List<ItemOrcamento> itensOrcamento = new ArrayList<ItemOrcamento>();

}
