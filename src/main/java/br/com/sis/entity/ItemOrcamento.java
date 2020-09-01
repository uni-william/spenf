package br.com.sis.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "itens_orcamento")
public class ItemOrcamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "orcamento_id")
	private Orcamento orcamento;

	@ManyToOne
	@JoinColumn(name = "servico_id")
	private Servico servico;

	@Column(length = 256)
	private String descricao;

	@Column(precision = 10, scale = 0)
	private BigDecimal quantidade = BigDecimal.ONE;
	@Column(precision = 10, scale = 2)
	private BigDecimal valor;
	
	public BigDecimal getValortotal() {
		return quantidade != null && valor != null ? quantidade.multiply(valor) : null;
	}

}
