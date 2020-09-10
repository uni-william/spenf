package br.com.sis.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.sis.enuns.Funcionalidade;
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
@Table(name = "perfis")
public class Perfil implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(length = 50, nullable = false)
	private String descricao;

	@ElementCollection
	@CollectionTable(name = "perfil_funcionalidade", joinColumns = @JoinColumn(name = "perfil_id"))
	@Column(length = 30, name = "funcionalidade")
	@Enumerated(EnumType.STRING)
	private List<Funcionalidade> funcionalidades = new ArrayList<Funcionalidade>();
	
	@ManyToMany(mappedBy = "perfis")
	private List<Colaborador> colaboradores = new ArrayList<Colaborador>();

}
