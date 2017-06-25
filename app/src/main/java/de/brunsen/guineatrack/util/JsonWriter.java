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

    public JSONArray createJsonArrayFromList(List<GuineaPig> guineaPigs) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (GuineaPig pig : guineaPigs) {
            jsonArray.put(createGuineaJson(pig));
        }
        return jsonArray;
    }

    public JSONObject createGuineaJson(GuineaPig guineaPig) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(mContext.getString(R.string.id_key), guineaPig.getId());
        json.put(mContext.getString(R.string.name_key), guineaPig.getName());
        json.put(mContext.getString(R.string.birth_key), guineaPig.getBirth());
        json.put(mContext.getString(R.string.gender_key), guineaPig.getGender());
        json.put(mContext.getString(R.string.color_key), guineaPig.getColor());
        json.put(mContext.getString(R.string.breed_key), guineaPig.getBreed());
        json.put(mContext.getString(R.string.type_key), guineaPig.getType());
        JSONObject optionalObject = new JSONObject();
        optionalObject.put(mContext.getString(R.string.last_birth_key), guineaPig.getOptionalData().getLastBirth());
        optionalObject.put(mContext.getString(R.string.due_date_key), guineaPig.getOptionalData().getDueDate());
        optionalObject.put(mContext.getString(R.string.weight_key), guineaPig.getOptionalData().getWeight());
        optionalObject.put((mContext.getString(R.string.origin_key)), guineaPig.getOptionalData().getOrigin());
        optionalObject.put(mContext.getString(R.string.limitations_key), guineaPig.getOptionalData().getLimitations());
        optionalObject.put(mContext.getString(R.string.castration_date_key), guineaPig.getOptionalData().getCastrationDate());
        optionalObject.put(mContext.getString(R.string.picture_key), guineaPig.getOptionalData().getPicturePath());
        optionalObject.put(mContext.getString(R.string.entry_key), guineaPig.getOptionalData().getEntry());
        optionalObject.put(mContext.getString(R.string.departure_key), guineaPig.getOptionalData().getDeparture());
        json.put(mContext.getString(R.string.optional_data_key), optionalObject);
        return json;
    }
}
