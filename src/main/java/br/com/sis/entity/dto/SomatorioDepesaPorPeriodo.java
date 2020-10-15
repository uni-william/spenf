package br.com.sis.entity.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SomatorioDepesaPorPeriodo {
	private String descricao;
	private BigDecimal total;
	private Long quantidade;

}
