package br.com.sis.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FluxoDeCaixa {
	
	private String descricao;
	private String tipo;
	private LocalDate data;
	private BigDecimal valor;
	
	public String getCor() {
		return this.tipo.equals("R") ? "blue" : "red";
	}

}
