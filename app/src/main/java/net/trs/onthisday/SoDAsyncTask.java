package net.trs.onthisday;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import net.trs.onthisday.backend.endpointapi.soDApi.SoDApi;
import net.trs.onthisday.backend.endpointapi.soDApi.model.SoD;

import java.io.IOException;

import static com.google.api.client.extensions.android.http.AndroidHttp.newCompatibleTransport;

/**
 * Created by steve on 4/2/15.
 */
public class SoDAsyncTask extends AsyncTask<Object, Void, String> {
    private static SoDApi sodApiService = null;
    private Context context;
    String[] results;

    @Override
    protected String doInBackground(Object... params) {
        if(sodApiService == null){
            SoDApi.Builder builder = new SoDApi.Builder(newCompatibleTransport(), new AndroidJsonFactory(), null)
            .setRootUrl("https://nimble-unison-90718.appspot.com/_ah/api/");

            sodApiService = builder.build();
        }

        context = (Context) params[0];
        String monthString = null;
        try {
            monthString = net.trs.onthisday.backend.Month.of(Integer.parseInt(params[1].toString())).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dateString = (String) params[2];
        String yearString = (String) params[3];
        SoD result;
        results = new String[2];

        try{
            result = sodApiService.getSoD(monthString, dateString, yearString).execute();
            android.util.Log.d("TAG", result.toString());
        }catch(IOException e){
            android.util.Log.d("TAG", e.getMessage());
            return e.getMessage();
        }

        results[0] = (result.getSongName() == null ? "" : result.getSongName());
        results[1] = (result.getArtName() == null ? "There was an error getting the info!" : result.getArtName());

        return null;
    }

    @Override
    protected void onPostExecute(String result){
        MainFragment.songOnDayRes(results);
    }
}
