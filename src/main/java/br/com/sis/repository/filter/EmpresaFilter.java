package br.com.sis.repository.filter;

import br.com.sis.entity.Empresa;
import br.com.sis.enuns.TipoEmpresa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaFilter {

	private String cnpj;
	private String razaoSocial;
	private String nomeFantasia;
	private String inscricaoEstadual;
	private String inscricaoMunicipal;
	private TipoEmpresa tipoEmpresa;
	private Empresa mantenedora;
	
}
