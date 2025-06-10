package com.demo.dao;

import com.demo.models.Category;
import com.demo.models.Notice;
import com.demo.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ejb.Stateless;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Stateless
public class NoticeDAOImpl extends AbstractDAOImpl<Notice> implements NoticeDAO{
    private static final Logger logger = LogManager.getLogger(NoticeDAOImpl.class);

    @Override
    public List<Notice> findByTitle(String title) {
        logger.debug("Szukanie ogłoszeń o nazwie: {}", title);
        return em.createQuery(
                        "SELECT n FROM Notice n WHERE n.title = :title", Notice.class)
                .setParameter("title", title)
                .getResultList();
    }

    @Override
    public List<Notice> findByPublishedDate(Date publishedDate){
        LocalDate localDate = publishedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        logger.debug("Szukanie ogłoszeń stworzonych dnia: {}", localDate);

        Date startOfDay = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date nextDay = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        return em.createQuery(
                        "SELECT n FROM Notice n WHERE n.publishDate >= :startOfDay AND n.publishDate < :nextDay", Notice.class)
                .setParameter("startOfDay", startOfDay)
                .setParameter("nextDay", nextDay)
                .getResultList();
    }

    @Override
    public List<Notice> findByAuthor(User author) {
        logger.debug("Szukanie ogłoszeń po autorze: {} {}", author.getName(), author.getSurname());
        return em.createQuery(
                        "SELECT n FROM Notice n WHERE n.author = :author", Notice.class)
                .setParameter("author", author)
                .getResultList();
    }

    @Override
    public List<Notice> findByCategory(Category category) {
        logger.debug("Szukanie ogłoszeń po kategorii: {}", category.getName());
        return em.createQuery(
                        "SELECT n FROM Notice n WHERE n.category = :category", Notice.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public List<Notice> findModerated() {
        logger.debug("Szukanie ogłoszeń po moderacji: ");
        return em.createQuery(
                        "SELECT n FROM Notice n WHERE n.wasModerated = true ", Notice.class)
                .getResultList();
    }

}
