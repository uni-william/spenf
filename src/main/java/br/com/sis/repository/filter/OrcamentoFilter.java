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
	
	private Long numeroOrcamento;
	private Empresa mantenedora;
	private Empresa cliente;
	
	private LocalDate dataCricaoIni;
	private LocalDate dataCricaoFim;
	
	private LocalDate dataPedidoIni;
	private LocalDate dataPedidoFim;	
	
	private LocalDate dataNotaIni;
	private LocalDate dataNotaFim;
	
	private LocalDate dataPrevisaoPagamentoIni;
	private LocalDate dataPrevisaoPagamentoFim;	
	
	private LocalDate dataPagamentoIni;
	private LocalDate dataPagamentoFim;	
	
	private String pedido;
	private boolean somenteComPedido = false;
	private boolean emAbertos = false;
	private boolean somenteComNota=false;
	private Long numeroNota;

}
