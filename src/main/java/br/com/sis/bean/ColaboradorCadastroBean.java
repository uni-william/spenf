package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DualListModel;

import br.com.sis.entity.Colaborador;
import br.com.sis.entity.Empresa;
import br.com.sis.entity.Perfil;
import br.com.sis.entity.dto.CepVO;
import br.com.sis.enuns.Estado;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.PerfilRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.service.ColaboradorService;
import br.com.sis.service.NegocioException;
import br.com.sis.util.Utils;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ColaboradorCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Colaborador colaborador;

	@Inject
	private ColaboradorService colaboradorService;

	@Inject
	private PerfilRepository perfilRepository;

	@Inject
	private EmpresaRepository empresaRepository;

	@Getter
	private List<Empresa> mantenedoras;

	@Getter
	private boolean podeSalvar;

	@Getter
	@Setter
	private DualListModel<Perfil> perfis;

	public void inicializar() {
		EmpresaFilter filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		this.mantenedoras = empresaRepository.listAll(filter);
		List<Perfil> totalPerfis = perfilRepository.listAll();
		if (this.mantenedoras.size() > 0) {
			podeSalvar = true;
			if (colaborador == null) {
				colaborador = new Colaborador();
				this.colaborador.setMantenedora(this.mantenedoras.get(0));
				this.colaborador.getEndereco().setEstado(Estado.AM);
				this.colaborador.getEndereco().setCodigoIbegeEstado("13");
				this.colaborador.getEndereco().setCodigoIbegePais("1058");
				this.colaborador.getEndereco().setPais("Brasil");
			}
			List<Perfil> target = new ArrayList<Perfil>();
			List<Perfil> source = new ArrayList<Perfil>();
			target = colaborador.getPerfis();
			for (Perfil perfil : totalPerfis) {
				if (target.contains(perfil) == false) {
					source.add(perfil);
				}
			}
			perfis = new DualListModel<Perfil>(source, target);

		} else {
			FacesUtil.addWarnMessage("Não existe Mantenedora cadastrada.Verifique!");
			podeSalvar = false;
		}
	}

	public void salvar() {
		try {
			colaborador.getPerfis().clear();
			colaborador.setPerfis(perfis.getTarget());
			colaborador = colaboradorService.salvar(colaborador);
			FacesUtil.addInfoMessage("Registro salvo salvo com sucesso!");
		} catch (NegocioException e) {
			FacesUtil.addErroMessage(e.getMessage());
		}
	}

	public boolean isEditando() {
		return this.colaborador.getId() != null;
	}

	public void carregarDadosCep() {
		CepVO cepVO = Utils.retonaDadosEndereco(this.getColaborador().getEndereco().getCep());
		if (cepVO != null) {
			this.getColaborador().getEndereco().setLogradouro(cepVO.getLogradouro());
			this.getColaborador().getEndereco().setComplemento(cepVO.getComplemento());
			this.getColaborador().getEndereco().setBairro(cepVO.getBairro());
			this.getColaborador().getEndereco().setNomeCidade(cepVO.getLocalidade());
			this.getColaborador().getEndereco().setEstado(Estado.valueOf(cepVO.getUf()));
			this.getColaborador().getEndereco().setCodigoIbegeCidade(cepVO.getIbge());
		}
		else 
			FacesUtil.addErroMessage("CEP não encontrado!");		

	}

}
