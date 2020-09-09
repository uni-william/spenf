package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Perfil;
import br.com.sis.repository.PerfilRepository;
import br.com.sis.util.jpa.Transactional;

public class PerfilService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PerfilRepository perfilRepository;

	@Transactional
	public Perfil salvar(Perfil perfil) {
		Perfil perfilSalvo = perfilRepository.findByDescricao(perfil.getDescricao());
		if (perfilSalvo != null && !perfil.equals(perfilSalvo))
			throw new NegocioException("Já existe um perfil cadastrado com essa descrição");
		return perfilRepository.salvar(perfil);
	}

}
