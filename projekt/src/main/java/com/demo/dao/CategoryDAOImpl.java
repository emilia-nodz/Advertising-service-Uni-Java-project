package com.demo.dao;

import com.demo.models.Category;
import com.demo.models.Notice;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Stateless
public class CategoryDAOImpl extends AbstractDAOImpl<Category> implements CategoryDAO{
    private static final Logger logger = LogManager.getLogger(NoticeDAOImpl.class);

    @Override
    public Category findByName(String name) {
        logger.debug("Szukanie kategorii o nazwie: {}", name);
        return em.createQuery(
                        "SELECT c FROM Category c WHERE c.name = :name", Category.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
