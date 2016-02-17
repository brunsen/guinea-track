package de.brunsen.guineatrack.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import org.json.JSONException;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.GuineaPigOptionalData;
import de.brunsen.guineatrack.model.Type;
import de.brunsen.guineatrack.database.GuineaPigCRUD;
import de.brunsen.guineatrack.services.ImageService;
import de.brunsen.guineatrack.services.JsonReader;
import de.brunsen.guineatrack.services.JsonWriter;

public class GuineaPigDetailActivity extends BaseActivity {
    private GuineaPig pig;
    private ImageView pigImage;
    private TextView nameText;
    private TextView birthText;
    private TextView weightText;
    private TextView colorText;
    private TextView genderText;
    private TextView raceText;
    private TextView typeText;
    private LinearLayout lastBirthGroup;
    private TextView lastBirth;
    private LinearLayout castrationDateGroup;
    private TextView castrationDateTextView;
    private TextView originTextView;
    private TextView limitationsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guinea_pig_detail);
        JsonReader reader = new JsonReader(this);
        try {
            pig = reader.getGuineaPigFromString(getIntent().getStringExtra(getString(R.string.pig_identifier)));
            initComponents();
            initToolbar();
            setData();
        } catch (JSONException e) {
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

    private void initComponents() {
        pigImage = (ImageView) findViewById(R.id.detail_image);
        nameText = (TextView) findViewById(R.id.detail_name_text);
        birthText = (TextView) findViewById(R.id.detail_birth_text);
        colorText = (TextView) findViewById(R.id.detail_color_text);
        genderText = (TextView) findViewById(R.id.detail_gender_spinner);
        raceText = (TextView) findViewById(R.id.detail_breed_text);
        typeText = (TextView) findViewById(R.id.detail_type_text);
        lastBirthGroup = (LinearLayout) findViewById(R.id.detail_last_birth);
        lastBirth = (TextView) findViewById(R.id.detail_last_birth_text);
        castrationDateGroup = (LinearLayout) findViewById(R.id.detail_castration_data_area);
        castrationDateTextView = (TextView) findViewById(R.id.detail_castration_date_text);
        weightText = (TextView) findViewById(R.id.detail_weight_text);
        originTextView = (TextView) findViewById(R.id.detail_origin_text);
        limitationsTextView = (TextView) findViewById(R.id.detail_limitations_text);
    }

    private void setData() {
        GuineaPigOptionalData optionalData = pig.getOptionalData();
        setPicture(optionalData);
        nameText.setText(pig.getName());
        birthText.setText(pig.getBirth());
        colorText.setText(pig.getColor());
        genderText.setText(pig.getGender().getText());
        raceText.setText(pig.getBreed());
        typeText.setText(pig.getType().getText());
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
        } else {
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
        if (pig.getGender() == Gender.Female) {
            lastBirthGroup.setVisibility(View.VISIBLE);
            String lastBirthText = optionalData.getLastBirth();
            if (lastBirthText.equals("")) {
                lastBirthText = getString(R.string.unknown);
            }
            lastBirth.setText(lastBirthText);
        }
        if (pig.getGender() != Gender.Male && pig.getType() != Type.BREED) {
            castrationDateGroup.setVisibility(View.VISIBLE);
            String castrationDate = optionalData.getCastrationDate();
            if (castrationDate.equals("")) {
                castrationDate = getString(R.string.unknown);
            }
            castrationDateTextView.setText(castrationDate);
        }
        weightText.setText("" + optionalData.getWeight());
        String originText = optionalData.getOrigin();
        if (originText.equals("")){
            originText = getString(R.string.unknown);
        }
        originTextView.setText(originText);
        String limitationsText = optionalData.getLimitations();
        if (limitationsText.equals("")){
            limitationsText = getString(R.string.no_limitations);
        }
        limitationsTextView.setText(limitationsText);
    }

    public void deletePig() {
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            crud.deleteGuineaPig(pig);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_pig_not_deleted_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void callEditor() {
        try {
            Intent intent = new Intent(getApplicationContext(),
                    GuineaPigEditActivity.class);
            JsonWriter writer = new JsonWriter(this);
            String json = writer.createGuineaJson(pig).toString();
            intent.putExtra(getString(R.string.pig_identifier), json);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(getString(R.string.confirm));
        builder.setMessage(getString(R.string.deletion_confirmation_text, pig.getName()));
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePig();
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
