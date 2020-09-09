package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Perfil;
import br.com.sis.repository.PerfilRepository;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class PerfilPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PerfilRepository perfilRepository;

	@Getter
	private List<Perfil> perfis;

	@Getter
	@Setter
	private Perfil perfilSelecionado;
	
	public void inicializar() {
		pesquisar();
	}
	
	public void pesquisar() {
		perfis = perfilRepository.listAll();
	}

	public void excluir() {
		if (perfilRepository.remover(this.perfilSelecionado)) {
			FacesUtil.addInfoMessage("Perfil excluída com sucesso");
			perfis = perfilRepository.listAll();
		}
	}

}
