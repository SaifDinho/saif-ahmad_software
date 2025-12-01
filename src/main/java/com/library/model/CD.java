package com.library.model;

import java.io.Serializable;

public class CD implements Serializable {
    private static final long serialVersionUID = 1L;

    private int cdId;
    private String title;
    private String artist;
    private String catalogNumber;
    private int quantityTotal;
    private int quantityAvailable;
    private double dailyFineRate;

    public CD() {
    }

    public CD(int cdId, String title, String artist, String catalogNumber, int quantityTotal, int quantityAvailable, double dailyFineRate) {
        this.cdId = cdId;
        this.title = title;
        this.artist = artist;
        this.catalogNumber = catalogNumber;
        this.quantityTotal = quantityTotal;
        this.quantityAvailable = quantityAvailable;
        this.dailyFineRate = dailyFineRate;
    }

    public int getCdId() {
        return cdId;
    }

    public void setCdId(int cdId) {
        this.cdId = cdId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public int getQuantityTotal() {
        return quantityTotal;
    }

    public void setQuantityTotal(int quantityTotal) {
        this.quantityTotal = quantityTotal;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public double getDailyFineRate() {
        return dailyFineRate;
    }

    public void setDailyFineRate(double dailyFineRate) {
        this.dailyFineRate = dailyFineRate;
    }

    @Override
    public String toString() {
        return "CD{" +
                "cdId=" + cdId +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", catalogNumber='" + catalogNumber + '\'' +
                ", quantityTotal=" + quantityTotal +
                ", quantityAvailable=" + quantityAvailable +
                ", dailyFineRate=" + dailyFineRate +
                '}';
    }
}
