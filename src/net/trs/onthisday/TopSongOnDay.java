package net.trs.onthisday;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.os.AsyncTask;
import android.util.Log;

public class TopSongOnDay extends AsyncTask<String, Void, String>{

	private static final String TAG = "SongOfTheDayBGThread";
	
	public String doInBackground(String... params){
		Log.v(TAG, "Preparing to send for the song of the day");
		String url   = params[0]; //SoD Service URI
		String month = params[1]; //Month
		String day   = params[2]; //Date
		String year  = params[3]; //Year
		String song  = "Opps! No info Found for that date.";
		
		Document doc = null;
		try {
			Log.i(TAG, "Sending for the song of the day");
			doc = Jsoup.connect(url)
					.data("month", month)
					.data("day", day)
					.data("year", year)
					.post();
		} catch (IOException e) {
			Log.e(TAG, "Error getting the song of the day: " + e.getMessage());
			e.printStackTrace();
			return song;
		}
		Element result = doc.getElementById("result");
		
		if (result != null) {
			Log.i(TAG, "Got the song of the day");
			song = result.text();
		}
		
		return song;
	}
	
	protected void onPostExecute(String result){
		Log.i(TAG, "Done, sending info back to the Main Activity");
		MainActivity.songOnDayRes(result);
	}
}
