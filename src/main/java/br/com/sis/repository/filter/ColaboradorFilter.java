package br.com.sis.repository.filter;

import br.com.sis.entity.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorFilter {

	private String cpf;
	private String nome;
	private Empresa mantenedora;
	
}
