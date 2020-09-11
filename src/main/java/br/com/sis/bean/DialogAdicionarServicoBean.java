package br.com.sis.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.Servico;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.service.ServicoService;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class DialogAdicionarServicoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmpresaRepository empresaRepository;
	
	@Inject
	private ServicoService servicoService;
	
	@Getter
	@Setter
	private Servico servico;
	
	public void inicializar() {
		servico = new Servico();
		EmpresaFilter filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		List<Empresa> mantenedoras = empresaRepository.listAll(filter);
		servico.setMantenedora(mantenedoras.get(0));
		System.out.println("executou!");
	}
	

	public void view() {
	    Map<String,Object> options = new HashMap<String, Object>();
	    options.put("modal", true);
	    options.put("draggable", false);
	    options.put("resizable", false);
	    options.put("contentHeight", 250);
	    PrimeFaces.current().dialog().openDynamic("/dialogos/DialogServico", options, null);
	}	

	public void adicionarServico() {
		servicoService.salvar(servico);
		PrimeFaces.current().dialog().closeDynamic(servico);
	}

}
