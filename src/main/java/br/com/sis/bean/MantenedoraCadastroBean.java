package br.com.sis.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

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
import br.com.sis.service.EmpresaService;
import br.com.sis.util.Utils;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class MantenedoraCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String URL_VIA_CEP = "http://viacep.com.br/ws/";
	private static final String FORMATO = "/json/";

	private Empresa empresa;

	@Inject
	private EmpresaService empresaService;

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public void inicializar() {
		if (empresa == null) {
			empresa = new Empresa();
			empresa.getEndereco().setEstado(Estado.AM);
			empresa.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
			empresa.setCrt(TipoRegime.SIMPLES_NACIONAL);
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

}
