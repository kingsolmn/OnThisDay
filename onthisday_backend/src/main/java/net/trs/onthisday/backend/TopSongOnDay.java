package net.trs.onthisday.backend;

/**
 * Created by steve on 1/10/15.
 * Copyright
 */


public class TopSongOnDay {

    private static final String TAG = "SongOfTheDayBGThread";

    public String run(String... params){
//        Log.v(TAG, "Preparing to send for the song of the day");
        String url   = "http://www.gamquistu.com/stuff/charts/result";//params[0]; //SoD Service URI
        String month = "February"; //params[1]; //Month
        String day   = "18"; //params[2]; //Date
        String year  = "1978"; //params[3]; //Year
        String song  = "Opps! No info Found for that date.";


        org.jsoup.nodes.Document doc;
        try {
//            Log.i(TAG, "Sending for the song of the day");
            doc = org.jsoup.Jsoup.connect(url)
                    .data("month", month)
                    .data("day", day)
                    .data("year", year)
                    .post();
//            Log.i(TAG, "Request sent");
        } catch (java.io.IOException e) {
//            Log.e(TAG, "Error getting the song of the day: " + e.getMessage());
            e.printStackTrace();
            return song;
        }


        org.jsoup.nodes.Element result = null;
        try{
//            Log.i(TAG, "Processing Song of the Day Result");
            result = doc.getElementById("result");
//            Log.i(TAG, "Extracted result");
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
//            Log.i(TAG, "Checking result for a song");
            if (result != null) {
//                Log.i(TAG, "Got the song of the day");
                song = result.text();
//                Log.i(TAG, "Extracted song, ready to send back to the forground");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

//        Log.i(TAG, "Returning from the background");
        return song;
//    }

//    protected void onPostExecute(String result){
//        Log.i(TAG, "Done, sending info back to the Main Activity");
//        try {
//            net.trs.onthisday.MainFragment.songOnDayRes(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}

