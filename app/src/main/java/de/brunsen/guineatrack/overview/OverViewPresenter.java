package de.brunsen.guineatrack.overview;

import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.util.BasePresenter;

public interface OverViewPresenter extends BasePresenter {

    void onResume();

    void onItemClick(int position);

    void onItemLongClick(int position);

    int getItemCount();

    GuineaPig getGuineaPig(int position);
}
