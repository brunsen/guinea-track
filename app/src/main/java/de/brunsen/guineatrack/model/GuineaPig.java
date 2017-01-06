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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuineaPig guineaPig = (GuineaPig) o;

        if (id != guineaPig.id) return false;
        if (!name.equals(guineaPig.name)) return false;
        if (!birth.equals(guineaPig.birth)) return false;
        if (gender != guineaPig.gender) return false;
        if (!color.equals(guineaPig.color)) return false;
        if (!breed.equals(guineaPig.breed)) return false;
        if (type != guineaPig.type) return false;
        return guineaPigOptionalData.equals(guineaPig.guineaPigOptionalData);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + birth.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + color.hashCode();
        result = 31 * result + breed.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + guineaPigOptionalData.hashCode();
        return result;
    }
}
