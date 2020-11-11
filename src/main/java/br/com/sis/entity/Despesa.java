package br.com.sis.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

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
@Table(name = "despesas")
public class Despesa implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(length = 256, nullable = false)	
	private String descricao;
	
	@NotNull
	@Column(precision = 10, scale = 2)
	private BigDecimal valor;
	
	@Column(length = 20)
	private String comprovante;
	
	private LocalDate data;
	
	private LocalDate dataPagamento;
		
	@ManyToOne
	@JoinColumn(name = "mantenedora_id")
	private Empresa mantenedora;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "tipo_despesa_id", nullable = false)	
	private TipoDespesa tipoDespesa;
	
	@Transient
	public String getStatus() {
		if (this.getDataPagamento() != null)
			return "Paga";
		else {
			if (this.getData().isAfter(LocalDate.now()))
				return "A vencer";
			else if (this.getData().isBefore(LocalDate.now()))
				return "Vencida";
			else
				return "Vencedo hoje";
		}
	}

}
