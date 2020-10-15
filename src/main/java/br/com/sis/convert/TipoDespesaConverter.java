package br.com.sis.convert;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.TipoDespesa;
import br.com.sis.repository.TipoDespesaRepository;
import br.com.sis.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = TipoDespesa.class)
public class TipoDespesaConverter implements Converter<Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private TipoDespesaRepository tipoDespesaRepository;
	
	public TipoDespesaConverter() {
		tipoDespesaRepository = CDIServiceLocator.getBean(TipoDespesaRepository.class);
	}

	@Override
	public TipoDespesa getAsObject(FacesContext context, UIComponent component, String value) {
		TipoDespesa tipoDespesa = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = Long.parseLong(value);
			tipoDespesa = tipoDespesaRepository.findById(id);
		}
		return tipoDespesa;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			TipoDespesa tipoDespesa = (TipoDespesa) value;
			return tipoDespesa.getId() == null ? null : tipoDespesa.getId().toString();
		}
		return "";
	}

}
