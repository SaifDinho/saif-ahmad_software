package com.example.library.repository;

import com.example.library.domain.MediaItem;

import java.util.*;

public class FileMediaItemRepository implements MediaItemRepository {

    private final Map<Integer, MediaItem> storage = new HashMap<>();
    private int nextId = 1;

    @Override
    public MediaItem save(MediaItem mediaItem) {
        if (mediaItem.getItemId() == null) {
            mediaItem.setItemId(nextId++);
        }
        storage.put(mediaItem.getItemId(), cloneItem(mediaItem));
        return mediaItem;
    }

    @Override
    public MediaItem update(MediaItem mediaItem) {
        if (mediaItem.getItemId() == null || !storage.containsKey(mediaItem.getItemId())) {
            return save(mediaItem);
        }
        storage.put(mediaItem.getItemId(), cloneItem(mediaItem));
        return mediaItem;
    }

    @Override
    public Optional<MediaItem> findById(Integer itemId) {
        MediaItem item = storage.get(itemId);
        return Optional.ofNullable(item == null ? null : cloneItem(item));
    }

    @Override
    public Optional<MediaItem> findByIsbn(String isbn) {
        return storage.values().stream()
                .filter(i -> Objects.equals(i.getIsbn(), isbn))
                .findFirst()
                .map(this::cloneItem);
    }

    @Override
    public List<MediaItem> findAll() {
        List<MediaItem> result = new ArrayList<>();
        for (MediaItem item : storage.values()) {
            result.add(cloneItem(item));
        }
        return result;
    }

    @Override
    public List<MediaItem> findByType(String type) {
        List<MediaItem> result = new ArrayList<>();
        for (MediaItem item : storage.values()) {
            if (Objects.equals(item.getType(), type)) {
                result.add(cloneItem(item));
            }
        }
        return result;
    }

    @Override
    public List<MediaItem> findByTitleContaining(String title) {
        List<MediaItem> result = new ArrayList<>();
        for (MediaItem item : storage.values()) {
            if (item.getTitle() != null && item.getTitle().contains(title)) {
                result.add(cloneItem(item));
            }
        }
        return result;
    }

    @Override
    public List<MediaItem> findByAuthorContaining(String author) {
        List<MediaItem> result = new ArrayList<>();
        for (MediaItem item : storage.values()) {
            if (item.getAuthor() != null && item.getAuthor().contains(author)) {
                result.add(cloneItem(item));
            }
        }
        return result;
    }

    @Override
    public List<MediaItem> findAvailableItems() {
        List<MediaItem> result = new ArrayList<>();
        for (MediaItem item : storage.values()) {
            if (item.getAvailableCopies() != null && item.getAvailableCopies() > 0) {
                result.add(cloneItem(item));
            }
        }
        return result;
    }

    @Override
    public boolean deleteById(Integer itemId) {
        return storage.remove(itemId) != null;
    }

    @Override
    public void updateAvailableCopies(Integer itemId, Integer availableCopies) {
        MediaItem item = storage.get(itemId);
        if (item != null) {
            item.setAvailableCopies(availableCopies);
        }
    }

    @Override
    public List<MediaItem> search(String keyword) {
        List<MediaItem> result = new ArrayList<>();
        for (MediaItem item : storage.values()) {
            if (item.getTitle() != null && item.getTitle().contains(keyword)) {
                result.add(cloneItem(item));
                continue;
            }
            if (item.getAuthor() != null && item.getAuthor().contains(keyword)) {
                result.add(cloneItem(item));
                continue;
            }
            if (item.getIsbn() != null && item.getIsbn().contains(keyword)) {
                result.add(cloneItem(item));
                continue;
            }
            if (item.getType() != null && item.getType().contains(keyword)) {
                result.add(cloneItem(item));
            }
        }
        return result;
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return findByIsbn(isbn).isPresent();
    }

    private MediaItem cloneItem(MediaItem original) {
        MediaItem copy = new MediaItem();
        copy.setItemId(original.getItemId());
        copy.setTitle(original.getTitle());
        copy.setAuthor(original.getAuthor());
        copy.setType(original.getType());
        copy.setIsbn(original.getIsbn());
        copy.setPublicationDate(original.getPublicationDate());
        copy.setPublisher(original.getPublisher());
        copy.setTotalCopies(original.getTotalCopies());
        copy.setAvailableCopies(original.getAvailableCopies());
        copy.setLateFeesPerDay(original.getLateFeesPerDay());
        return copy;
    }
}
