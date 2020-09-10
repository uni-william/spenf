package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DualListModel;

import br.com.sis.entity.Perfil;
import br.com.sis.enuns.Funcionalidade;
import br.com.sis.service.NegocioException;
import br.com.sis.service.PerfilService;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class PerfilCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Perfil perfil;

	@Inject
	private PerfilService perfilService;
	
	@Getter
	@Setter
	private DualListModel<Funcionalidade> funcionalidades;

	public void inicializar() {
		List<Funcionalidade> target = new ArrayList<Funcionalidade>();
		List<Funcionalidade> source = new ArrayList<Funcionalidade>();
		if (perfil == null)
			perfil = new Perfil();
		target = perfil.getFuncionalidades();
		for (Funcionalidade func: Funcionalidade.values()) {
			if (target.contains(func) == false) {
				source.add(func);
			}
		}
		funcionalidades = new DualListModel<Funcionalidade>(source, target);
	}

	public void salvar() {
		try {
			perfil.getFuncionalidades().clear();
			perfil.setFuncionalidades(funcionalidades.getTarget());
			perfil = perfilService.salvar(perfil);
			FacesUtil.addInfoMessage("Perfil salvo com sucesso!");
		} catch (NegocioException e) {
			FacesUtil.addErroMessage(e.getMessage());
		}
	}

	public boolean isEditando() {
		return this.perfil.getId() != null;
	}

}
