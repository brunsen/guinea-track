package de.brunsen.guineatrack.overview;

public interface OverViewView {

    void refreshList();

    void setGenderText(String text);

    void showErrorDialog(String title, String message, String okMessage);
}
