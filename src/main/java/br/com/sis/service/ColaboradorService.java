package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.sis.entity.Colaborador;
import br.com.sis.repository.ColaboradorRepository;
import br.com.sis.util.Utils;
import br.com.sis.util.jpa.Transactional;

public class ColaboradorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ColaboradorRepository colaboradorRepository;

	@Transactional
	public Colaborador salvar(Colaborador colaborador) {
		Colaborador colaboradorSalvo = colaboradorRepository.findByCpf(colaborador.getCpf());
		
		if (colaboradorSalvo != null && !colaborador.equals(colaboradorSalvo))
			throw new NegocioException("Já existe um colaborador cadastrado com esse CPF");
		
		colaboradorSalvo = colaboradorRepository.findByEmail(colaborador.getEmail());
		
		if (colaboradorSalvo != null && !colaborador.equals(colaboradorSalvo))
			throw new NegocioException("Já existe um colaborador cadastrado com esse E-mail");
		
		if (colaborador.getId() == null) {
			String senha = Utils.removerCaracter(Utils.removerCaracter(colaborador.getCpf(), "."), "-"); 
			colaborador.setPassword(new BCryptPasswordEncoder().encode(senha));
		}
		
		return colaboradorRepository.salvar(colaborador);
	}

}
