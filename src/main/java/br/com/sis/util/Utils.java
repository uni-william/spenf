package br.com.sis.util;

import java.io.Serializable;

public class Utils implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static String removerCaracter(String origem, String caracter) {
		return origem.replace(caracter, "");
	}	

}
