package br.com.sis.security;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import br.com.sis.entity.Empresa;
import br.com.sis.enuns.Funcionalidade;

@Named
@RequestScoped
public class Seguranca {

	@Inject
	private ExternalContext externalContext;

	public UsuarioSistema getUsuarioLogado() {
		UsuarioSistema usuario = null;
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext
				.getCurrentInstance().getExternalContext().getUserPrincipal();
		if (auth != null && auth.getPrincipal() != null) {
			usuario = (UsuarioSistema) auth.getPrincipal();

		}
		return usuario;
	}

	public String getNomeUsuario() {
		String nome = null;
		UsuarioSistema user = getUsuarioLogado();
		if (user != null) {
			nome = user.getUsuario().getNome();
		}
		return nome;
	}

	public String getNomeEmpresaUsuario() {
		String nomeCreche = null;
		UsuarioSistema user = getUsuarioLogado();
		nomeCreche = user.getUsuario().getMantenedora().getNomeFantasia();
		return nomeCreche;
	}

	public Empresa getCrecheUsuarioLogado() {
		Empresa empresa = null;
		UsuarioSistema user = getUsuarioLogado();
		empresa = user.getUsuario().getMantenedora();

		return empresa;
	}

	public boolean isPerfilInserir() {
		return externalContext.isUserInRole(Funcionalidade.PERFIL_INS.toString());
	}

	public boolean isPerfilEditar() {
		return externalContext.isUserInRole(Funcionalidade.PERFIL_UPD.toString());
	}

	public boolean isPerfilExcluir() {
		return externalContext.isUserInRole(Funcionalidade.PERFIL_DEL.toString());
	}
	
	public boolean isColaboradorEditar() {
		return externalContext.isUserInRole(Funcionalidade.COLABORADOR_UPD.toString());
	}
	
	public boolean isMantenedoraInserir() {
		return externalContext.isUserInRole(Funcionalidade.MANTENEDORA_INS.toString());
	}

	public boolean isMantenedoraEditar() {
		return externalContext.isUserInRole(Funcionalidade.MANTENEDORA_UPD.toString());
	}

	public boolean isMantenedoraExcluir() {
		return externalContext.isUserInRole(Funcionalidade.MANTENEDORA_DEL.toString());
	}	
	
	public boolean isDespesaInserir() {
		return externalContext.isUserInRole(Funcionalidade.DESPESA_INS.toString());
	}

	public boolean isDespesaEditar() {
		return externalContext.isUserInRole(Funcionalidade.DESPESA_UPD.toString());
	}

	public boolean isDespesaExcluir() {
		return externalContext.isUserInRole(Funcionalidade.DESPESA_DEL.toString());
	}	
	
	public boolean isPermiteLimparPagamento() {
		return externalContext.isUserInRole(Funcionalidade.PAGAMENTO_CLEAR.toString());
	}
	
	public boolean isPermiteLimparNota() {
		return externalContext.isUserInRole(Funcionalidade.NOTA_CLEAR.toString());
	}	
	
	public boolean isPermiteLimparPedido() {
		return externalContext.isUserInRole(Funcionalidade.PEDIDO_CLEAR.toString());
	}	
	

}
