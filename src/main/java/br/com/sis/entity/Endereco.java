package br.com.sis.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.sis.enuns.Estado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

	@NotNull
	@Column(length = 120, nullable = false)
	private String logradouro;

	@Column(length = 20)
	private String numero;

	@Column(length = 50)
	private String complemento;
	
	@Column(length = 50)
	private String bairro;

	@Column(length = 15)
	private String cep;

	@NotEmpty
	@Column(length = 80, nullable = false)
	private String nomeCidade;

	@NotEmpty
	@Column(length = 8, nullable = false)
	private String codigoIbegeCidade;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 2, nullable = false)
	private Estado estado;

	@NotEmpty
	@Column(length = 8, nullable = false)
	private String codigoIbegeEstado;

	@NotEmpty
	@Column(length = 50, nullable = false)
	private String pais;

	@NotEmpty
	@Column(length = 4, nullable = false)
	private String codigoIbegePais;

}
