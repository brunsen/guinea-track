package de.brunsen.guineatrack.model;

public class GuineaPigOptionalData {
    private int weight;
    private String lastBirth;
    private String origin;
    private String limitations;
    private String castrationDate;
    private String picturePath;

    public GuineaPigOptionalData() {
        this(0, "", "", "", "", "");
    }

    public GuineaPigOptionalData(int weight, String lastBirth, String origin, String limitations, String castrationDate, String picturePath) {
        this.setWeight(weight);
        this.setPicturePath(picturePath);
        this.setLastBirth(lastBirth);
        this.setOrigin(origin);
        this.setCastrationDate(castrationDate);
        this.setLimitations(limitations);
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getLastBirth() {
        return lastBirth;
    }

    public void setLastBirth(String lastBirth) {
        this.lastBirth = lastBirth;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getLimitations() {
        return limitations;
    }

    public void setLimitations(String limitations) {
        this.limitations = limitations;
    }

    public String getCastrationDate() {
        return castrationDate;
    }

    public void setCastrationDate(String castrationDate) {
        this.castrationDate = castrationDate;
    }
}
