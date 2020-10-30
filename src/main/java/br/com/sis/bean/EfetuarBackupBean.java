package br.com.sis.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.service.EmailService;
import br.com.sis.util.Utils;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class EfetuarBackupBean implements Serializable {

	private static final long serialVersionUID = 1L;
	

	@Inject
	private EmpresaRepository empresaRepository;
	
	@Inject
	private EmailService emailService;
	
	private Empresa mantenedora;
	private List<Empresa> mantenedoras;	
	
	@Getter
	@Setter
	private String emailDestino;
	
	public void inicializar() {
		EmpresaFilter filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		this.mantenedoras = empresaRepository.listAll(filter);
		this.mantenedora = this.mantenedoras.get(0);
	}
	
	public void enviarBackup() {	
		String arquivo = Utils.fazerBackup();
		if (arquivo != null) {
			String data = Utils.dataCompletaFormatada(new Date());
			String mensagem = "Banckup da base realizado em " + data;
			if (emailService.sendAttachmentEmailBackup(mantenedora, "Backup base de dados", mensagem, emailDestino, arquivo))
				FacesUtil.addInfoMessage("Backup realizado com sucesso!");
		}
	}

}
