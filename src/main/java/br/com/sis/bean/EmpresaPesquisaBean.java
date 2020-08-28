package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.repository.EmpresaRepository;

@Named
@ViewScoped
public class EmpresaPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmpresaRepository empresaRepository;

	private List<Empresa> empresas;

	private Empresa empresaSelecionada;

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}

	public void inicializar() {
		empresas = empresaRepository.listAll();
	}
	
	public void excluir() {
		empresaRepository.remover(this.empresaSelecionada);
	}

}
