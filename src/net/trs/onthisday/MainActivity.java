package net.trs.onthisday;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "OnThisDay";
	private static String provider;
	private static String month;
	private static String date;
	private static String year;
	
	private static Context context;
	private static TextView tv;
	private static Button btn;
	private static EditText edtex;
	private static ProgressBar prg;
	private static Calendar cal;
	private static DatePickerDialog datePicker;
	private static DatePickerDialog.OnDateSetListener dtPickListener;

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
		Log.i(TAG, "onResume");
		context = this;
		provider = "http://www.gamquistu.com/stuff/charts/result";
		
		cal = Calendar.getInstance(TimeZone.getDefault());	
		
		dtPickListener = new DatePickerDialog.OnDateSetListener() {			
			@Override
			public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
				Log.v(TAG, "onDateSet");
				year = String.valueOf(selectedYear);
				month = String.valueOf(monthOfYear);
				date = String.valueOf(dayOfMonth);
				edtex.setText((new DateFormatSymbols().getMonths()[Integer.valueOf(month)]) + " " + date + ", " + year);
			}
		};	

		datePicker = new DatePickerDialog(
				context,
				dtPickListener,
				cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
				
		datePicker.setCancelable(false);
		datePicker.setTitle("Select a Date");				
		datePicker.show();
				
		tv = (TextView) findViewById(R.id.song_on_day);
		btn = (Button) findViewById(R.id.btnSubmitDate);
		edtex = (EditText) findViewById(R.id.edtxDate);
		prg = (ProgressBar) findViewById(R.id.prgbrLookUpProgress);
		
		Log.v(TAG, "Setting the EditText OnFocusChangeListener");
		edtex.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus){
				Log.v(TAG, "EditText onFocusChange");
				if(v instanceof EditText || hasFocus){
					Log.v(TAG, "hasFocus true");					
					datePicker.show();
				}else{
					Log.v(TAG, "hasFocus false");
					if(datePicker.isShowing()){
						Log.v(TAG, "DatePickerDialog showing");
						datePicker.dismiss();
					}
				}
			}
		});
		
		edtex.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Log.v(TAG, "EditText onClick");
				if(v instanceof EditText){
					Log.v(TAG, "is EditText");					
					datePicker.show();
				}
			}
			
		});
		Log.v(TAG, "onResume Done");
	}
	
	public static void lookUpDate(View v){
		Log.i(TAG, "lookUpDate");
		tv.setText("Looking up the song of the day for \n" + (new DateFormatSymbols().getMonths()[Integer.valueOf(month)]) + " " + date + ", " + year + " . . . ");
		prg.setVisibility(View.VISIBLE);
		new TopSongOnDay().execute(
				provider, 
				month, 
				date, 
				year);
	}
	
	public static void songOnDayRes(String songOnDay){
		Log.i(TAG, "songOnDayRes");
		prg.setVisibility(View.INVISIBLE);
		tv.setText(songOnDay);
	}

}
