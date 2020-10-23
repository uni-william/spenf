package br.com.sis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import br.com.sis.entity.dto.CepVO;

public class Utils implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String URL_VIA_CEP = "http://viacep.com.br/ws/";
	private static final String FORMATO = "/json/";	

	public static String removerCaracter(String origem, String caracter) {
		return origem.replace(caracter, "");
	}
	
	public static String geraSenha() {
		String letras = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvYyWwXxZz1234567890";
		Random random = new Random();
		String armazenaChaves = "";
		int index = -1;
		for (int i = 0; i < 6; i++) {
			index = random.nextInt(letras.length());
			armazenaChaves += letras.substring(index, index + 1);
		}
		return armazenaChaves;
	}	
	
	public static CepVO retonaDadosEndereco(String cep) {
		cep = removerCaracter(Utils.removerCaracter(cep, "."), "-");
		String url = URL_VIA_CEP + cep + FORMATO;
		if (!StringUtils.isEmpty(cep)) {
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				if (conn.getResponseCode() != 200) {
					System.out.println("Erro " + conn.getResponseCode() + " ao obter dados da URL " + url);
				}
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()), "UTF-8"));
				String output = "";
				String line;
				while ((line = br.readLine()) != null) {
					output += line;
				}
				conn.disconnect();
				Gson gson = new Gson();
				CepVO dados = gson.fromJson(new String(output.getBytes()), CepVO.class);
				return dados;

			} catch (IOException e) {
				return null;
			}
		}
		return null;
	}

}
