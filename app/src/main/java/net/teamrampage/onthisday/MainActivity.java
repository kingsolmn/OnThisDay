package net.teamrampage.onthisday;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;


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
    private static LinearLayout lLayout;
    private static FragmentActivity faActivity;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        faActivity = (FragmentActivity) super.getActivity();
        lLayout = (LinearLayout) inflater.inflate(R.activity_main, container, false);

        return lLayout;
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
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
        btn.setVisibility(View.INVISIBLE);
        new TopSongOnDay().execute(
                provider,
                month,
                date,
                year);
    }

    public static void songOnDayRes(String songOnDay){
        Log.i(TAG, "songOnDayRes");
        prg.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.VISIBLE);
        tv.setText(songOnDay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
