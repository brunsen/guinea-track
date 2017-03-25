package de.brunsen.guineatrack.detail;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.ui.activities.BaseActivity;
import de.brunsen.guineatrack.util.ImageService;

public class GuineaPigDetailActivity extends BaseActivity implements GuineaPigDetailView {

    @BindView(R.id.detail_image)
    protected ImageView detailImageView;

    @BindView(R.id.detail_name_text)
    protected TextView nameText;

    @BindView(R.id.detail_birth_text)
    protected TextView birthText;

    @BindView(R.id.detail_weight_text)
    protected TextView weightText;

    @BindView(R.id.detail_color_text)
    protected TextView colorText;

    @BindView(R.id.detail_gender_text)
    protected TextView genderText;

    @BindView(R.id.detail_breed_text)
    protected TextView breedText;

    @BindView(R.id.detail_type_text)
    protected TextView typeText;

    @BindView(R.id.detail_last_birth)
    protected LinearLayout lastBirthGroup;

    @BindView(R.id.detail_last_birth_text)
    protected TextView lastBirthText;

    @BindView(R.id.detail_due_date)
    protected LinearLayout dueDateGroup;

    @BindView(R.id.detail_due_date_text)
    protected TextView dueDateText;

    @BindView(R.id.detail_castration_date_area)
    protected LinearLayout castrationDateGroup;

    @BindView(R.id.detail_castration_date_text)
    protected TextView castrationDateTextView;

    @BindView(R.id.detail_origin_text)
    protected TextView originTextView;

    @BindView(R.id.detail_limitations_text)
    protected TextView limitationsTextView;

    private Unbinder mUnbinder;
    private GuineaPigDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guinea_pig_detail);
        mUnbinder= ButterKnife.bind(this);
        initToolbar();
        setupImageView();
        int id = getIntent().getIntExtra(getString(R.string.guinea_pig_identifier), 0);
        presenter = new GuineaPigDetailPresenterImpl(this, this, id);
    }

    @Override
    protected void onDestroy() {
        presenter.clearView();
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        presenter.onOptionsItemSelected(item);
        return true;
    }

    private void setupImageView() {
        int height = getResources().getDisplayMetrics().heightPixels;
        ViewGroup.LayoutParams imageLayoutParams = detailImageView.getLayoutParams();
        imageLayoutParams.height = (int) (height / 1.7);
        detailImageView.setLayoutParams(imageLayoutParams);
    }

    @Override
    public void setNameText(String text) {
        nameText.setText(text);
    }

    @Override
    public void setBirthText(String text) {
        birthText.setText(text);
    }

    @Override
    public void setColorText(String text) {
        colorText.setText(text);
    }

    @Override
    public void setGenderText(String text) {
        genderText.setText(text);
    }

    @Override
    public void setBreedText(String text) {
        breedText.setText(text);
    }

    @Override
    public void setTypeText(String text) {
        typeText.setText(text);
    }

    @Override
    public void setWeightText(String text) {
        weightText.setText(text);
    }

    @Override
    public void setLastBirthText(String text) {
        lastBirthText.setText(text);
    }

    @Override
    public void setDueDateText(String text) {
        dueDateText.setText(text);
    }

    @Override
    public void setOriginText(String text) {
        originTextView.setText(text);
    }

    @Override
    public void setLimitationsText(String text) {
        limitationsTextView.setText(text);
    }

    @Override
    public void setCastrationDateText(String text) {
        castrationDateTextView.setText(text);
    }

    @Override
    public void setPicture(File file) {
        ImageService.getInstance().loadImageIntoView(detailImageView, file);
    }

    @Override
    public void setPicture(int resId) {
        ImageService.getInstance().loadImageIntoView(detailImageView, resId);
    }

    @Override
    public void showCastrationDateArea() {
        castrationDateGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDueDateArea() {
        dueDateGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLastBirthArea() {
        lastBirthGroup.setVisibility(View.VISIBLE);
    }
}
