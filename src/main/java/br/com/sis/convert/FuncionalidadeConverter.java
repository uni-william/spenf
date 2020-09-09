package br.com.sis.convert;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.sis.enuns.Funcionalidade;

@FacesConverter(value="funcionalidadeConverter")
public class FuncionalidadeConverter implements Converter<Object>, Serializable {
	private static final long serialVersionUID = 1L;	

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {		
		return Funcionalidade.valueOf(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
	}

}
