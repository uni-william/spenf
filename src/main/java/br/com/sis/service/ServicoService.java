package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Servico;
import br.com.sis.repository.ServicoRepository;
import br.com.sis.util.jpa.Transactional;

public class ServicoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ServicoRepository servicoRepository;

	@Transactional
	public Servico salvar(Servico servico) {
		return servicoRepository.salvar(servico);
	}

}
