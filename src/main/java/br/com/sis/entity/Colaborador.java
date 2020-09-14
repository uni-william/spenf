package br.com.sis.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
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
@Table(name = "colaboradores")
public class Colaborador implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Column(length = 20, nullable = false)
	private String cpf;

	@Column(length = 20)
	private String rg;

	@NotEmpty
	@Column(length = 100, nullable = false)
	private String nome;

	@NotEmpty
	@Email
	@Column(length = 100)
	private String email;

	@Column(length = 100, nullable = false)
	private String password;

	@Column(length = 20)
	private String telefone;

	@Column(length = 20)
	private String celular;

	private boolean ativo = true;

	@ManyToOne
	@JoinColumn(name = "mantenedora_id")
	private Empresa mantenedora;

	@Embedded
	private Endereco endereco = new Endereco();

	@ManyToMany
	@JoinTable(name = "colaborador_perfil", joinColumns = @JoinColumn(name = "colaborador_id"), inverseJoinColumns = @JoinColumn(name = "perfil_id"))
	private List<Perfil> perfis = new ArrayList<Perfil>();
	
	@Transient
	public String getDescricaoStatus() {
		return this.ativo == true ? "Ativo" : "Inativo";
	}

}
