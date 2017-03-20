package de.brunsen.guineatrack.edit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.edit.adapter.GenderSpinnerAdapter;
import de.brunsen.guineatrack.edit.adapter.TypeSpinnerAdapter;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.Type;
import de.brunsen.guineatrack.ui.activities.BaseActivity;
import de.brunsen.guineatrack.ui.dialogs.DatePickDialog;

public class GuineaPigEditActivity extends BaseActivity implements EditView{

    @BindView(R.id.edit_name_text)
    protected EditText nameEdit;
    @BindView(R.id.edit_birth_text)
    protected EditText birthEdit;
    @BindView(R.id.edit_color_text)
    protected EditText colorEdit;
    @BindView(R.id.edit_breed_text)
    protected EditText breedEdit;
    @BindView(R.id.edit_weight_text)
    protected EditText weightEdit;
    @BindView(R.id.edit_castration_text)
    protected EditText castrationDateEdit;
    @BindView(R.id.edit_limitations_text)
    protected EditText limitationsEdit;
    @BindView(R.id.edit_origin_text)
    protected EditText originEdit;
    @BindView(R.id.edit_gender_spinner)
    protected Spinner genderSpinner;
    @BindView(R.id.edit_type_spinner)
    protected Spinner typeSpinner;
    @BindView(R.id.edit_last_birth_row)
    protected TableRow lastBirthRow;
    @BindView(R.id.edit_due_date_row)
    protected TableRow dueDateRow;
    @BindView(R.id.edit_castration_row)
    protected TableRow castrationDateRow;
    @BindView(R.id.edit_last_birth_text)
    protected EditText lastBirthEdit;
    @BindView(R.id.edit_due_date_text)
    protected EditText dueDateEdit;
    @BindView(R.id.edit_image)
    protected ImageView editImage;

    private BaseEditPresenter presenter;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guinea_pig_edit);
        initToolbar();
        mUnbinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        boolean editorMode = intent.getBooleanExtra(getString(R.string.editor_mode_flag), false);
        if (editorMode) {
            int guineaPigId = intent.getIntExtra(getString(R.string.guinea_pig_identifier), 0);
            presenter = new EditPresenterImpl(this, guineaPigId);
        } else {
            presenter = new AddPresenterImpl(this);
        }
        setSpinnerAdapters();
        presenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        genderSpinner.setAdapter(null);
        typeSpinner.setAdapter(null);
        mUnbinder.unbind();
        presenter.clearView();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.onOptionsItemSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    protected void setSpinnerAdapters() {
        TypeSpinnerAdapter typeAdapter = new TypeSpinnerAdapter(this,
                android.R.layout.simple_list_item_1, presenter);
        typeSpinner.setAdapter(typeAdapter);
        GenderSpinnerAdapter genderAdapter = new GenderSpinnerAdapter(this,
                android.R.layout.simple_list_item_1, presenter);
        genderSpinner.setAdapter(genderAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, data);
    }

    @Override
    public void showLeaveConfirmation(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(R.string.confirm);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setPicture(Bitmap bitmap) {
        editImage.setImageBitmap(bitmap);
    }

    @Override
    public void setPicture(Drawable drawable) {
        editImage.setImageDrawable(drawable);
    }

    @Override
    public void showCastrationDateArea(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        castrationDateRow.setVisibility(visibility);
    }

    @Override
    public void showDueDateArea(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        dueDateRow.setVisibility(visibility);
    }

    @Override
    public void showLastBirthArea(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        lastBirthRow.setVisibility(visibility);
    }

    @Override
    public int getImageViewWidth() {
        return editImage.getWidth();
    }

    @Override
    public void setNameText(String text) {
        nameEdit.setText(text);
    }

    @Override
    public void setBirthText(String text) {
        birthEdit.setText(text);
    }

    @Override
    public void setColorText(String text) {
        colorEdit.setText(text);
    }

    @Override
    public void setBreedText(String text) {
        breedEdit.setText(text);
    }

    @Override
    public void setSpinnerType(Type type) {
        typeSpinner.setSelection(type.getPosition());
    }

    @Override
    public void setSpinnerGender(Gender gender) {
        genderSpinner.setSelection(gender.getPosition());
    }

    @Override
    public void setWeightText(String text) {
        weightEdit.setText(text);
    }

    @Override
    public void setLastBirthText(String text) {
        lastBirthEdit.setText(text);
    }

    @Override
    public void setDueDateText(String text) {
        dueDateEdit.setText(text);
    }

    @Override
    public void setOriginText(String text) {
        originEdit.setText(text);
    }

    @Override
    public void setLimitationsText(String text) {
        limitationsEdit.setText(text);
    }

    @Override
    public void setCastrationDateText(String text) {
        castrationDateEdit.setText(text);
    }

    // TextListeners

    @OnTextChanged(value = R.id.edit_name_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onNameTextChanged(Editable editable) {
        presenter.updateName(editable.toString());
    }

    @OnTextChanged(value = R.id.edit_birth_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onBirthTextChanged(Editable editable) {
        presenter.updateBirthDate(editable.toString());
    }

    @OnTextChanged(value = R.id.edit_color_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onColorTextChanged(Editable editable){
        presenter.updateColor(editable.toString());
    }

    @OnTextChanged(value = R.id.edit_breed_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onBreedTextChanged(Editable editable){
        presenter.updateBreed(editable.toString());
    }

    @OnTextChanged(value = R.id.edit_weight_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onWeightTextChanged(Editable editable){
        presenter.updateWeight(editable.toString());
    }

    @OnTextChanged(value = R.id.edit_castration_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onCastrationTextChanged(Editable editable){
        presenter.updateCastrationDate(editable.toString());
    }

    @OnTextChanged(value = R.id.edit_limitations_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onLimitationsTextChanged(Editable editable){
        presenter.updateLimitations(editable.toString());
    }

    @OnTextChanged(value = R.id.edit_origin_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onOriginTextChanged(Editable editable){
        presenter.updateOrigin(editable.toString());
    }

    @OnTextChanged(value = R.id.edit_last_birth_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onLastBirthTextChanged(Editable editable){
        presenter.updateLastBirthDate(editable.toString());
    }

    @OnTextChanged(value = R.id.edit_due_date_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onDueDateTextChanged(Editable editable){
        presenter.updateDueDate(editable.toString());
    }

    @OnItemSelected(R.id.edit_type_spinner)
    protected void onTypeItemSelected(int position) {
        presenter.onTypeItemSelected(position);
    }

    @OnItemSelected(R.id.edit_gender_spinner)
    protected void onGenderItemSelected(int position) {
        presenter.onGenderItemSelected(position);
    }

    @OnClick(R.id.edit_add_picture)
    protected void onAddPictureButtonClicked() {
        presenter.onAddPictureButtonClicked();
    }

    @OnFocusChange(value = {R.id.edit_birth_text, R.id.edit_last_birth_text, R.id.edit_due_date_text, R.id.edit_castration_text})
    protected void focusChanged(View view, boolean hasFocus) {
        if (hasFocus) {
            EditText editText = (EditText) view;
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            DatePickDialog datePicker = new DatePickDialog(editText.getContext(), editText, year, month, day);
            datePicker.show();
        }
    }
}
