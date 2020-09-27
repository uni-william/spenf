package br.com.sis.service;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.generic.NumberTool;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.Orcamento;

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

	public boolean sendHtmlEmail(Empresa mantenedora, Orcamento orcamento) {
		HtmlEmail email = new HtmlEmail();
		email.setHostName(mantenedora.getHost());
		email.setSmtpPort(mantenedora.getPorta());
		email.setAuthenticator(
				new DefaultAuthenticator(mantenedora.getUsuarioEnviaEmail(), mantenedora.getSenhaUsuarioEmail()));
		email.setSSLOnConnect(mantenedora.isSslOnConection());
		try {
			for (String contato : orcamento.getEmailsOrcamento()) {
				email.addTo(contato);
			}
			email.setFrom(mantenedora.getEmailEnvio());
			email.setSubject(mantenedora.getNomeFantasia() + " - Orçamento - " + orcamento.getIdFormatted());
			email.setHtmlMsg(htmlMsg(orcamento));
			email.setTextMsg("Não carregou HTML");
			email.send();
			return true;
		} catch (EmailException e) {
			System.out.println(e.getMessage());
			return false;
		}

	}

	public boolean sendAttachmentEmail(Empresa mantenedora, Orcamento orcamento, String arquivo) {
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(arquivo);
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
	attachment.setDescription("Orçamento_" + orcamento.getIdFormatted() + ".pdf");
		attachment.setName("Orçamento_" + orcamento.getIdFormatted() + ".pdf");
		// Create the email message
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName(mantenedora.getHost());
		email.setSmtpPort(mantenedora.getPorta());
		email.setAuthenticator(
				new DefaultAuthenticator(mantenedora.getUsuarioEnviaEmail(), mantenedora.getSenhaUsuarioEmail()));
		email.setSSLOnConnect(mantenedora.isSslOnConection());		
		try {
			for (String contato : orcamento.getEmailsOrcamento()) {
				email.addTo(contato);
			}
			email.setFrom(mantenedora.getEmailEnvio());
			email.setSubject(mantenedora.getNomeFantasia() + " - Orçamento - " + orcamento.getIdFormatted());
			email.setMsg("Em anexo - Orçamento - " + orcamento.getIdFormatted());
			// add the attachment
			email.attach(attachment);
			// send the email
			email.send();
			return true;
		} catch (EmailException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	private String htmlMsg(Orcamento orcamento) {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();
		VelocityContext context = new VelocityContext();
		context.put("orcamento", orcamento);
		context.put("numberTool", new NumberTool());
		context.put("locale", new Locale("pt", "BR"));
		Template t = ve.getTemplate("emails/orcamento.vm");
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}
}
