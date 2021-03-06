package de.brunsen.guineatrack.detail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.database.GuineaPigCRUD;
import de.brunsen.guineatrack.edit.GuineaPigEditActivity;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.GuineaPigOptionalData;
import de.brunsen.guineatrack.model.Type;
import de.brunsen.guineatrack.ui.dialogs.PermissionDialog;
import de.brunsen.guineatrack.util.Settings;
import de.brunsen.guineatrack.util.TextUtils;
import io.reactivex.functions.Consumer;

public class GuineaPigDetailPresenterImpl implements GuineaPigDetailPresenter {

    private Context mContext;

    private GuineaPigDetailView mView;

    private GuineaPig mGuineaPig;

    public GuineaPigDetailPresenterImpl(Context context, GuineaPigDetailView view, int guineaPigId) {
        mContext = context;
        setView(view);
        GuineaPigCRUD crud = new GuineaPigCRUD(mContext);
        try {
            mGuineaPig = crud.getGuineaPigForId(guineaPigId);
            setData();
        } catch (Exception e) {
            e.printStackTrace();
            mView.finish();
        }
    }

    @Override
    public void setView(Object view) {
        mView = (GuineaPigDetailView) view;
    }

    @Override
    public void clearView() {
        mGuineaPig = null;
        mContext = null;
        mView = null;
    }

    @Override
    public boolean onOptionsItemSelected(int itemId) {
        switch (itemId) {
            case R.id.call_edit:
                callEditor();
                return true;
            case R.id.call_delete:
                showDeleteDialog();
                return true;
            default:
                return false;
        }
    }

    private void callEditor() {
        Intent intent = new Intent(mContext, GuineaPigEditActivity.class);
        intent.putExtra(mContext.getString(R.string.guinea_pig_identifier), mGuineaPig.getId());
        intent.putExtra(mContext.getString(R.string.editor_mode_flag), true);
        mContext.startActivity(intent);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
        builder.setTitle(mContext.getString(R.string.confirm));
        builder.setMessage(mContext.getString(R.string.deletion_confirmation_text, mGuineaPig.getName()));
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

    private void deleteGuineaPig() {
        GuineaPigCRUD crud = new GuineaPigCRUD(mContext);
        try {
            crud.deleteGuineaPig(mGuineaPig);
            mView.finish();
        } catch (Exception e) {
            Toast.makeText(mContext, mContext.getString(R.string.error_pig_not_deleted_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void checkPicture(GuineaPigOptionalData optionalData) {
        String picturePath = optionalData.getPicturePath();
        RxPermissions permissions = RxPermissions.getInstance(mContext);
        boolean hasExternalReadAccess = permissions.isGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (TextUtils.textEmpty(picturePath)) {
            mView.setPicture(R.drawable.unknown_guinea_pig);
        } else if (!hasExternalReadAccess) {
            askForExternalStorageReadAccess();
        } else {
            setPicture(picturePath);
        }
    }

    private void setPicture(String picturePath) {
        File file = new File(picturePath);
        if (file.exists()) {
            mView.setPicture(file);
        } else {
            mView.setPicture(R.drawable.unknown_guinea_pig);
        }
    }

    private void setData() {
        GuineaPigOptionalData optionalData = mGuineaPig.getOptionalData();
        mView.setNameText(mGuineaPig.getName());
        mView.setBirthText(mGuineaPig.getBirth());
        mView.setColorText(mGuineaPig.getColor());
        mView.setGenderText(mGuineaPig.getGender().getText(mContext));
        mView.setBreedText(mGuineaPig.getBreed());
        mView.setTypeText(mGuineaPig.getType().getText(mContext));
        checkPicture(optionalData);
        setOptionalData(optionalData);
    }

    private void setOptionalData(GuineaPigOptionalData optionalData) {
        String unknownText = mContext.getString(R.string.unknown);
        Settings settings = Settings.getSettings(mContext);
        setWeightData(optionalData, settings);
        setOriginData(optionalData, settings, unknownText);
        setLimitationsData(optionalData, settings);
        setEntryData(optionalData, settings, unknownText);
        setDepartureData(optionalData, settings, unknownText);

        if (mGuineaPig.getGender() == Gender.Female) {
            setDueDateData(optionalData, settings, unknownText);
            setLastBirthData(optionalData, settings, unknownText);
        }
        setCastrationData(optionalData, unknownText);
    }

    private void setEntryData(GuineaPigOptionalData optionalData, Settings settings, String unknownText) {
        if (settings.displayEntryField()) {
            String entryText = optionalData.getEntry();
            if (TextUtils.textEmpty(entryText)) {
                entryText = unknownText;
            }
            mView.setEntryText(entryText);
            mView.showEntryArea();
        }
    }

    private void setDepartureData(GuineaPigOptionalData optionalData, Settings settings, String unknownText) {
        if (settings.displayDepartureField()) {
            String departureText = optionalData.getDeparture();
            if (TextUtils.textEmpty(departureText)) {
                departureText = unknownText;
            }
            mView.setDepartureText(departureText);
            mView.showDepartureArea();
        }
    }

    private void setLimitationsData(GuineaPigOptionalData optionalData, Settings settings) {
        if (settings.displayLimitationsField()) {
            String limitationsText = optionalData.getLimitations();
            if (TextUtils.textEmpty(limitationsText)) {
                limitationsText = mContext.getString(R.string.no_limitations);
            }
            mView.setLimitationsText(limitationsText);
            mView.showLimitationsArea();
        }
    }

    private void setOriginData(GuineaPigOptionalData optionalData, Settings settings, String unknownText) {
        if (settings.displayOriginField()) {
            String originText = optionalData.getOrigin();
            if (TextUtils.textEmpty(originText)) {
                originText = unknownText;
            }
            mView.setOriginText(originText);
            mView.showOriginArea();
        }
    }

    private void setWeightData(GuineaPigOptionalData optionalData, Settings settings) {
        if (settings.displayWeightField()) {
            mView.setWeightText("" + optionalData.getWeight());
            mView.showWeightArea();
        }
    }

    private void setLastBirthData(GuineaPigOptionalData optionalData, Settings settings, String unknownText) {
        if (settings.displayLastBirthField()) {
            String lastBirthDisplayText = optionalData.getLastBirth();
            if (TextUtils.textEmpty(lastBirthDisplayText)) {
                lastBirthDisplayText = unknownText;
            }
            mView.setLastBirthText(lastBirthDisplayText);
            mView.showLastBirthArea();
        }
    }

    private void setDueDateData(GuineaPigOptionalData optionalData, Settings settings, String unknownText) {
        if (settings.displayDueDateField()) {
            String dueDateDisplayText = optionalData.getDueDate();
            if (TextUtils.textEmpty(dueDateDisplayText)) {
                dueDateDisplayText = unknownText;
            }
            mView.setDueDateText(dueDateDisplayText);
            mView.showDueDateArea();
        }
    }

    private void setCastrationData(GuineaPigOptionalData optionalData, String unknownText) {
        boolean showCastration = (!Gender.Male.equals(mGuineaPig.getGender()) && !Type.BREED.equals(mGuineaPig.getType()))
                || Type.COLLECTOR.equals(mGuineaPig.getType());
        if (showCastration && Settings.getSettings(mContext).displayCastrationField()) {
            mView.showCastrationDateArea();
            String castrationDate = optionalData.getCastrationDate();
            if (TextUtils.textEmpty(castrationDate)) {
                castrationDate = unknownText;
            }
            mView.setCastrationDateText(castrationDate);
        }
    }

    private void askForExternalStorageReadAccess() {
        RxPermissions permissions = RxPermissions.getInstance(mContext);
        permissions.requestEach(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            setPicture(mGuineaPig.getOptionalData().getPicturePath());
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            String rationaleMessage = mContext.getString(R.string.rationale_message_image_display);
                            PermissionDialog dialog = new PermissionDialog(mContext, rationaleMessage, null);
                            dialog.show();
                        }
                    }
                });
    }
}