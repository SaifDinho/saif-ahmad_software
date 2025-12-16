package com.example.library.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a media item in the Library Management System.
 * Contains information about books, CDs, and other media that can be borrowed.
 * Tracks availability, copy counts, and late fee information.
 * 
 * @author Library System Team
 * @version 1.0
 */
public class MediaItem {
    private Integer itemId;
    private String title;
    private String author;
    private String type;
    private String isbn;
    private LocalDate publicationDate;
    private String publisher;
    private Integer totalCopies;
    private Integer availableCopies;
    private BigDecimal lateFeesPerDay;
    
    /**
     * Default constructor for creating a new MediaItem instance.
     * Required for frameworks and ORM mapping.
     */
    public MediaItem() {
    }
    
    /**
     * Constructs a new MediaItem with all specified properties.
     * 
     * @param itemId the unique identifier for the media item
     * @param title the title of the media item
     * @param author the author or creator of the media item
     * @param type the type of media (BOOK, CD, DVD, etc.)
     * @param isbn the ISBN number (for books)
     * @param publicationDate the date when the item was published
     * @param publisher the publisher of the media item
     * @param totalCopies the total number of copies available
     * @param availableCopies the number of copies currently available for borrowing
     * @param lateFeesPerDay the late fee charged per day for overdue returns
     */
    public MediaItem(Integer itemId, String title, String author, String type, String isbn, 
                     LocalDate publicationDate, String publisher, Integer totalCopies, 
                     Integer availableCopies, BigDecimal lateFeesPerDay) {
        this.itemId = itemId;
        this.title = title;
        this.author = author;
        this.type = type;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.lateFeesPerDay = lateFeesPerDay;
    }
    
    /**
     * Gets the unique identifier for this media item.
     * 
     * @return the item ID, or null if not yet saved to database
     */
    public Integer getItemId() {
        return itemId;
    }
    
    /**
     * Sets the unique identifier for this media item.
     * Typically used by repository when saving to database.
     * 
     * @param itemId the item ID to set
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    
    /**
     * Gets the title of this media item.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title of this media item.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Gets the author or creator of this media item.
     * 
     * @return the author
     */
    public String getAuthor() {
        return author;
    }
    
    /**
     * Sets the author or creator of this media item.
     * 
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    
    /**
     * Gets the type of this media item.
     * 
     * @return the type (BOOK, CD, DVD, etc.)
     */
    public String getType() {
        return type;
    }
    
    /**
     * Sets the type of this media item.
     * 
     * @param type the type to set (BOOK, CD, DVD, etc.)
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Gets the ISBN number of this media item.
     * Primarily used for books.
     * 
     * @return the ISBN number, or null if not applicable
     */
    public String getIsbn() {
        return isbn;
    }
    
    /**
     * Sets the ISBN number of this media item.
     * Primarily used for books.
     * 
     * @param isbn the ISBN number to set, or null if not applicable
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    /**
     * Gets the publication date of this media item.
     * 
     * @return the publication date, or null if not specified
     */
    public LocalDate getPublicationDate() {
        return publicationDate;
    }
    
    /**
     * Sets the publication date of this media item.
     * 
     * @param publicationDate the publication date to set, or null if not specified
     */
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
    
    /**
     * Gets the publisher of this media item.
     * 
     * @return the publisher, or null if not specified
     */
    public String getPublisher() {
        return publisher;
    }
    
    /**
     * Sets the publisher of this media item.
     * 
     * @param publisher the publisher to set, or null if not specified
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    /**
     * Gets the total number of copies of this media item.
     * 
     * @return the total number of copies
     */
    public Integer getTotalCopies() {
        return totalCopies;
    }
    
    /**
     * Sets the total number of copies of this media item.
     * 
     * @param totalCopies the total number of copies to set
     */
    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }
    
    /**
     * Gets the number of copies currently available for borrowing.
     * 
     * @return the number of available copies
     */
    public Integer getAvailableCopies() {
        return availableCopies;
    }
    
    /**
     * Sets the number of copies currently available for borrowing.
     * This value should not exceed totalCopies.
     * 
     * @param availableCopies the number of available copies to set
     */
    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }
    
    /**
     * Gets the late fee charged per day for overdue returns.
     * 
     * @return the late fee per day
     */
    public BigDecimal getLateFeesPerDay() {
        return lateFeesPerDay;
    }
    
    /**
     * Sets the late fee charged per day for overdue returns.
     * 
     * @param lateFeesPerDay the late fee per day to set
     */
    public void setLateFeesPerDay(BigDecimal lateFeesPerDay) {
        this.lateFeesPerDay = lateFeesPerDay;
    }
    
    /**
     * Returns a string representation of this media item.
     * 
     * @return string representation with item details
     */
    @Override
    public String toString() {
        return "MediaItem{" +
                "itemId=" + itemId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", type='" + type + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publicationDate=" + publicationDate +
                ", publisher='" + publisher + '\'' +
                ", totalCopies=" + totalCopies +
                ", availableCopies=" + availableCopies +
                ", lateFeesPerDay=" + lateFeesPerDay +
                '}';
    }
}
