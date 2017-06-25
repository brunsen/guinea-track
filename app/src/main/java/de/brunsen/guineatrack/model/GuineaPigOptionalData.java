package de.brunsen.guineatrack.model;

public class GuineaPigOptionalData {
    private int weight;
    private String lastBirth;
    private String dueDate;
    private String origin;
    private String limitations;
    private String castrationDate;
    private String picturePath;
    private String entry;
    private String departure;

    public GuineaPigOptionalData() {
        this(0, "", "", "", "", "", "", "", "");
    }

    public GuineaPigOptionalData(int weight, String lastBirth, String dueDate, String origin,
                                 String limitations, String castrationDate, String picturePath,
                                 String entry, String departure) {
        this.setWeight(weight);
        this.setLastBirth(lastBirth);
        this.setDueDate(dueDate);
        this.setOrigin(origin);
        this.setLimitations(limitations);
        this.setCastrationDate(castrationDate);
        this.setPicturePath(picturePath);
        this.setEntry(entry);
        this.setDeparture(departure);
    }

    public GuineaPigOptionalData(GuineaPigOptionalData optionalData) {
        this(optionalData.getWeight(), optionalData.getLastBirth(), optionalData.getDueDate(),
                optionalData.getOrigin(), optionalData.getLimitations(), optionalData.getCastrationDate(),
                optionalData.getPicturePath(), optionalData.getEntry(), optionalData.getDeparture());
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

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuineaPigOptionalData)) return false;

        GuineaPigOptionalData that = (GuineaPigOptionalData) o;

        if (getWeight() != that.getWeight()) return false;
        if (getLastBirth() != null ? !getLastBirth().equals(that.getLastBirth()) : that.getLastBirth() != null)
            return false;
        if (getDueDate() != null ? !getDueDate().equals(that.getDueDate()) : that.getDueDate() != null)
            return false;
        if (getOrigin() != null ? !getOrigin().equals(that.getOrigin()) : that.getOrigin() != null)
            return false;
        if (getLimitations() != null ? !getLimitations().equals(that.getLimitations()) : that.getLimitations() != null)
            return false;
        if (getCastrationDate() != null ? !getCastrationDate().equals(that.getCastrationDate()) : that.getCastrationDate() != null)
            return false;
        if (getPicturePath() != null ? !getPicturePath().equals(that.getPicturePath()) : that.getPicturePath() != null)
            return false;
        if (getEntry() != null ? !getEntry().equals(that.getEntry()) : that.getEntry() != null)
            return false;
        return getDeparture() != null ? getDeparture().equals(that.getDeparture()) : that.getDeparture() == null;

    }

    @Override
    public int hashCode() {
        int result = getWeight();
        result = 31 * result + (getLastBirth() != null ? getLastBirth().hashCode() : 0);
        result = 31 * result + (getDueDate() != null ? getDueDate().hashCode() : 0);
        result = 31 * result + (getOrigin() != null ? getOrigin().hashCode() : 0);
        result = 31 * result + (getLimitations() != null ? getLimitations().hashCode() : 0);
        result = 31 * result + (getCastrationDate() != null ? getCastrationDate().hashCode() : 0);
        result = 31 * result + (getPicturePath() != null ? getPicturePath().hashCode() : 0);
        result = 31 * result + (getEntry() != null ? getEntry().hashCode() : 0);
        result = 31 * result + (getDeparture() != null ? getDeparture().hashCode() : 0);
        return result;
    }
}
