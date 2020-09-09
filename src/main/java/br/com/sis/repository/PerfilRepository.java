package br.com.sis.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.sis.entity.Perfil;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class PerfilRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Perfil findById(Long id) {
		return manager.find(Perfil.class, id);
	}
	
	public List<Perfil> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Perfil> criteriaQuery = builder.createQuery(Perfil.class);
		Root<Perfil> root = criteriaQuery.from(Perfil.class);
		criteriaQuery.select(root);
		TypedQuery<Perfil> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public Perfil salvar(Perfil perfil) {
		return manager.merge(perfil);
	}

	@Transactional
	public boolean remover(Perfil perfil) {
		try {
			perfil = findById(perfil.getId());
			manager.remove(perfil);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("Perfil não pode ser excluído.");
			return false;
		}
	}	
	
	public Perfil findByDescricao(String descricao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Perfil> criteriaQuery = builder.createQuery(Perfil.class);
		Root<Perfil> root = criteriaQuery.from(Perfil.class);
		criteriaQuery.select(root);
		criteriaQuery.where(builder.equal(builder.lower(root.get("descricao")), descricao.toLowerCase()));
		TypedQuery<Perfil> query = manager.createQuery(criteriaQuery);
		try {
			return query.getSingleResult();
		} catch(NoResultException e) {
			return null;
		}
	}

}
