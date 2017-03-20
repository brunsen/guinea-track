package de.brunsen.guineatrack.edit;

import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.database.GuineaPigCRUD;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.GuineaPigOptionalData;
import de.brunsen.guineatrack.model.Type;
import de.brunsen.guineatrack.ui.dialogs.PermissionDialog;
import de.brunsen.guineatrack.util.ImageService;
import de.brunsen.guineatrack.util.TextUtils;
import io.reactivex.functions.Consumer;

public abstract class BaseEditPresenterImpl implements BaseEditPresenter {

    private static final int RESULT_GALLERY = 0;

    protected EditView mEditView;
    protected Context mContext;

    protected GuineaPigCRUD mCrud;
    protected GuineaPig mGuineaPig;
    protected GuineaPig mCopyGuineaPig;
    private List<Gender> genderList;
    private List<Type> typeList;

    protected BaseEditPresenterImpl(Context context) {
        mContext = context;
        mCrud = new GuineaPigCRUD(mContext);
        genderList = new ArrayList<>(Arrays.asList(Gender
                .values()));
        typeList = new ArrayList<>(Arrays.asList(Type.values()));
    }

    @Override
    public void onOptionsItemSelected(int id) {
        // Handle item selection
        if (id == R.id.action_save) {
            save();
        }
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == RESULT_GALLERY && data != null) {

            Uri imgUri = data.getData();
            Cursor cursor = null;
            GuineaPigOptionalData copyOptionalData = mCopyGuineaPig.getOptionalData();
            try {
                String[] projection = {MediaStore.Images.Media.DATA};
                CursorLoader cursorLoader = new CursorLoader(mContext, imgUri,
                        projection, null, null, null);
                cursor = cursorLoader.loadInBackground();

                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String picturePath = cursor.getString(column_index);
                copyOptionalData.setPicturePath(picturePath);
            } catch (Exception e) {
                copyOptionalData.setPicturePath("");
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            if (copyOptionalData.getPicturePath() != null) {
                setImage(copyOptionalData.getPicturePath());
            } else {
                copyOptionalData.setPicturePath("");
            }
        }
    }

    private void setImage(String filePath) {
        Drawable defaultDrawable = ImageService.getInstance().getGetDefaultImage(mContext);
        if (!filePath.equals("")) {
            RxPermissions permissions = RxPermissions.getInstance(mContext);
            boolean hasExternalReadAccess = permissions.isGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasExternalReadAccess) {
                int requiredWidth = mEditView.getImageViewWidth();
                int requiredHeight = mEditView.getImageViewWidth();
                Bitmap picture = ImageService.getInstance().getPicture(filePath, requiredWidth, requiredHeight);
                if (picture != null) {
                    mEditView.setPicture(picture);
                } else {
                    mEditView.setPicture(defaultDrawable);
                }
            } else {
                mEditView.setPicture(defaultDrawable);
                askForExternalStorageReadAccess();
            }
        } else {
            mEditView.setPicture(defaultDrawable);
        }
    }

    @Override
    public void onTypeItemSelected(int position) {
        Type selectedType = typeList.get(position);
        mCopyGuineaPig.setType(selectedType);
        toggleBirthArea();
        toggleCastrationDateArea();
    }

    @Override
    public void onGenderItemSelected(int position) {
        Gender selectedGender = genderList.get(position);
        mCopyGuineaPig.setGender(selectedGender);
        toggleBirthArea();
        toggleCastrationDateArea();
    }

    private void toggleCastrationDateArea() {
        Type selectedType = mCopyGuineaPig.getType();
        Gender selectedGender = mCopyGuineaPig.getGender();
        boolean showCastration = !Gender.Male.equals(selectedGender) && !Type.BREED.equals(selectedType);
        mEditView.showCastrationDateArea(showCastration);
    }

    private void toggleBirthArea() {
        Type selectedType = mCopyGuineaPig.getType();
        Gender selectedGender = mCopyGuineaPig.getGender();
        boolean showBirthArea = Gender.Female.equals(selectedGender) && Type.BREED.equals(selectedType);
        mEditView.showLastBirthArea(showBirthArea);
        mEditView.showDueDateArea(showBirthArea);
    }

    @Override
    public void onAddPictureButtonClicked() {
        AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
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

    private void removePicture() {
        mCopyGuineaPig.getOptionalData().setPicturePath("");
        Drawable drawable = ImageService.getInstance().getGetDefaultImage(mContext);
        mEditView.setPicture(drawable);
    }

    private void callGallery() {
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mEditView.startActivityForResult(galleryIntent, RESULT_GALLERY);
    }

    private void askForExternalStorageReadAccess() {
        RxPermissions permissions = RxPermissions.getInstance(mContext);
        permissions.requestEach(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            setImage(mGuineaPig.getOptionalData().getPicturePath());
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            String rationaleMessage = mContext.getString(R.string.rationale_message_image_display);
                            PermissionDialog dialog = new PermissionDialog(mContext, rationaleMessage, null);
                            dialog.show();
                        } else {
                            // TODO: Inform user about the grave mistake...
                        }
                    }
                });
    }

    @Override
    public List<Gender> getGenderList() {
        return genderList;
    }

    @Override
    public List<Type> getTypeList() {
        return typeList;
    }

    @Override
    public void onBackPressed() {
        if (unStoredChanges()) {
            mEditView.showLeaveConfirmation(getLeaveMessage());
        } else {
            mEditView.finish();
        }
    }

    @Override
    public abstract boolean unStoredChanges();

    @Override
    public void updateName(String name) {
        mCopyGuineaPig.setName(name);
    }

    @Override
    public void updateWeight(String weightText) {
        int weight = TextUtils.textEmpty(weightText) ? 0 : Integer.valueOf(weightText);
        mCopyGuineaPig.getOptionalData().setWeight(weight);
    }

    @Override
    public void updateBirthDate(String date) {
        mCopyGuineaPig.setBirth(date);
    }

    @Override
    public void updateColor(String color) {
        mCopyGuineaPig.setColor(color);
    }

    @Override
    public void updateBreed(String breed) {
        mCopyGuineaPig.setBreed(breed);
    }

    @Override
    public void updateOrigin(String origin) {
        mCopyGuineaPig.getOptionalData().setOrigin(origin);
    }

    @Override
    public void updateLimitations(String limitations) {
        mCopyGuineaPig.getOptionalData().setLimitations(limitations);
    }

    @Override
    public void updateCastrationDate(String date) {
        mCopyGuineaPig.getOptionalData().setCastrationDate(date);
    }

    @Override
    public void updateLastBirthDate(String date) {
        mCopyGuineaPig.getOptionalData().setLastBirth(date);
    }

    @Override
    public void updateDueDate(String date) {
        mCopyGuineaPig.getOptionalData().setDueDate(date);
    }

    @Override
    public void setView(Object view) {
        mEditView = (EditView) view;
        mEditView.setTitle(getTitle());
        setData();
    }

    @Override
    public void clearView() {
        mGuineaPig = null;
        mCopyGuineaPig = null;
        mCrud = null;
        mContext = null;
        mEditView = null;
    }

    protected abstract void storeWithCrud();

    private void save() {
        String name = mCopyGuineaPig.getName();
        String birth = mCopyGuineaPig.getBirth();
        String color = mCopyGuineaPig.getColor();
        String breed = mCopyGuineaPig.getBreed();
        Gender selectedGender = mCopyGuineaPig.getGender();
        Type selectedType = mCopyGuineaPig.getType();
        if (TextUtils.textEmpty(name) || TextUtils.textEmpty(birth) || TextUtils.textEmpty(color)
                || TextUtils.textEmpty(breed) || selectedGender == null || selectedType == null) {
            Toast.makeText(mContext, mContext.getString(R.string.error_empty_fields), Toast.LENGTH_LONG).show();
        } else {
            storeWithCrud();
        }
    }

    protected abstract String getTitle();

    protected abstract String getLeaveMessage();

    private void setData() {
        mEditView.setNameText(mCopyGuineaPig.getName());
        mEditView.setBirthText(mCopyGuineaPig.getBirth());
        mEditView.setColorText(mCopyGuineaPig.getColor());
        mEditView.setBreedText(mCopyGuineaPig.getBreed());
        mEditView.setSpinnerGender(mCopyGuineaPig.getGender());
        mEditView.setSpinnerType(mCopyGuineaPig.getType());
        GuineaPigOptionalData optionalData = mCopyGuineaPig.getOptionalData();
        mEditView.setWeightText("" + optionalData.getWeight());
        mEditView.setOriginText(optionalData.getOrigin());
        mEditView.setLimitationsText(optionalData.getLimitations());
        mEditView.setCastrationDateText(optionalData.getCastrationDate());
        mEditView.setLastBirthText(optionalData.getLastBirth());
        mEditView.setDueDateText(optionalData.getDueDate());
        setImage(optionalData.getPicturePath());
    }
}
