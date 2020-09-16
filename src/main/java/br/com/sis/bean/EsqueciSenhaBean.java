package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.sis.service.ColaboradorService;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class EsqueciSenhaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ColaboradorService colaboradorService;
	
	@Getter
	@Setter
	@NotEmpty
	@Email
	private String email;
	
	public void redefinirSenha() {
		if (colaboradorService.redefinirSenha(email)) {
			FacesUtil.redirecionarPaginaComMensagem("Login.xhtml", "Senha alterada com sucesso!");
		} else {
			FacesUtil.addErroMessage("E-mail não encontrado. Verifique!");
		}

	}	

}
