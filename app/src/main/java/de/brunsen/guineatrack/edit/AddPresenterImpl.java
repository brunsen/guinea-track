package de.brunsen.guineatrack.edit;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.GuineaPigOptionalData;
import de.brunsen.guineatrack.model.Type;
import de.brunsen.guineatrack.util.TextUtils;

public class AddPresenterImpl extends BaseEditPresenterImpl {

    private String mDefaultDateText;

    protected AddPresenterImpl(Context context) {
        super(context);
        DateFormat dateFormat = DateFormat.getDateInstance();
        mDefaultDateText = dateFormat.format(new Date());
        mGuineaPig = new GuineaPig();
        mGuineaPig.setBirth(mDefaultDateText);
        mCopyGuineaPig = new GuineaPig();
        mCopyGuineaPig.setBirth(mDefaultDateText);
    }

    @Override
    public void storeWithCrud() {
        try {
            mCrud.storeGuineaPig(mCopyGuineaPig);
            mEditView.finish();
        } catch (Exception e) {
            Toast.makeText(mContext, mContext.getString(R.string.save_error_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected String getTitle() {
        return mContext.getString(R.string.title_activity_guinea_pig_add);
    }

    @Override
    protected String getLeaveMessage() {
        return mContext.getString(R.string.message_unsaved_guinea_pig);
    }

    @Override
    public boolean unStoredChanges() {
        boolean empty = true;
        empty &= TextUtils.textEmpty(mCopyGuineaPig.getName());
        String currentDateText = mCopyGuineaPig.getBirth();
        empty &= currentDateText.equals(mDefaultDateText) || TextUtils.textEmpty(currentDateText);
        empty &= TextUtils.textEmpty(mCopyGuineaPig.getColor());
        empty &= TextUtils.textEmpty(mCopyGuineaPig.getBreed());
        Gender selectedGender = mCopyGuineaPig.getGender();
        Type selectedType = mCopyGuineaPig.getType();
        empty &= selectedGender.equals(mGuineaPig.getGender());
        empty &= selectedType.equals(mCopyGuineaPig.getType());
        GuineaPigOptionalData copyOptionalData = mCopyGuineaPig.getOptionalData();
        if (selectedGender == Gender.Female) {
            empty &= TextUtils.textEmpty(copyOptionalData.getLastBirth());
            empty &= TextUtils.textEmpty(copyOptionalData.getDueDate());
        }
        if (!Gender.Male.equals(selectedGender) && !Type.BREED.equals(selectedType)) {
            empty &= TextUtils.textEmpty(copyOptionalData.getCastrationDate());
        }
        empty &= copyOptionalData.getWeight() == 0;
        empty &= TextUtils.textEmpty(copyOptionalData.getLimitations());
        empty &= TextUtils.textEmpty(copyOptionalData.getOrigin());
        empty &= TextUtils.textEmpty(copyOptionalData.getPicturePath());
        return !empty;
    }
}
