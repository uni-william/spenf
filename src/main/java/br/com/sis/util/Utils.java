package br.com.sis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.UnknownFormatConversionException;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import br.com.sis.entity.dto.CepVO;
import br.com.sis.util.jsf.FacesUtil;

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
	
	public static  boolean fazerBackup() {
		String caminhoBackup = FacesUtil.localArquivos();
		File diretorio = new File(caminhoBackup);
		if(!diretorio.exists()) {
			diretorio.mkdir();
		}
		caminhoBackup = FacesUtil.localArquivos() +  "db_prime_backup.sql";
		File arq = new File(caminhoBackup);
		if (arq.exists())
			arq.delete();
		Process proc = null;
		String mysqldump = "mysqldump";
		String system = System.getProperty("os.name");
		if (system.toLowerCase().contains("windows")) {
			mysqldump = "C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\" + mysqldump;
		}
		try {
			proc = Runtime.getRuntime().exec(mysqldump + " --databases primedb -u primeroot -pprime > " + caminhoBackup);
			Path path = Paths.get(caminhoBackup);
			String line = inputStreamToString(proc.getInputStream());
			Files.createFile(path);
		    //printa o retorno
		   	Files.write(path, line.getBytes(), StandardOpenOption.APPEND);
			return true;
		} catch (IOException | UnknownFormatConversionException e) {
			FacesUtil.addErroMessage(e.getMessage() != null ? e.getMessage(): e.getCause().toString());		
			return false;
		}
	}
	
	private static String inputStreamToString(InputStream isr) {
	     try {
	        BufferedReader br = new BufferedReader(new InputStreamReader(isr, "UTF-8"));
	        StringBuilder sb = new StringBuilder();
	        String s = null;
	        while ((s = br.readLine()) != null) {
	                  sb.append(s + "\n");
	        }
	        return sb.toString();
	     } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return null;
	     }
	  }	

}
