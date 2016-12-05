package de.brunsen.guineatrack.ui.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.GuineaPigOptionalData;
import de.brunsen.guineatrack.model.Type;
import de.brunsen.guineatrack.ui.adapter.GenderSpinnerAdapter;
import de.brunsen.guineatrack.ui.adapter.TypeSpinnerAdapter;
import de.brunsen.guineatrack.ui.dialogs.DatePickDialog;
import de.brunsen.guineatrack.ui.dialogs.PermissionDialog;
import de.brunsen.guineatrack.util.ImageService;

public abstract class BaseGuineaPigEditActivity extends BaseActivity {

    protected static final int PERMISSION_REQUEST_PICTURE = 42;
    private static final int RESULT_GALLERY = 0;
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
    @BindView(R.id.edit_add_picture)
    protected Button selectImageButton;
    protected Gender selectedGender;
    protected Type selectedType;
    protected List<Gender> genderSpinnerItems;
    protected List<Type> typeSpinnerItems;
    protected String selectedImage;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guinea_pig_edit);
        initToolbar();
        mUnbinder = ButterKnife.bind(this);
        setAdaptersAndListeners();
        selectedImage = "";
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
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
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_PICTURE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && !selectedImage.equals("")) {
                setImage(selectedImage);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void askForExternalStorageReadAccess(final int request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    String rationaleMessage = getString(R.string.rationale_message_image_display);
                    PermissionDialog dialog = new PermissionDialog(this, rationaleMessage, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BaseGuineaPigEditActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request);
                        }
                    });
                    dialog.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request);
                }
            }
        }
    }

    protected void setAdaptersAndListeners() {
        typeSpinnerItems = new ArrayList<>(Arrays.asList(Type.values()));
        TypeSpinnerAdapter typeAdapter = new TypeSpinnerAdapter(this,
                android.R.layout.simple_list_item_1, typeSpinnerItems);
        typeSpinner.setAdapter(typeAdapter);
        genderSpinnerItems = new ArrayList<>(Arrays.asList(Gender
                .values()));
        GenderSpinnerAdapter genderAdapter = new GenderSpinnerAdapter(this,
                android.R.layout.simple_list_item_1, genderSpinnerItems);
        genderSpinner.setAdapter(genderAdapter);
        birthEdit.setOnFocusChangeListener(new TimePickerCaller());
        lastBirthEdit.setOnFocusChangeListener(new TimePickerCaller());
        dueDateEdit.setOnFocusChangeListener(new TimePickerCaller());
        castrationDateEdit.setOnFocusChangeListener(new TimePickerCaller());
    }

    @OnItemSelected(R.id.edit_type_spinner)
    protected void onTypeItemSelected(int position) {
        selectedType = typeSpinnerItems.get(position);
        toggleCastrationDateArea();
    }

    @OnItemSelected(R.id.edit_gender_spinner)
    protected void onGenderItemSelected(int position) {
        selectedGender = genderSpinnerItems.get(position);
        boolean female = selectedGender == Gender.Female;
        setLastBirthAreaVisible(female);
        setDueDateAreaVisible(female);
        toggleCastrationDateArea();
    }

    @OnClick(R.id.edit_add_picture)
    public void onAddButtonClicked() {
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

    protected void setImage(String filePath) {
        if (!filePath.equals("")) {
            if (hasExternalReadAccess()) {
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
                askForExternalStorageReadAccess(PERMISSION_REQUEST_PICTURE);
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
        String breed = breedEdit.getText().toString();
        if (name.equals("") || birth.equals("") || color.equals("")
                || breed.equals("") || selectedGender == null || selectedType == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_empty_fields), Toast.LENGTH_LONG)
                    .show();
        } else {
            String lastBirth = "";
            String dueDate = "";
            if (selectedGender == Gender.Female) {
                lastBirth = lastBirthEdit.getText().toString();
                dueDate = dueDateEdit.getText().toString();
            }
            String castrationDate = "";
            if (!selectedGender.equals(Gender.Male) && !selectedType.equals(Type.BREED)) {
                castrationDate = castrationDateEdit.getText().toString();
            }
            String weightText = weightEdit.getText().toString();
            int weight = 0;
            if (!weightText.equals("")) {
                weight = Integer.valueOf(weightText);
            }
            String origin = originEdit.getText().toString();
            String limitations = limitationsEdit.getText().toString();
            GuineaPigOptionalData optionalData = new GuineaPigOptionalData(weight, lastBirth, dueDate, origin, limitations, castrationDate, selectedImage);
            GuineaPig pig = new GuineaPig(name, birth, selectedGender, color, breed, selectedType, optionalData);
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

    protected void setDueDateAreaVisible(boolean visible) {
        if (visible) {
            dueDateRow.setVisibility(View.VISIBLE);
        } else {
            dueDateRow.setVisibility(View.GONE);
        }
    }

    protected void toggleCastrationDateArea() {
        boolean showCastration = selectedGender != null && !selectedGender.equals(Gender.Male) && selectedType != null && !selectedType.equals(Type.BREED);
        if (showCastration) {
            castrationDateRow.setVisibility(View.VISIBLE);
        } else {
            castrationDateRow.setVisibility(View.GONE);
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

    private class TimePickerCaller implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                EditText editText = (EditText) v;
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickDialog datePicker = new DatePickDialog(editText.getContext(), editText, year, month, day);
                datePicker.show();
            }
        }
    }
}
