package br.com.sis.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.enuns.TipoRegime;
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
@Table(name = "empresas")
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Column(length = 20, nullable = false)
	private String cnpj;

	@NotEmpty
	@Column(length = 120, nullable = false)
	private String razaoSocial;
	
	@NotEmpty
	@Column(length = 120, nullable = false)
	private String nomeFantasia;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private TipoRegime crt;
	
	@NotEmpty
	@Column(length = 30, nullable = false)
	private String inscricaoEstadual;
	
	@NotEmpty
	@Column(length = 30, nullable = false)
	private String inscricaoMunicipal;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private TipoEmpresa tipoEmpresa;	
	
	@Column(length = 20)
	private String telefone;	

	@Column(length = 100)
	private String email;	
	
	@ManyToOne
	@JoinColumn(name = "mantenedora_id")
	private Empresa mantenedora;

	@Embedded
	private Endereco endereco = new Endereco();

}
