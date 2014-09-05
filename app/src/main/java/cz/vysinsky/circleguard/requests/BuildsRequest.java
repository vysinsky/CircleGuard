package cz.vysinsky.circleguard.requests;

import android.content.Context;
import android.net.Uri;

import cz.vysinsky.circleguard.model.lists.BuildsList;

public class BuildsRequest extends CircleCiRequest<BuildsList> {

    public static final String API_URL = "https://circleci.com/api/v1%s";
    public static final int DEFAULT_LIMIT = 20;
    private Integer limit = DEFAULT_LIMIT;
    private Integer offset = 0;



    public BuildsRequest(Context context) {
        super(BuildsList.class, context);
    }



    @Override
    public BuildsList loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(String.format(API_URL, "/recent-builds"))
                .buildUpon()
                .appendQueryParameter("circle-token", TOKEN)
                .appendQueryParameter("offset", offset.toString())
                .appendQueryParameter("limit", String.valueOf(Math.min(limit, 100)));

        return getRestTemplate().getForObject(uriBuilder.build().toString(), BuildsList.class);
    }



    public String createCacheKey() {
        return "builds-O" + offset + "-L" + limit;
    }



    public void setOffset(int offset) {
        this.offset = offset;
    }



    public void setLimit(int limit) {
        this.limit = limit;
    }
}
