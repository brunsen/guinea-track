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
import de.brunsen.guineatrack.services.GuineaPigCRUD;
import de.brunsen.guineatrack.services.ImageService;
import de.brunsen.guineatrack.services.JsonReader;
import de.brunsen.guineatrack.services.JsonWriter;

public class GuineaPigDetailActivity extends BaseActivity {
    private GuineaPig pig;
    private ImageView pigImage;
    private TextView nameText;
    private TextView birthText;
    private TextView colorText;
    private TextView genderText;
    private TextView raceText;
    private TextView typeText;
    private LinearLayout lastBirthGroup;
    private TextView lastBirth;

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
        raceText = (TextView) findViewById(R.id.detail_race_text);
        typeText = (TextView) findViewById(R.id.detail_type_text);
        lastBirthGroup = (LinearLayout) findViewById(R.id.detail_last_birth);
        lastBirth = (TextView) findViewById(R.id.detail_last_birth_text);
    }

    private void setData() {
        String picturePath = pig.getPicturePath();
        int height = getResources().getDisplayMetrics().heightPixels;
        ViewGroup.LayoutParams imageLayoutParams = pigImage.getLayoutParams();
        imageLayoutParams.height = (int) (height / 1.7);
        pigImage.setLayoutParams(imageLayoutParams);
        if (picturePath.equals("")) {
            ImageService.getInstance().setDefaultImage(pigImage);
        } else {
            int requiredWidth =imageLayoutParams.width / 2;
            int requiredHeight = imageLayoutParams.height / 2;
            Bitmap picture = ImageService.getInstance().getPicture(pig.getPicturePath(), requiredWidth, requiredHeight);
            if (picture != null) {
                pigImage.setImageBitmap(picture);
            } else {
                ImageService.getInstance().setDefaultImage(pigImage);
            }
        }
        nameText.setText(pig.getName());
        birthText.setText(pig.getBirth());
        colorText.setText(pig.getColor());
        genderText.setText(pig.getGender().getText());
        raceText.setText(pig.getRace());
        typeText.setText(pig.getType().getText());
        if (pig.getGender() == Gender.Female) {
            lastBirthGroup.setVisibility(View.VISIBLE);
            String lastBirthText = pig.getLastBirth();
            if (lastBirthText.equals("")) {
                lastBirthText = getString(R.string.unknown);
            }
            lastBirth.setText(lastBirthText);
        }
    }

    public void deletePig() {
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            crud.deletePig(pig);
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
