package de.brunsen.guineatrack.ui.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.database.GuineaPigCRUD;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.GuineaPigOptionalData;
import de.brunsen.guineatrack.model.Type;
import de.brunsen.guineatrack.util.ImageService;
import de.brunsen.guineatrack.ui.dialogs.PermissionDialog;

public class GuineaPigDetailActivity extends BaseActivity {
    protected static final int PERMISSION_REQUEST_PICTURE = 42;
    private GuineaPig mGuineaPig;
    private ImageView pigImage;
    private TextView nameText;
    private TextView birthText;
    private TextView weightText;
    private TextView colorText;
    private TextView genderText;
    private TextView raceText;
    private TextView typeText;
    private LinearLayout lastBirthGroup;
    private TextView lastBirthText;
    private LinearLayout dueDateGroup;
    private TextView dueDateText;
    private LinearLayout castrationDateGroup;
    private TextView castrationDateTextView;
    private TextView originTextView;
    private TextView limitationsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guinea_pig_detail);
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            mGuineaPig = crud.getGuineaPigForId(getIntent().getIntExtra(getString(R.string.pig_identifier), 0));
            initComponents();
            initToolbar();
            setData();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
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
        switch (item.getItemId()) {
            case R.id.call_edit:
                callEditor();
                return true;
            case R.id.call_delete:
                showDeleteDialog();
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
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setPicture(mGuineaPig.getOptionalData());
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
                            ActivityCompat.requestPermissions(GuineaPigDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request);
                        }
                    });
                    dialog.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request);
                }
            }
        }
    }

    private void initComponents() {
        pigImage = (ImageView) findViewById(R.id.detail_image);
        nameText = (TextView) findViewById(R.id.detail_name_text);
        birthText = (TextView) findViewById(R.id.detail_birth_text);
        colorText = (TextView) findViewById(R.id.detail_color_text);
        genderText = (TextView) findViewById(R.id.detail_gender_spinner);
        raceText = (TextView) findViewById(R.id.detail_breed_text);
        typeText = (TextView) findViewById(R.id.detail_type_text);
        lastBirthGroup = (LinearLayout) findViewById(R.id.detail_last_birth);
        lastBirthText = (TextView) findViewById(R.id.detail_last_birth_text);
        dueDateGroup = (LinearLayout) findViewById(R.id.detail_due_date);
        dueDateText = (TextView) findViewById(R.id.detail_due_date_text);
        castrationDateGroup = (LinearLayout) findViewById(R.id.detail_castration_data_area);
        castrationDateTextView = (TextView) findViewById(R.id.detail_castration_date_text);
        weightText = (TextView) findViewById(R.id.detail_weight_text);
        originTextView = (TextView) findViewById(R.id.detail_origin_text);
        limitationsTextView = (TextView) findViewById(R.id.detail_limitations_text);
    }

    private void setData() {
        GuineaPigOptionalData optionalData = mGuineaPig.getOptionalData();
        setPicture(optionalData);
        nameText.setText(mGuineaPig.getName());
        birthText.setText(mGuineaPig.getBirth());
        colorText.setText(mGuineaPig.getColor());
        genderText.setText(mGuineaPig.getGender().getText(this));
        raceText.setText(mGuineaPig.getBreed());
        typeText.setText(mGuineaPig.getType().getText(this));
        setOptionalData(optionalData);
    }

    private void setPicture(GuineaPigOptionalData optionalData) {
        String picturePath = optionalData.getPicturePath();
        int height = getResources().getDisplayMetrics().heightPixels;
        ViewGroup.LayoutParams imageLayoutParams = pigImage.getLayoutParams();
        imageLayoutParams.height = (int) (height / 1.7);
        pigImage.setLayoutParams(imageLayoutParams);
        if (picturePath.equals("")) {
            ImageService.getInstance().setDefaultImage(pigImage);
        } else if (!hasExternalReadAccess()) {
            askForExternalStorageReadAccess(PERMISSION_REQUEST_PICTURE);
        }else {
            int requiredWidth = imageLayoutParams.width / 2;
            int requiredHeight = imageLayoutParams.height / 2;
            Bitmap picture = ImageService.getInstance().getPicture(picturePath, requiredWidth, requiredHeight);
            if (picture != null) {
                pigImage.setImageBitmap(picture);
            } else {
                ImageService.getInstance().setDefaultImage(pigImage);
            }
        }
    }

    private void setOptionalData(GuineaPigOptionalData optionalData) {
        if (mGuineaPig.getGender() == Gender.Female) {
            dueDateGroup.setVisibility(View.VISIBLE);
            lastBirthGroup.setVisibility(View.VISIBLE);
            String lastBirthDisplayText = optionalData.getLastBirth();
            String dueDateDisplayText = optionalData.getDueDate();
            if (lastBirthDisplayText.equals("")) {
                lastBirthDisplayText = getString(R.string.unknown);
            }
            if (dueDateDisplayText.equals("")) {
                dueDateDisplayText = getString(R.string.unknown);
            }
            lastBirthText.setText(lastBirthDisplayText);
            dueDateText.setText(dueDateDisplayText);
        }
        if (mGuineaPig.getGender() != Gender.Male && mGuineaPig.getType() != Type.BREED) {
            castrationDateGroup.setVisibility(View.VISIBLE);
            String castrationDate = optionalData.getCastrationDate();
            if (castrationDate.equals("")) {
                castrationDate = getString(R.string.unknown);
            }
            castrationDateTextView.setText(castrationDate);
        }
        weightText.setText("" + optionalData.getWeight());
        String originText = optionalData.getOrigin();
        if (originText.equals("")) {
            originText = getString(R.string.unknown);
        }
        originTextView.setText(originText);
        String limitationsText = optionalData.getLimitations();
        if (limitationsText.equals("")) {
            limitationsText = getString(R.string.no_limitations);
        }
        limitationsTextView.setText(limitationsText);
    }

    public void deleteGuineaPig() {
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            crud.deleteGuineaPig(mGuineaPig);
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_pig_not_deleted_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void callEditor() {
        Intent intent = new Intent(getApplicationContext(),
                GuineaPigEditActivity.class);
        intent.putExtra(getString(R.string.pig_identifier), mGuineaPig.getId());
        startActivity(intent);
    }

    public void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(getString(R.string.confirm));
        builder.setMessage(getString(R.string.deletion_confirmation_text, mGuineaPig.getName()));
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteGuineaPig();
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

}
