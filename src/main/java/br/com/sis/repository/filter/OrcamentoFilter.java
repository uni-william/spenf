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
public class OrcamentoFilter {
	
	private Empresa mantenedora;
	private Empresa cliente;
	private LocalDate dataCricaoInicio;
	private LocalDate dataCricaoFim;
	private LocalDate dataPedidoIni;
	private LocalDate dataPedidoFim;	
	
	private String pedido;
	private boolean somenteComPedido = false;
	private boolean emAbertos = false;
	private Long numeroNota;

}
