package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Orcamento;
import br.com.sis.repository.OrcamentoRepository;
import br.com.sis.util.jpa.Transactional;

public class OrcamentoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private OrcamentoRepository orcamentoRepository;

	@Transactional
	public Orcamento salvar(Orcamento orcamento) {
		return orcamentoRepository.salvar(orcamento);
	}
	
	@Transactional
	public Orcamento cancelarOrcamento(Orcamento orcamento) {
		Orcamento orcamentoSalvo = orcamentoRepository.findById(orcamento.getId());
		orcamentoSalvo.setCancelado(true);
		return orcamentoRepository.salvar(orcamentoSalvo);
	}

}
