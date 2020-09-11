package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class HomeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private HttpServletRequest request;

	public void inicializar() {
		if ("true".equals(request.getParameter("trocada")))
			FacesUtil.addInfoMessage("Sua senha foi alterada com sucesso!");
	}

}
