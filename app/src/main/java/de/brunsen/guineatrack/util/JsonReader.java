package de.brunsen.guineatrack.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.Type;

public class JsonReader {

    private Context mContext;

    public JsonReader(Context context) {
        mContext = context;
    }

    public List<GuineaPig> getGuineaListFromString(String jsonString) throws JSONException {
        List<GuineaPig> restoredPigs = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        int arraySize = jsonArray.length();
        for (int i = 0; i < arraySize; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            GuineaPig restoredPig = getGuineaPigFromJson(jsonObject);
            restoredPigs.add(restoredPig);
        }
        return restoredPigs;
    }

    private GuineaPig getGuineaPigFromJson(JSONObject jsonObject) throws JSONException {
        GuineaPig restoredPig = new GuineaPig();
        if (jsonObject.has(mContext.getString(R.string.id_key)))
            restoredPig.setId(jsonObject.getInt(mContext.getString(R.string.id_key)));
        if (jsonObject.has(mContext.getString(R.string.name_key)))
            restoredPig.setName(jsonObject.getString(mContext.getString(R.string.name_key)));
        if (jsonObject.has(mContext.getString(R.string.birth_key)))
            restoredPig.setBirth(jsonObject.getString(mContext.getString(R.string.birth_key)));
        if (jsonObject.has(mContext.getString(R.string.gender_key)))
            restoredPig.setGender(Gender.valueOf(jsonObject.getString(mContext.getString(R.string.gender_key))));
        if (jsonObject.has(mContext.getString(R.string.color_key)))
            restoredPig.setColor(jsonObject.getString(mContext.getString(R.string.color_key)));
        if (jsonObject.has(mContext.getString(R.string.breed_key)))
            restoredPig.setBreed(jsonObject.getString(mContext.getString(R.string.breed_key)));
        if (jsonObject.has(mContext.getString(R.string.breed_key_old)))
            restoredPig.setBreed(jsonObject.getString(mContext.getString(R.string.breed_key_old)));
        if (jsonObject.has(mContext.getString(R.string.type_key)))
            restoredPig.setType(Type.valueOf(jsonObject.getString(mContext.getString(R.string.type_key))));

        getOptionalDataFromJson(jsonObject, restoredPig);
        return restoredPig;
    }

    private void getOptionalDataFromJson(JSONObject jsonObject, GuineaPig restoredPig) throws JSONException {
        if (jsonObject.has(mContext.getString(R.string.last_birth_key)))
            restoredPig.getOptionalData().setLastBirth(jsonObject.getString(mContext.getString(R.string.last_birth_key)));
        if (jsonObject.has(mContext.getString(R.string.picture_key)))
            restoredPig.getOptionalData().setPicturePath(jsonObject.getString(mContext.getString(R.string.picture_key)));

        if (jsonObject.has(mContext.getString(R.string.optional_data_key))) {
            JSONObject optionalData = jsonObject.getJSONObject(mContext.getString(R.string.optional_data_key));
            if (optionalData.has(mContext.getString(R.string.weight_key))) {
                try {
                    restoredPig.getOptionalData().setWeight(optionalData.getInt(mContext.getString(R.string.weight_key)));
                } catch (JSONException e) {
                    restoredPig.getOptionalData().setWeight(0);
                }
            }
            if (optionalData.has(mContext.getString(R.string.origin_key)))
                restoredPig.getOptionalData().setOrigin(optionalData.getString((mContext.getString(R.string.origin_key))));
            if (optionalData.has(mContext.getString(R.string.castration_date_key)))
                restoredPig.getOptionalData().setCastrationDate(optionalData.getString(mContext.getString(R.string.castration_date_key)));
            if (optionalData.has(mContext.getString(R.string.limitations_key)))
                restoredPig.getOptionalData().setLimitations(optionalData.getString(mContext.getString(R.string.limitations_key)));
            if (optionalData.has(mContext.getString(R.string.last_birth_key)))
                restoredPig.getOptionalData().setLastBirth(optionalData.getString(mContext.getString(R.string.last_birth_key)));
            if (optionalData.has(mContext.getString(R.string.due_date_key)))
                restoredPig.getOptionalData().setDueDate(optionalData.getString(mContext.getString(R.string.due_date_key)));
            if (optionalData.has(mContext.getString(R.string.picture_key)))
                restoredPig.getOptionalData().setPicturePath(optionalData.getString(mContext.getString(R.string.picture_key)));
            if (optionalData.has(mContext.getString(R.string.entry_key)))
                restoredPig.getOptionalData().setEntry(optionalData.getString(mContext.getString(R.string.entry_key)));
            if (optionalData.has(mContext.getString(R.string.departure_key)))
                restoredPig.getOptionalData().setDeparture(optionalData.getString(mContext.getString(R.string.departure_key)));
        }
    }

}
