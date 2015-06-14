package de.brunsen.guineatrack.model;

public class GuineaPig {

    private int id;
    private String name;
    private String birth;
    private Gender gender;
    private String color;
    private String race;
    private Type type;
    private String picturePath;
    private String lastBirth;

    public GuineaPig() {
        id = 0;
        name = "";
        birth = "";
        gender = Gender.CASTRATO;
        color = "";
        race = "";
        type = Type.RESCUE;
        picturePath = "";
        lastBirth = "";
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

    public String getLastBirth() {
        return lastBirth;
    }

    public void setLastBirth(String lastBirth) {
        this.lastBirth = lastBirth;
    }
}
