package br.com.sis.convert;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.DespesaMaterial;
import br.com.sis.repository.DespesaMaterialRepository;
import br.com.sis.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = DespesaMaterial.class)
public class DespesaMaterialConverter implements Converter<Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private DespesaMaterialRepository despesaMaterialRepository;
	
	public DespesaMaterialConverter() {
		despesaMaterialRepository = CDIServiceLocator.getBean(DespesaMaterialRepository.class);
	}

	@Override
	public DespesaMaterial getAsObject(FacesContext context, UIComponent component, String value) {
		DespesaMaterial despesaMaterial = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = Long.parseLong(value);
			despesaMaterial = despesaMaterialRepository.findById(id);
		}
		return despesaMaterial;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			DespesaMaterial despesaMaterial = (DespesaMaterial) value;
			return despesaMaterial.getId() == null ? null : despesaMaterial.getId().toString();
		}
		return "";
	}

}
