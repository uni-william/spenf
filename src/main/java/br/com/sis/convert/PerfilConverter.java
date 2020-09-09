package br.com.sis.convert;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Perfil;
import br.com.sis.repository.PerfilRepository;
import br.com.sis.util.cdi.CDIServiceLocator;

@Named
@FacesConverter(forClass = Perfil.class)
public class PerfilConverter implements Converter<Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private PerfilRepository perfilRepository;
	
	public PerfilConverter() {
		perfilRepository = CDIServiceLocator.getBean(PerfilRepository.class);
	}

	@Override
	public Perfil getAsObject(FacesContext context, UIComponent component, String value) {
		Perfil perfil = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = Long.parseLong(value);
			perfil = perfilRepository.findById(id);
		}
		return perfil;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Perfil perfil = (Perfil) value;
			return perfil.getId() == null ? null : perfil.getId().toString();
		}
		return "";
	}

}
