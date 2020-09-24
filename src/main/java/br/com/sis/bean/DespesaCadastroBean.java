package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.Despesa;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.security.Seguranca;
import br.com.sis.service.DespesaService;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class DespesaCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Despesa despesa;

	@Inject
	private DespesaService despesaService;

	@Inject
	private EmpresaRepository empresaRepository;

	@Getter
	private List<Empresa> mantenedoras;

	@Inject
	private Seguranca seguranca;

	@Getter
	private boolean podeSalvar;

	public void inicializar() {
		EmpresaFilter filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		this.mantenedoras = empresaRepository.listAll(filter);
		if (this.mantenedoras.size() > 0) {
			this.podeSalvar = true;
			if (despesa == null) {
				despesa = new Despesa();
				this.despesa.setMantenedora(this.mantenedoras.get(0));
			}
		} else {
			FacesUtil.addWarnMessage("Não existe Mantenedora cadastrada.Verifique!");
			this.podeSalvar = false;
		}

		this.podeSalvar = this.podeSalvar && ((this.isEditando() && this.seguranca.isDespesaEditar())
				|| (!this.isEditando() && this.seguranca.isDespesaInserir()));
	}

	public void salvar() {
		despesa = despesaService.salvar(despesa);
		FacesUtil.addInfoMessage("Despesa salva com sucesso!");
	}

	public boolean isEditando() {
		return this.despesa.getId() != null;
	}

}
