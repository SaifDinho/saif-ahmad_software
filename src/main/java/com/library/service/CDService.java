package com.library.service;

import com.library.model.CD;
import com.library.repository.CDRepository;
import com.library.repository.CDRepositoryImpl;
import java.util.List;

public class CDService {
    private CDRepository cdRepository;

    public CDService() {
        this.cdRepository = new CDRepositoryImpl();
    }

    public CDService(CDRepository cdRepository) {
        this.cdRepository = cdRepository;
    }

    public void addCD(CD cd) {
        if (cd == null || cd.getTitle() == null || cd.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("CD title cannot be empty");
        }
        if (cd.getArtist() == null || cd.getArtist().trim().isEmpty()) {
            throw new IllegalArgumentException("CD artist cannot be empty");
        }
        if (cd.getQuantityTotal() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (cd.getDailyFineRate() < 0) {
            throw new IllegalArgumentException("Daily fine rate cannot be negative");
        }

        cdRepository.save(cd);
    }

    public void updateCD(CD cd) {
        if (cd == null || cd.getCdId() <= 0) {
            throw new IllegalArgumentException("Invalid CD");
        }
        cdRepository.update(cd);
    }

    public void deleteCD(int cdId) {
        cdRepository.delete(cdId);
    }

    public CD getCD(int cdId) {
        return cdRepository.findById(cdId);
    }

    public List<CD> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return cdRepository.findAll();
        }
        return cdRepository.findByTitle(title);
    }

    public List<CD> searchByArtist(String artist) {
        if (artist == null || artist.trim().isEmpty()) {
            return cdRepository.findAll();
        }
        return cdRepository.findByArtist(artist);
    }

    public List<CD> getAllCDs() {
        return cdRepository.findAll();
    }
}
