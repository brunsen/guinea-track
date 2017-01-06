package de.brunsen.guineatrack.edit;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.overview.OverviewActivity;

public class EditPresenterImpl extends BaseEditPresenterImpl {

    public EditPresenterImpl(Context context, int guineaPigId) {
        super(context);
        mGuineaPig = mCrud.getGuineaPigForId(guineaPigId);
        mCopyGuineaPig = new GuineaPig();
        mCopyGuineaPig.setId(mGuineaPig.getId());
        mCopyGuineaPig.setName(mGuineaPig.getName());
        mCopyGuineaPig.setBirth(mGuineaPig.getBirth());
        mCopyGuineaPig.setBreed(mGuineaPig.getBreed());
        mCopyGuineaPig.setColor(mGuineaPig.getColor());
        mCopyGuineaPig.setType(mGuineaPig.getType());
        mCopyGuineaPig.setGender(mGuineaPig.getGender());
        mCopyGuineaPig.setOptionalData(mGuineaPig.getOptionalData());
    }

    @Override
    public void storeWithCrud() {
        try {
            mCrud.updateGuineaPig(mCopyGuineaPig);
            Intent intent = new Intent(mContext, OverviewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, mContext.getString(R.string.update_error_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected String getTitle() {
        return mContext.getString(R.string.title_activity_edit);
    }

    @Override
    protected String getLeaveMessage() {
        return mContext.getString(R.string.message_unsaved_update);
    }

    @Override
    public boolean unStoredChanges() {
        return !mCopyGuineaPig.equals(mGuineaPig);
    }
}
