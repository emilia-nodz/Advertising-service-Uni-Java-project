package com.demo.dao;

import com.demo.models.Category;
import com.demo.models.Notice;
import com.demo.models.User;

import java.util.Date;
import java.util.List;

public interface NoticeDAO extends AbstractDAO<Notice> {
    List<Notice> findByTitle(String title);
    List<Notice> findByPublishedDate(Date publishedDate);
    List<Notice> findByAuthor(User author);
    List<Notice> findByCategory(Category category);
    List<Notice> findModerated();
    List<Notice> findNotModerated();
    List<Notice> findModeratedByCategory(Category category);
}
