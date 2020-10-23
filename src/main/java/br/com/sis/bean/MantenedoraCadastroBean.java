package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.dto.CepVO;
import br.com.sis.enuns.Estado;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.enuns.TipoRegime;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.security.Seguranca;
import br.com.sis.service.EmpresaService;
import br.com.sis.util.Utils;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class MantenedoraCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Empresa empresa;

	@Inject
	private EmpresaRepository empresaRepository;

	@Inject
	private EmpresaService empresaService;

	@Getter
	private boolean podeSalvar = true;

	@Inject
	private Seguranca seguranca;

	public void inicializar() {
		if (empresa == null) {
			empresa = new Empresa();
			empresa.getEndereco().setEstado(Estado.AM);
			empresa.getEndereco().setCodigoIbegeEstado("13");
			empresa.getEndereco().setCodigoIbegePais("1058");
			empresa.getEndereco().setPais("Brasil");
			empresa.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
			empresa.setCrt(TipoRegime.SIMPLES_NACIONAL);
			if (this.isExisteMantenedoraCadastrada())
				podeSalvar = false;
			podeSalvar = podeSalvar && seguranca.isMantenedoraInserir();

		} else {
			podeSalvar = seguranca.isMantenedoraEditar();
		}
	}

	public void salvar() {
		empresa = empresaService.salvar(empresa);
		FacesUtil.addInfoMessage("Empresa salva com sucesso!");
	}

	public boolean isEditando() {
		return this.empresa.getId() != null;
	}

	public Estado[] getEstados() {
		return Estado.values();
	}

	public TipoRegime[] getTipoRegimes() {
		return TipoRegime.values();
	}

	public void carregarDadosCep() {
		CepVO cepVO = Utils.retonaDadosEndereco(this.getEmpresa().getEndereco().getCep());
		if (cepVO != null) {
			this.getEmpresa().getEndereco().setLogradouro(cepVO.getLogradouro());
			this.getEmpresa().getEndereco().setComplemento(cepVO.getComplemento());
			this.getEmpresa().getEndereco().setBairro(cepVO.getBairro());
			this.getEmpresa().getEndereco().setNomeCidade(cepVO.getLocalidade());
			this.getEmpresa().getEndereco().setEstado(Estado.valueOf(cepVO.getUf()));
			this.getEmpresa().getEndereco().setCodigoIbegeCidade(cepVO.getIbge());
		}
		else 
			FacesUtil.addErroMessage("CEP não encontrado!");		
	}

	public boolean isExisteMantenedoraCadastrada() {
		EmpresaFilter filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		List<Empresa> lista = empresaRepository.listAll(filter);
		return lista.size() > 0;
	}

}
