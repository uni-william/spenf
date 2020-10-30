package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.DespesaMaterial;
import br.com.sis.repository.DespesaMaterialRepository;
import br.com.sis.util.jpa.Transactional;

public class DespesaMaterialService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private DespesaMaterialRepository despesaMaterialRepository;

	@Transactional
	public DespesaMaterial salvar(DespesaMaterial despesaMaterial) {
		return despesaMaterialRepository.salvar(despesaMaterial);
	}

}
