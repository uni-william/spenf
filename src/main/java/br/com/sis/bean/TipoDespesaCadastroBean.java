package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.TipoDespesa;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.service.TipoDespesaService;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class TipoDespesaCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private TipoDespesa tipoDespesa;

	@Inject
	private TipoDespesaService tipoDespesaService;

	@Inject
	private EmpresaRepository empresaRepository;

	@Getter
	private List<Empresa> mantenedoras;
	
	@Getter
	private boolean podeSalvar;

	public void inicializar() {
		EmpresaFilter filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		this.mantenedoras = empresaRepository.listAll(filter);
		if (this.mantenedoras.size() > 0) {
			podeSalvar = true;
			if (tipoDespesa == null) {
				tipoDespesa = new TipoDespesa();
				this.tipoDespesa.setMantenedora(this.mantenedoras.get(0));
			}
		} else {
			FacesUtil.addWarnMessage("Não existe Mantenedora cadastrada.Verifique!");
			podeSalvar = false;			
		}
	}

	public void salvar() {
		tipoDespesa = tipoDespesaService.salvar(tipoDespesa);
		FacesUtil.addInfoMessage("Registro salvo com sucesso!");
	}

	public boolean isEditando() {
		return this.tipoDespesa.getId() != null;
	}

}
