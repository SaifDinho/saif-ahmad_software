package com.library.repository;

import com.library.model.CD;
import java.util.List;

public interface CDRepository {
    void save(CD cd);
    void update(CD cd);
    void delete(int cdId);
    CD findById(int cdId);
    List<CD> findByTitle(String title);
    List<CD> findByArtist(String artist);
    List<CD> findAll();
}
