package com.demo.services;

import com.demo.models.Category;
import com.demo.models.Notice;
import com.demo.models.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface NoticeService {
    Notice save(Notice notice);
    Notice update(Notice notice);
    void delete(Long id);
    Optional<Notice> findById(Long id);
    List<Notice> findAll();
    List<Notice> findByTitle(String title);
    List<Notice> findByPublishedDate(Date publishedDate);
    List<Notice> findByAuthor(User author);
    List<Notice> findByCategory(Category category);
    List<Notice> findModerated();
    List<Notice> findNotModerated();
    List<Notice> findModeratedByCategory(Category category);
}
