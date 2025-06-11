package com.demo.services;


import com.demo.dao.NoticeDAO;
import com.demo.dao.NoticeDAOImpl;
import com.demo.models.Category;
import com.demo.models.Notice;
import com.demo.models.User;
import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Stateless
public class NoticeServiceImpl implements NoticeService {
    private static final Logger logger = LogManager.getLogger(NoticeServiceImpl.class);

    @EJB
    private NoticeDAO noticeDao;

    @EJB
    private MessageSender messageSender;

    @Override
    public Notice save(Notice notice) {
        logger.info("Próba zapisu ogłoszenia o tytule: {}", notice.getTitle());
        if (notice.getTitle() == null || notice.getTitle().isBlank()) {
            logger.error("Nazwa ogłoszenia jest wymagana");
            throw new IllegalArgumentException("Nazwa ogłoszenia jest wymagana");
        }
        if (notice.getAuthor() == null) {
            logger.error("Autor ogłoszenia jest wymagany");
            throw new IllegalArgumentException("Autor ogłoszenia jest wymagany");
        }
        if (notice.getCategory() == null) {
            logger.error("Kategoria ogłoszenia jest wymagana");
            throw new IllegalArgumentException("Kategoria ogłoszenia jest wymagana");
        }

        notice.setPublishDate(new Date());
        notice.setWasModerated(false);

        return noticeDao.save(notice);
    }

    @Override
    public Notice update(Notice notice) {
        logger.info("Próba edycji ogłoszenia o tytule: {}", notice.getTitle());

        Optional<Notice> existing = noticeDao.findById(notice.getId());
        if (existing.isEmpty()) {
            logger.error("Nie znaleziono ogłoszenia o id: {}", notice.getId());
            throw new IllegalArgumentException("Nie znalezniono ogłoszenia o id: " + notice.getId());
        }

        // Zachowuje oryginalną datę publikacji
        notice.setPublishDate(existing.get().getPublishDate());

        return noticeDao.update(notice);
    }

    @Override
    public void delete(Long id) {
        logger.info("Próba usunięcia ogłoszenia o id: {}", id);

        Optional<Notice> notice = noticeDao.findById(id);
        if (notice.isEmpty()) {
            logger.error("Nie znaleziono ogłoszenia o id: {}", id);
            throw new IllegalArgumentException("Nie znalezniono ogłoszenia o id: " + id);
        }
        noticeDao.delete(id);
    }

    // wykonuje się co godzinę
    @Override
    @Schedule(hour = "*", minute = "0")
    @Transactional
    public void removeExpiredNotices() {
        logger.info("Rozpoczęcie usuwania wygasłych ogłoszeń");
        noticeDao.deleteByTerminationDate();
        logger.info("Zakończenie usuwania wygasłych ogłoszeń");
    }

    @Schedule(hour = "12", minute = "0", persistent = true)
    public void sendExpirationNotifications() {
        logger.info("Rozpoczęcie wysyłania maili o wygaśnięciu ogłoszeń");

        Date tomorrow = Date.from(LocalDate.now()
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

        List<Notice> expiringNotices = noticeDao.findByTerminationDate(tomorrow);
        logger.info("Znaleziono {} ogłoszeń do usunięcia",  expiringNotices.size());

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));

        for (Notice notice : expiringNotices) {
            User author = notice.getAuthor();
            String subject = "Twoje ogłoszenie wygasa jutro";
            String formattedDate = formatter.format(notice.getTerminationDate());

            String body = "Ogłoszenie: " + notice.getTitle() +
                    " wygaśnie " + formattedDate;

            messageSender.send(author.getEmail(), subject, body);
        }
        logger.info("Zakończenie wysyłania maili o wygaśnięciu ogłoszeń");
    }

    @Override
    public Optional<Notice> findById(Long id) {
        return noticeDao.findById(id);
    }

    @Override
    public List<Notice> findAll() {
        return noticeDao.findAll();
    }

    @Override
    public List<Notice> findModerated() {
        return noticeDao.findModerated();
    }

    @Override
    public List<Notice> findNotModerated() {
        return noticeDao.findNotModerated();
    }

    @Override
    public List<Notice> findByTitle(String title) {
        return noticeDao.findByTitle(title);
    }

    @Override
    public List<Notice> findByPublishedDate(Date publishedDate) {
        return noticeDao.findByPublishedDate(publishedDate);
    }

    @Override
    public List<Notice> findByAuthor(User author) {
        return noticeDao.findByAuthor(author);
    }

    @Override
    public List<Notice> findByCategory(Category category) {
        return noticeDao.findByCategory(category);
    }

    @Override
    public List<Notice> findModeratedByCategory(Category category) {
        return noticeDao.findModeratedByCategory(category);
    }

    @Override
    public List<Notice> findModeratedByUser(User user) {
        return noticeDao.findModeratedByUser(user);
    }

    @Override
    public List<Notice> findNotModeratedByUser(User user) {
        return noticeDao.findNotModeratedByUser(user);
    }
}
