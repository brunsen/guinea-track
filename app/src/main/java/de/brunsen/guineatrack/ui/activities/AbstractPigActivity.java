package de.brunsen.guineatrack.ui.activities;

import android.app.DatePickerDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.brunsen.guineatrack.DatePickDialog;
import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.ui.adapter.GenderSpinnerAdapter;
import de.brunsen.guineatrack.ui.adapter.TypeSpinnerAdapter;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.Type;
import de.brunsen.guineatrack.services.ImageService;

public abstract class AbstractPigActivity extends BaseActivity implements
        OnItemSelectedListener, OnClickListener {
    protected EditText nameEdit;
    protected EditText birthEdit;
    protected EditText colorEdit;
    protected EditText raceEdit;
    protected Spinner genderSpinner;
    protected Spinner typeSpinner;
    protected Gender selectedGender;
    protected Type selectedType;
    protected ImageView editImage;
    protected Button selectImageButton;
    protected List<Gender> genderSpinnerItems;
    protected List<Type> typeSpinnerItems;
    protected String selectedImage;
    protected TableRow lastBirthRow;
    protected EditText lastBirthEdit;
    private static final int RESULT_GALLERY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guinea_pig_edit);
        initToolbar();
        initComponents();
        setAdaptersAndListeners();
        selectedImage = "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void initComponents() {
        nameEdit = (EditText) findViewById(R.id.detail_name_text);
        birthEdit = (EditText) findViewById(R.id.detail_birth_text);
        colorEdit = (EditText) findViewById(R.id.detail_color_text);
        raceEdit = (EditText) findViewById(R.id.detail_race_text);
        genderSpinner = (Spinner) findViewById(R.id.detail_gender_spinner);
        typeSpinner = (Spinner) findViewById(R.id.detail_type_text);
        selectImageButton = (Button) findViewById(R.id.add_picture);
        editImage = (ImageView) findViewById(R.id.edit_image);
        lastBirthRow = (TableRow) findViewById(R.id.last_birth_row);
        lastBirthEdit = (EditText) findViewById(R.id.last_birth_edit_text);
    }

    protected void setAdaptersAndListeners() {
        typeSpinnerItems = new ArrayList<>(Arrays.asList(Type.values()));
        TypeSpinnerAdapter typeAdapter = new TypeSpinnerAdapter(this,
                android.R.layout.simple_list_item_1, typeSpinnerItems);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(this);
        genderSpinnerItems = new ArrayList<>(Arrays.asList(Gender
                .values()));
        GenderSpinnerAdapter genderAdapter = new GenderSpinnerAdapter(this,
                android.R.layout.simple_list_item_1, genderSpinnerItems);
        genderSpinner.setAdapter(genderAdapter);
        genderSpinner.setOnItemSelectedListener(this);
        selectImageButton.setOnClickListener(this);
        birthEdit.setOnFocusChangeListener(new timePickerCaller());
        lastBirthEdit.setOnFocusChangeListener(new timePickerCaller());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        if (parent.getId() == R.id.detail_gender_spinner) {
            selectedGender = genderSpinnerItems.get(position);
            if (selectedGender == Gender.Female) {
                setLastBirthAreaVisible(true);
            } else {
                setLastBirthAreaVisible(false);
            }
        } else if (parent.getId() == R.id.detail_type_text) {
            selectedType = typeSpinnerItems.get(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (parent.getId() == R.id.detail_gender_spinner) {
            selectedGender = null;
        } else if (parent.getId() == R.id.detail_type_text) {
            selectedType = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_picture) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setItems(R.array.picture_actions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            callGallery();
                            break;
                        case 1:
                            removePicture();
                            break;
                        default:
                            break;
                    }
                }
            });
            adb.show();
        }
    }

    protected void setImage(String filePath) {
        if (!filePath.equals("")) {
            int requiredWidth = (int) getResources().getDimension(R.dimen.edit_image_width);
            int requiredHeight = (int) getResources().getDimension(R.dimen.edit_image_height);
            Bitmap picture = ImageService.getInstance().getPicture(filePath, requiredWidth, requiredHeight);
            if (picture != null) {
                editImage.setImageBitmap(picture);
            } else {
                ImageService.getInstance().setDefaultImage(editImage);
            }
        } else {
            ImageService.getInstance().setDefaultImage(editImage);
        }
    }

    protected void removePicture() {
        selectedImage = "";
        ImageService.getInstance().setDefaultImage(editImage);
    }

    protected void callGallery() {
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_GALLERY);
    }

    protected abstract void setData();

    public void save() {
        String name = nameEdit.getText().toString();
        String birth = birthEdit.getText().toString();
        String color = colorEdit.getText().toString();
        String race = raceEdit.getText().toString();
        if (name.equals("") || birth.equals("") || color.equals("")
                || race.equals("") || selectedGender == null || selectedType == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_empty_fields), Toast.LENGTH_LONG)
                    .show();
        } else {
            String lastBirth = "";
            if (selectedGender == Gender.Female) {
                lastBirth = lastBirthEdit.getText().toString();
            }
            GuineaPig pig = new GuineaPig(name, birth, selectedGender,
                    color, race, selectedType, selectedImage, lastBirth);
            storeWithCrud(pig);
        }
    }

    protected abstract void storeWithCrud(GuineaPig pig);


    protected void setLastBirthAreaVisible(boolean visible) {
        if (visible) {
            lastBirthRow.setVisibility(View.VISIBLE);
        } else {
            lastBirthRow.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_GALLERY && data != null) {

            Uri imgUri = data.getData();
            Cursor cursor = null;
            try {
                String[] projection = {MediaStore.Images.Media.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, imgUri,
                        projection, null, null, null);
                cursor = cursorLoader.loadInBackground();

                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                selectedImage = cursor.getString(column_index);
            } catch (Exception e) {
                selectedImage = "";
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            if (selectedImage != null) {
                setImage(selectedImage);
            } else {
                selectedImage = "";
            }
        }
    }

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

    private class timePickerCaller implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                EditText editText = (EditText) v;
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickDialog datePicker = new DatePickDialog(editText.getContext(), editText, year, month, day);
                datePicker.getDatePicker().setCalendarViewShown(false);
                Log.d("Bennet", "Datepicker calendar shown:" +datePicker.getDatePicker().getCalendarViewShown());
                datePicker.show();
            }
        }
    }
}
