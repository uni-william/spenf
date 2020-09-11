package br.com.sis.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.com.sis.entity.Colaborador;

public class UsuarioSistema extends User {
	
	private static final long serialVersionUID = 1L;
	
	private Colaborador usuario;

	public UsuarioSistema(Colaborador usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getEmail(), usuario.getPassword(), authorities);
		this.usuario = usuario;
	}

	public Colaborador getUsuario() {
		return usuario;
	}
	

}
