package net.trs.onthisday;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static String provider;
	
	static TextView tv;
	static Button btn;
	static EditText edtex;
	static ProgressBar prg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		provider = "http://www.gamquistu.com/stuff/charts/result";
		
		
		tv = (TextView) findViewById(R.id.song_on_day);
		btn = (Button) findViewById(R.id.btnSubmitDate);
		edtex = (EditText) findViewById(R.id.edtxDate);
		prg = (ProgressBar) findViewById(R.id.prgbrLookUpProgress);
	}
	
	public static void lookUpDate(View v){
		String wholeDate = edtex.getText().toString();
		String[] parsedDate = wholeDate.split("/");
		String month = parsedDate[0];
		String date = parsedDate[1];
		String year = parsedDate[2];
		
		tv.setText("Looking for the song of the day for Feb 18, 1978 . . . ");
		
		new TopSongOnDay().execute(
				provider, 
				month, 
				date, 
				year);
	}
	
	public static void songOnDayRes(String songOnDay){

		tv.setText(songOnDay);
	}

}
