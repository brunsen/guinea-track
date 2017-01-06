package de.brunsen.guineatrack.model;

public class GuineaPigOptionalData {
    private int weight;
    private String lastBirth;
    private String dueDate;
    private String origin;
    private String limitations;
    private String castrationDate;
    private String picturePath;

    public GuineaPigOptionalData() {
        this(0, "", "", "", "", "", "");
    }

    public GuineaPigOptionalData(int weight, String lastBirth, String dueDate, String origin, String limitations, String castrationDate, String picturePath) {
        this.setWeight(weight);
        this.setLastBirth(lastBirth);
        this.setDueDate(dueDate);
        this.setOrigin(origin);
        this.setLimitations(limitations);
        this.setCastrationDate(castrationDate);
        this.setPicturePath(picturePath);
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuineaPigOptionalData that = (GuineaPigOptionalData) o;

        if (weight != that.weight) return false;
        if (!lastBirth.equals(that.lastBirth)) return false;
        if (!dueDate.equals(that.dueDate)) return false;
        if (!origin.equals(that.origin)) return false;
        if (!limitations.equals(that.limitations)) return false;
        if (!castrationDate.equals(that.castrationDate)) return false;
        return picturePath.equals(that.picturePath);

    }

    @Override
    public int hashCode() {
        int result = weight;
        result = 31 * result + lastBirth.hashCode();
        result = 31 * result + dueDate.hashCode();
        result = 31 * result + origin.hashCode();
        result = 31 * result + limitations.hashCode();
        result = 31 * result + castrationDate.hashCode();
        result = 31 * result + picturePath.hashCode();
        return result;
    }
}
