package de.brunsen.guineatrack.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GuineaPig implements Comparable<GuineaPig>, Parcelable {

    private int id;
    private String name;
    private String birth;
    private Gender gender;
    private String color;
    private String race;
    private Type type;
    private String picturePath;
    private String lastBirth;

    public static final Creator<GuineaPig> CREATOR = new Creator<GuineaPig>() {

        @Override
        public GuineaPig createFromParcel(Parcel source) {
            return new GuineaPig(source);
        }

        @Override
        public GuineaPig[] newArray(int size) {
            return new GuineaPig[size];
        }
    };

    public GuineaPig() {
        // Empty constructor for gson
    }

    public GuineaPig(Parcel in) {
        this.setName(in.readString());
        this.setBirth(in.readString());
        this.setRace(in.readString());
        this.setColor(in.readString());
        this.setGender(Gender.fromInt(in.readInt()));
        this.setId(in.readInt());
        this.setType(Type.fromInt(in.readInt()));
        this.setPicturePath(in.readString());
        this.setLastBirth(in.readString());
    }

    public GuineaPig(String name, String birthday, Gender g, String color,
                     String race, Type t, String imgPath, String lastBirth) {
        this(0, name, birthday, g, color, race, t, imgPath, lastBirth);
    }

    public GuineaPig(int id, String name, String birthday, Gender g,
                     String color, String race, Type t, String imgPath, String lastBirth) {
        this.setId(id);
        this.setName(name);
        this.setBirth(birthday);
        this.setGender(g);
        this.setColor(color);
        this.setRace(race);
        this.setType(t);
        this.setPicturePath(imgPath);
        this.setLastBirth(lastBirth);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    @Override
    public int compareTo(GuineaPig another) {
        String currentRace = getRace().replaceAll("\\s+","");
        String anotherRace = another.getRace().replaceAll("\\s+","");
        int compareResult = currentRace.compareToIgnoreCase(anotherRace);
        if (compareResult == 0) {
            compareResult = getName().compareToIgnoreCase(another.getName());
        }
        return compareResult;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeString(getBirth());
        dest.writeString(getRace());
        dest.writeString(getColor());
        dest.writeInt(getGender().getPosition());
        dest.writeInt(getId());
        dest.writeInt(getType().getPosition());
        dest.writeString(getPicturePath());
        dest.writeString(getLastBirth());
    }

    public String getLastBirth() {
        return lastBirth;
    }

    public void setLastBirth(String lastBirth) {
        this.lastBirth = lastBirth;
    }
}
