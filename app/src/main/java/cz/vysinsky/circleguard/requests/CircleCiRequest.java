package cz.vysinsky.circleguard.requests;

import android.content.Context;
import android.preference.PreferenceManager;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public abstract class CircleCiRequest<RESULT> extends SpringAndroidSpiceRequest<RESULT> {

    public static final String KEY_PREF_API_TOKEN = "pref_api_token";
    public static String TOKEN;



    public CircleCiRequest(Class<RESULT> c, Context context) {
        super(c);
        TOKEN = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_PREF_API_TOKEN, null);
    }

}
