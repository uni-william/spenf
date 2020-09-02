package br.com.sis.convert;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Orcamento;
import br.com.sis.repository.OrcamentoRepository;
import br.com.sis.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Orcamento.class)
public class OrcamentoConverter implements Converter<Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private OrcamentoRepository orcamentoRepository;
	
	public OrcamentoConverter() {
		orcamentoRepository = CDIServiceLocator.getBean(OrcamentoRepository.class);
	}

	@Override
	public Orcamento getAsObject(FacesContext context, UIComponent component, String value) {
		Orcamento orcamento = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = Long.parseLong(value);
			orcamento = orcamentoRepository.findById(id);
		}
		return orcamento;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Orcamento orcamento = (Orcamento) value;
			return orcamento.getId() == null ? null : orcamento.getId().toString();
		}
		return "";
	}

}
