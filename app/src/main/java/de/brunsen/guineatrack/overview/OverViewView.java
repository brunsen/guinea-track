package de.brunsen.guineatrack.overview;

public interface OverViewView {

    public void refreshList();

    public void setGenderText(String text);

    public void showErrorDialog(String title, String message, String okMessage);
}
