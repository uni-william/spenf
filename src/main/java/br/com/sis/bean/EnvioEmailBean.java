package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.Orcamento;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.service.EmailService;
import br.com.sis.service.EmpresaService;
import br.com.sis.service.OrcamentoService;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class EnvioEmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private OrcamentoService orcamentoService;

	@Inject
	private EmpresaRepository empresaRepository;

	@Inject
	private EmailService emailService;
	
	@Inject
	private EmpresaService empresaService;

	@Getter
	@Setter
	private Orcamento orcamento;

	@Getter
	private List<String> emailsContatosCLienteOrcamento = new ArrayList<String>();;

	@Getter
	private List<String> emails = new ArrayList<String>();

	@Getter
	@Setter
	private String email;

	public void inicializar() {
		if (orcamento != null) {
			this.emailsContatosCLienteOrcamento = orcamento.getCliente().getEmailsCliente();
			this.emails = orcamento.getEmailsOrcamento();
		}
	}

	public void removerItem(String email) {
		emails.remove(email);
	}

	public void adicionarEmail() {
		if (!StringUtils.isAllEmpty(email) && !emails.contains(email))
			emails.add(email);
		email = "";
	}

	public void sendEmail() {
		this.orcamento.setEmailsOrcamento(this.emails);
		this.orcamento = this.orcamentoService.salvar(this.orcamento);
		Empresa mantenedora = empresaRepository.findById(this.orcamento.getMantenedora().getId());
		Empresa cliente = empresaRepository.findById(this.orcamento.getCliente().getId());
		for (String email : emails) {
			if (!cliente.getEmailsCliente().contains(email)) {
				cliente.getEmailsCliente().add(email);
				cliente = empresaService.salvar(cliente);
			}
		}
		
		if (emailService.sendHtmlEmail(mantenedora, orcamento))
			FacesUtil.redirecionarPaginaComMensagemInformativa(
					"orcamentos/CadastroOrcamento.xhtml?orcamento=" + this.orcamento.getId().toString(),
					"E-mail enviado com sucesso!");
		else
			FacesUtil.addErroMessage("Erro ao enviar e-mail");
	}

}
