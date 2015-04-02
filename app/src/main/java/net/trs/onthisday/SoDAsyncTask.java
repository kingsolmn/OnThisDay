package net.trs.onthisday;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import net.trs.onthisday.backend.endpointapi.soDApi.SoDApi;

import java.io.IOException;

import static com.google.api.client.extensions.android.http.AndroidHttp.newCompatibleTransport;

/**
 * Created by steve on 4/2/15.
 */
public class SoDAsyncTask extends AsyncTask<Object, Void, String> {
    private static SoDApi sodApiService = null;
    private Context context;

    @Override
    protected String doInBackground(Object... params) {
        if(sodApiService == null){
            SoDApi.Builder builder = new SoDApi.Builder(newCompatibleTransport(), new AndroidJsonFactory(), null)
            .setRootUrl("http://avian-science-88721.appspot.com/_ah/api/");

            sodApiService = builder.build();
        }

        context = (Context) params[0];
        String monthString = (String) params[1];
        String dateString = (String) params[2];
        String yearString = (String) params[3];

        try{
            return sodApiService.getSoD("", "", "").execute();//.getData();
        }catch(IOException e){
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result){
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}
