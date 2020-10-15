package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.TipoDespesa;
import br.com.sis.repository.TipoDespesaRepository;
import br.com.sis.util.jpa.Transactional;

public class TipoDespesaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TipoDespesaRepository tipoDespesaRepository;

	@Transactional
	public TipoDespesa salvar(TipoDespesa tipoDespesa) {
		return tipoDespesaRepository.salvar(tipoDespesa);
	}

}
