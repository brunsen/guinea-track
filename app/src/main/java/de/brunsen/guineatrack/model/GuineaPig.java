package de.brunsen.guineatrack.model;

public class GuineaPig {

    private int id;
    private String name;
    private String birth;
    private Gender gender;
    private String color;
    private String breed;
    private Type type;
    private GuineaPigOptionalData guineaPigOptionalData;

    public GuineaPig() {
        this(0, "", "", Gender.CASTRATO, "", "", Type.RESCUE, new GuineaPigOptionalData());
    }

    public GuineaPig(String name, String birthday, Gender g, String color, String breed, Type t, GuineaPigOptionalData optionalData) {
        this(0, name, birthday, g, color, breed, t, optionalData);
    }

    public GuineaPig(int id, String name, String birthday, Gender g, String color, String breed, Type t, GuineaPigOptionalData optionalData) {
        this.setId(id);
        this.setName(name);
        this.setBirth(birthday);
        this.setGender(g);
        this.setColor(color);
        this.setBreed(breed);
        this.setType(t);
        this.setOptionalData(optionalData);
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public GuineaPigOptionalData getOptionalData() {
        return guineaPigOptionalData;
    }

    public void setOptionalData(GuineaPigOptionalData guineaPigOptionalData) {
        this.guineaPigOptionalData = guineaPigOptionalData;
    }
}
