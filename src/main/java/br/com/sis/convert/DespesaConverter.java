package br.com.sis.convert;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Despesa;
import br.com.sis.repository.DespesaRepository;
import br.com.sis.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Despesa.class)
public class DespesaConverter implements Converter<Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private DespesaRepository despesaRepository;
	
	public DespesaConverter() {
		despesaRepository = CDIServiceLocator.getBean(DespesaRepository.class);
	}

	@Override
	public Despesa getAsObject(FacesContext context, UIComponent component, String value) {
		Despesa despesa = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = Long.parseLong(value);
			despesa = despesaRepository.findById(id);
		}
		return despesa;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Despesa despesa = (Despesa) value;
			return despesa.getId() == null ? null : despesa.getId().toString();
		}
		return "";
	}

}
