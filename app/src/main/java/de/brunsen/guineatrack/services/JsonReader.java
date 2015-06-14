package de.brunsen.guineatrack.services;

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

    public GuineaPig getGuineaPigFromString(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        return getGuineaPigFromJson(jsonObject);
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
        if (jsonObject.has(mContext.getString(R.string.race_key)))
            restoredPig.setRace(jsonObject.getString(mContext.getString(R.string.race_key)));
        if (jsonObject.has(mContext.getString(R.string.type_key)))
            restoredPig.setType(Type.valueOf(jsonObject.getString(mContext.getString(R.string.type_key))));
        if (jsonObject.has(mContext.getString(R.string.last_birth_key)))
            restoredPig.setLastBirth(jsonObject.getString(mContext.getString(R.string.last_birth_key)));
        if (jsonObject.has(mContext.getString(R.string.picture_key)))
            restoredPig.setPicturePath(jsonObject.getString(mContext.getString(R.string.picture_key)));
        return restoredPig;
    }

}
