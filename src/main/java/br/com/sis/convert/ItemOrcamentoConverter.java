package br.com.sis.convert;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.ItemOrcamento;
import br.com.sis.repository.ItemOrcamentoRepository;
import br.com.sis.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = ItemOrcamento.class)
public class ItemOrcamentoConverter implements Converter<Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private ItemOrcamentoRepository itemOrcamentoRepository;
	
	public ItemOrcamentoConverter() {
		itemOrcamentoRepository = CDIServiceLocator.getBean(ItemOrcamentoRepository.class);
	}

	@Override
	public ItemOrcamento getAsObject(FacesContext context, UIComponent component, String value) {
		ItemOrcamento itemOrcamento = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = Long.parseLong(value);
			itemOrcamento = itemOrcamentoRepository.findById(id);
		}
		return itemOrcamento;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			ItemOrcamento itemOrcamento = (ItemOrcamento) value;
			return itemOrcamento.getId() == null ? null : itemOrcamento.getId().toString();
		}
		return "";
	}

}
