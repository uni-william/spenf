package br.com.sis.util;

import java.io.Serializable;
import java.util.Random;

public class Utils implements Serializable {
	
	private static final long serialVersionUID = 1L;

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

}
