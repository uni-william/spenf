package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.sis.entity.Colaborador;
import br.com.sis.repository.ColaboradorRepository;
import br.com.sis.security.Seguranca;
import br.com.sis.service.ColaboradorService;
import br.com.sis.service.NegocioException;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class TrocaSenhaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private HttpServletRequest request;

	@Inject
	private Seguranca seguranca;

	@Inject
	private ColaboradorRepository colaboradorRepository;

	@Inject
	private ColaboradorService colaboradorService;

	@Getter
	@Setter
	@NotEmpty
	private String senhaAtual;
	@Getter
	@Setter
	@NotEmpty
	private String novaSenha;
	@Getter
	@Setter
	@NotEmpty
	private String confirmaSenha;

	private Colaborador colaborador;

	public void inicializar() {
		colaborador = seguranca.getUsuarioLogado().getUsuario();
		if ("true".equals(request.getParameter("troca"))) {
			FacesUtil.addWarnMessage("Sua senha deve ser alterada!");
		}
	}

	public void trocarSenha() {
		colaborador = colaboradorRepository.findById(colaborador.getId());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		if (encoder.matches(this.senhaAtual, colaborador.getPassword())) {
			String senhaCripto = encoder.encode(novaSenha);
			colaborador.setPassword(senhaCripto);
			try {
				colaborador = colaboradorService.salvar(colaborador);
				FacesUtil.redirecionarPagina("Home.xhtml?trocada=true");
			} catch (NegocioException e) {
				FacesUtil.addErroMessage(e.getMessage());
			}
		} else {
			FacesUtil.addErroMessage("A senha atual não confere!");
		}

	}

}
