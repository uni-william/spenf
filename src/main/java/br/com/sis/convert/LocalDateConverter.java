package br.com.sis.convert;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="localDateConverter")
public class LocalDateConverter implements Converter<Object>, Serializable {
	private static final long serialVersionUID = 1L;	

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {		
		return LocalDate.parse(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		LocalDate dateValue = (LocalDate) value;
        return dateValue.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
