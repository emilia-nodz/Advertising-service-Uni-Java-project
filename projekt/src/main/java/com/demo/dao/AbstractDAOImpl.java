package com.demo.dao;

import com.demo.models.AbstractModel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class AbstractDAOImpl<T extends AbstractModel> implements AbstractDAO<T> {

    @PersistenceContext(unitName = "myPU")
    protected EntityManager em;

    private final Class<T> type;
    private static final Logger logger = LogManager.getLogger(AbstractDAOImpl.class);

    public AbstractDAOImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }

    @Override
    public T save(T t) {
        if (t.getId() == null) {
            logger.debug("Tworzenie nowego obiektu");
            em.persist(t);
            return t;
        }
        logger.debug("Zapisanie zmian obiektu");
        return em.merge(t);
    }

    @Override
    public T update(T t) {
        if (t.getId() == null) {
            throw new IllegalArgumentException("Nie można zaktualizować obiektu bez id");
        }
        logger.debug("Aktualizacja obiektu z id: {}", t.getId());
        return em.merge(t);
    }

    @Override
    public void delete(Long id) {
        logger.debug("Usuwanie obiektu z id = {}", id);
        T t = em.getReference(type, id);
        em.remove(t);
        em.flush();
    }

    @Override
    public Optional<T> findById(Long id) {
        logger.debug("Wyszukiwanie obiektu o id = {}", id);
        T dto = em.find(type, id);
        return Optional.ofNullable(dto);
    }

    @Override
    public List<T> findAll() {
        logger.debug("Wyszukiwanie wszystkich obiektów");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(type);
        Root<T> rootEntry = cq.from(type);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    public List<T> findList(String queryName, Object... params) {
        TypedQuery<T> query = em.createNamedQuery(queryName, type);
        if (params.length > 0) {
            for (int i = 0; i < params.length - 1; i += 2) {
                query.setParameter((String) params[i], params[i + 1]);
            }
        }
        List<T> result = query.getResultList();
        return result;
    }

    public Optional<T> findSingle(String queryName, Object... params) {
        TypedQuery<T> query = em.createNamedQuery(queryName, type);
        try {
            if (params.length > 0) {
                for (int i = 0; i < params.length - 1; i += 2) {
                    query.setParameter((String) params[i], params[i + 1]);
                }
            }
            T e = query.getSingleResult();
            return Optional.of(e);
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }
}
