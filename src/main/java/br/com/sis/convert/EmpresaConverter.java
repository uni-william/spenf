package br.com.sis.convert;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Empresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.util.cdi.CDIServiceLocator;

@Named
@FacesConverter(forClass = Empresa.class)
public class EmpresaConverter implements Converter<Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private EmpresaRepository empresaRepository;
	
	public EmpresaConverter() {
		empresaRepository = CDIServiceLocator.getBean(EmpresaRepository.class);
	}

	@Override
	public Empresa getAsObject(FacesContext context, UIComponent component, String value) {
		Empresa empresa = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = Long.parseLong(value);
			empresa = empresaRepository.findById(id);
		}
		return empresa;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Empresa empresa = (Empresa) value;
			return empresa.getId() == null ? null : empresa.getId().toString();
		}
		return "";
	}

}
