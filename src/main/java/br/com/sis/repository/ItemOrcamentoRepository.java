package br.com.sis.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.sis.entity.ItemOrcamento;

public class ItemOrcamentoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public ItemOrcamento findById(Long id) {
		return manager.find(ItemOrcamento.class, id);
	}

}
