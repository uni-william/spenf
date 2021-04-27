package br.com.sis.repository.filter;

import java.time.LocalDate;

import br.com.sis.entity.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DespesaFilter {

	private LocalDate dataIni;
	private LocalDate dataFim;
	private LocalDate dataPagtoIni;
	private LocalDate dataPagtoFim;	
	private String descricao;
	private Empresa mantenedora;
	private int tipos = 0;
	
}
