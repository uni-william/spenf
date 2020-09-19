package br.com.sis.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.dto.CepVO;
import br.com.sis.enuns.Estado;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.enuns.TipoRegime;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.service.EmpresaService;
import br.com.sis.util.Utils;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ClienteCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String URL_VIA_CEP = "http://viacep.com.br/ws/";
	private static final String FORMATO = "/json/";

	@Getter
	@Setter
	private Empresa empresa;

	@Getter
	private List<Empresa> mantenedoras;

	@Inject
	private EmpresaService empresaService;

	@Inject
	private EmpresaRepository empresaRepository;

	@Getter
	@Setter
	private String email;
	
	@Getter
	@Setter
	private List<String> emails = new ArrayList<String>();

	@Getter
	private boolean podeSalvar;

	public void inicializar() {
		EmpresaFilter filter = new EmpresaFilter();
		filter.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		this.mantenedoras = empresaRepository.listAll(filter);
		if (this.mantenedoras.size() > 0) {
			if (empresa == null) {
				empresa = new Empresa();
				empresa.getEndereco().setEstado(Estado.AM);
				empresa.getEndereco().setCodigoIbegeEstado("13");
				empresa.getEndereco().setCodigoIbegePais("1058");
				empresa.getEndereco().setPais("Brasil");
				empresa.setTipoEmpresa(TipoEmpresa.CLIENTE);
				empresa.setCrt(TipoRegime.CLIENTE);
				empresa.setMantenedora(this.mantenedoras.get(0));
			}
			else {
				this.emails = empresa.getEmailsCliente();
			}
			podeSalvar = true;
		} else {
			FacesUtil.addWarnMessage("Não existe Mantenedora cadastrada. Verifique!");
			podeSalvar = false;
		}
	}

	public void salvar() {
		empresa.setEmailsCliente(this.emails);
		empresa = empresaService.salvar(empresa);
		FacesUtil.addInfoMessage("Cliente salvo com sucesso!");
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
		String cep = Utils.removerCaracter(Utils.removerCaracter(this.getEmpresa().getEndereco().getCep(), "."), "-");
		String url = URL_VIA_CEP + cep + FORMATO;
		if (!StringUtils.isEmpty(cep)) {
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				if (conn.getResponseCode() != 200) {
					System.out.println("Erro " + conn.getResponseCode() + " ao obter dados da URL " + url);
				}
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output = "";
				String line;
				while ((line = br.readLine()) != null) {
					output += line;
				}
				conn.disconnect();
				Gson gson = new Gson();
				CepVO dados = gson.fromJson(new String(output.getBytes()), CepVO.class);
				this.getEmpresa().getEndereco().setLogradouro(dados.getLogradouro());
				this.getEmpresa().getEndereco().setComplemento(dados.getComplemento());
				this.getEmpresa().getEndereco().setBairro(dados.getBairro());
				this.getEmpresa().getEndereco().setNomeCidade(dados.getLocalidade());
				this.getEmpresa().getEndereco().setEstado(Estado.valueOf(dados.getUf()));
				this.getEmpresa().getEndereco().setCodigoIbegeCidade(dados.getIbge());

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	public void adicionarEmailLista() {
		if (!this.getEmails().contains(this.email) && !StringUtils.isEmpty(this.email)) {
			this.getEmails().add(this.email);
		}
		this.email = "";
	}

	public void removerEmailLista(String email) {
		this.getEmails().remove(email);
	}

}
