package br.com.sis.convert;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Servico;
import br.com.sis.repository.ServicoRepository;
import br.com.sis.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Servico.class)
public class ServicoConverter implements Converter<Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private ServicoRepository servicoRepository;
	
	public ServicoConverter() {
		servicoRepository = CDIServiceLocator.getBean(ServicoRepository.class);
	}

	@Override
	public Servico getAsObject(FacesContext context, UIComponent component, String value) {
		Servico servico = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = Long.parseLong(value);
			servico = servicoRepository.findById(id);
		}
		return servico;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Servico servico = (Servico) value;
			return servico.getId() == null ? null : servico.getId().toString();
		}
		return "";
	}

}
