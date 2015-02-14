package net.trs.onthisday;

/**
 * Created by steve on 1/10/15.
 * Copyright
 */

        import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class TopSongOnDay extends AsyncTask<String, Void, String>{

    private static final String TAG = "SongOfTheDayBGThread";

    public String doInBackground(String... params){
        Log.v(TAG, "Preparing to send for the song of the day");
        String url   = params[0]; //SoD Service URI
        String month = params[1]; //Month
        String day   = params[2]; //Date
        String year  = params[3]; //Year
        String song  = "Opps! No info Found for that date.";

        Document doc;
        try {
            Log.i(TAG, "Sending for the song of the day");
            doc = Jsoup.connect(url)
                    .data("month", month)
                    .data("day", day)
                    .data("year", year)
                    .post();
            Log.i(TAG, "Request sent");
        } catch (IOException e) {
            Log.e(TAG, "Error getting the song of the day: " + e.getMessage());
            e.printStackTrace();
            return song;
        }


        Element result = null;
        try{
            Log.i(TAG, "Processing Song of the Day Result");
            result = doc.getElementById("result");
            Log.i(TAG, "Extracted result");
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            Log.i(TAG, "Checking result for a song");
            if (result != null) {
                Log.i(TAG, "Got the song of the day");
                song = result.text();
                Log.i(TAG, "Extracted song, ready to send back to the forground");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        Log.i(TAG, "Returning from the background");
        return song;
    }

    protected void onPostExecute(String result){
        Log.i(TAG, "Done, sending info back to the Main Activity");
        try {
            net.trs.onthisday.MainFragment.songOnDayRes(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

