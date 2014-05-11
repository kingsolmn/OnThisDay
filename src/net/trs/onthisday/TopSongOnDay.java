package net.trs.onthisday;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.os.AsyncTask;

public class TopSongOnDay extends AsyncTask<String, Void, String>{

	public String doInBackground(String... params){
		String url   = params[0]; //SoD Service URI
		String month = params[1]; //Month
		String day   = params[2]; //Date
		String year  = params[3]; //Year
		String song  = "Opps! No info Found for that date.";
		
		Document doc = null;
		try {
			doc = Jsoup.connect(url)
					.data("month", month)
					.data("day", day)
					.data("year", year)
					.post();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element result = doc.getElementById("result");
		
		if (result != null) {
			song = result.text();
		}
		
		return song;
	}
	
	protected void onPostExecute(String result){
		MainActivity.songOnDayRes(result);
	}
}
