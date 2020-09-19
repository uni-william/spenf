package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.sis.entity.Orcamento;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class EnvioEmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

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

}
