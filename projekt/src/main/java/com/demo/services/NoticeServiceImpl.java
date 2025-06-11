package com.demo.services;


import com.demo.dao.NoticeDAO;
import com.demo.models.Category;
import com.demo.models.Notice;
import com.demo.models.User;
import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Stateless
public class NoticeServiceImpl implements NoticeService {
    @EJB
    private NoticeDAO noticeDao;

    @EJB
    private MessageSender messageSender;

    @Override
    public Notice save(Notice notice) {
        if (notice.getTitle() == null || notice.getTitle().isBlank()) {
            throw new IllegalArgumentException("Nazwa ogłoszenia jest wymagana");
        }
        if (notice.getAuthor() == null) {
            throw new IllegalArgumentException("Autor ogłoszenia jest wymagany");
        }
        if (notice.getCategory() == null) {
            throw new IllegalArgumentException("Kategoria ogłoszenia jest wymagana");
        }

        notice.setPublishDate(new Date());
        notice.setWasModerated(false);

        return noticeDao.save(notice);
    }

    @Override
    public Notice update(Notice notice) {
        Optional<Notice> existing = noticeDao.findById(notice.getId());
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Nie znalezniono ogłoszenia o id: " + notice.getId());
        }

        // Zachowuje oryginalną datę publikacji
        notice.setPublishDate(existing.get().getPublishDate());

        return noticeDao.update(notice);
    }

    @Override
    public void delete(Long id) {
        Optional<Notice> notice = noticeDao.findById(id);
        if (notice.isEmpty()) {
            throw new IllegalArgumentException("Nie znalezniono ogłoszenia o id: " + id);
        }
        noticeDao.delete(id);
    }

    // wykonuje się co godzinę
    @Override
    @Schedule(hour = "*", minute = "0")
    @Transactional
    public void removeExpiredNotices() {
        noticeDao.deleteByTerminationDate();
    }

    @Schedule(hour = "8", minute = "0", persistent = false) 
    public void sendExpirationNotifications() {
        Date tomorrow = Date.from(LocalDate.now()
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

        List<Notice> expiringNotices = noticeDao.findByTerminationDate(tomorrow);

        for (Notice notice : expiringNotices) {
            User author = notice.getAuthor();
            String subject = "Twoje ogłoszenie wygasa jutro";
            String body = "Ogłoszenie: " + notice.getTitle() + " wygaśnie " + notice.getTerminationDate();

            messageSender.send(author.getEmail(), subject, body);
        }
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
