package br.com.sis.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.sis.entity.Colaborador;
import br.com.sis.entity.Perfil;
import br.com.sis.enuns.Funcionalidade;
import br.com.sis.repository.ColaboradorRepository;
import br.com.sis.util.cdi.CDIServiceLocator;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		ColaboradorRepository repository = CDIServiceLocator.getBean(ColaboradorRepository.class);
		Colaborador usuario = repository.findByEmail(login);
		UsuarioSistema user = null;

		if (usuario != null) {
			user = new UsuarioSistema(usuario, getAuthorities(usuario));
		}

		return user;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Colaborador usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		ColaboradorRepository repository = CDIServiceLocator.getBean(ColaboradorRepository.class);
		usuario = repository.findById(usuario.getId());
		for (Perfil perfil : usuario.getPerfis()) {
			for (Funcionalidade pagina : perfil.getFuncionalidades()) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + pagina.toString()));
			}
		}
		
		return authorities;
	}

}
