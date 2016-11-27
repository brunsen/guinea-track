package de.brunsen.guineatrack.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.GuineaPig;

public class JsonWriter {
    private Context mContext;

    public JsonWriter(Context context) {
        mContext = context;
    }

    public JSONArray createJsonArrayFromList(List<GuineaPig> pigs) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (GuineaPig pig : pigs) {
            jsonArray.put(createGuineaJson(pig));
        }
        return jsonArray;
    }

    public JSONObject createGuineaJson(GuineaPig pig) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(mContext.getString(R.string.id_key), pig.getId());
        json.put(mContext.getString(R.string.name_key), pig.getName());
        json.put(mContext.getString(R.string.birth_key), pig.getBirth());
        json.put(mContext.getString(R.string.gender_key), pig.getGender());
        json.put(mContext.getString(R.string.color_key), pig.getColor());
        json.put(mContext.getString(R.string.breed_key), pig.getBreed());
        json.put(mContext.getString(R.string.type_key), pig.getType());
        JSONObject optionalObject = new JSONObject();
        optionalObject.put(mContext.getString(R.string.last_birth_key), pig.getOptionalData().getLastBirth());
        optionalObject.put(mContext.getString(R.string.due_date_key), pig.getOptionalData().getDueDate());
        optionalObject.put(mContext.getString(R.string.weight_key), pig.getOptionalData().getWeight());
        optionalObject.put((mContext.getString(R.string.origin_key)), pig.getOptionalData().getOrigin());
        optionalObject.put(mContext.getString(R.string.limitations_key), pig.getOptionalData().getLimitations());
        optionalObject.put(mContext.getString(R.string.castration_date_key), pig.getOptionalData().getCastrationDate());
        optionalObject.put(mContext.getString(R.string.picture_key), pig.getOptionalData().getPicturePath());
        json.put(mContext.getString(R.string.optional_data_key), optionalObject);
        return json;
    }
}
