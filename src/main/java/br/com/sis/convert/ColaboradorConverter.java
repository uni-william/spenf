package br.com.sis.convert;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Colaborador;
import br.com.sis.repository.ColaboradorRepository;
import br.com.sis.util.cdi.CDIServiceLocator;

@Named
@FacesConverter(forClass = Colaborador.class)
public class ColaboradorConverter implements Converter<Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private ColaboradorRepository colaboradorRepository;
	
	public ColaboradorConverter() {
		colaboradorRepository = CDIServiceLocator.getBean(ColaboradorRepository.class);
	}

	@Override
	public Colaborador getAsObject(FacesContext context, UIComponent component, String value) {
		Colaborador colaborador = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = Long.parseLong(value);
			colaborador = colaboradorRepository.findById(id);
		}
		return colaborador;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Colaborador colaborador = (Colaborador) value;
			return colaborador.getId() == null ? null : colaborador.getId().toString();
		}
		return "";
	}

}
