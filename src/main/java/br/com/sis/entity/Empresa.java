package br.com.sis.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	@NotEmpty
	@Column(length = 10, nullable = false)
	private String crt;
	
	@NotEmpty
	@Column(length = 30, nullable = false)
	private String inscricaoEstadual;
	
	@NotEmpty
	@Column(length = 30, nullable = false)
	private String inscricaoMunicipal;

	@Embedded
	private Endereco endereco = new Endereco();

}
