package br.com.sis.service;

import java.io.Serializable;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import br.com.sis.entity.Empresa;

public class EmailService implements Serializable {

	private static final long serialVersionUID = 1L;

	public void sendSimpleEmail(Empresa mantenedora, String remetente, String assunto, String mensagem,
			String destinatario) {

		Email email = new SimpleEmail();
		email.setHostName(mantenedora.getHost());
		email.setSmtpPort(mantenedora.getPorta());
		email.setAuthenticator(
				new DefaultAuthenticator(mantenedora.getUsuarioEnviaEmail(), mantenedora.getSenhaUsuarioEmail()));
		email.setSSLOnConnect(mantenedora.isSslOnConection());
		try {
			email.setFrom(mantenedora.getEmailEnvio());
			email.setSubject(assunto);
			email.setMsg(mensagem);
			email.addTo(destinatario);
			email.send();

		} catch (EmailException e) {
			System.out.println(e.getMessage());
		}

	}
}
