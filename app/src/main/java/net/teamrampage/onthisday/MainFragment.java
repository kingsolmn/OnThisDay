package net.teamrampage.onthisday;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.RelativeLayout;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainFragment extends Fragment {
    private static final String TAG = "OnThisDay - Fragment";
    private static String provider;
    private static String month;
    private static String date;
    private static String year;

    private static Context context;
    private static TextView tv;
    private static Button btn;
    private static ProgressBar prg;
    private static EditText edtex;
    private static Calendar cal;
    private static DatePickerDialog datePicker;
    private static DatePickerDialog.OnDateSetListener dtPickListener;
    private static RelativeLayout lLayout;
    private static FragmentActivity faActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        faActivity = (FragmentActivity) super.getActivity();
        lLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_main, container, false);

        com.google.android.gms.ads.AdView mAdView = (com.google.android.gms.ads.AdView) lLayout.findViewById(R.id.adView);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return lLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        context = super.getActivity();
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

        tv = (TextView) lLayout.findViewById(R.id.song_on_day);
        btn = (Button) lLayout.findViewById(R.id.btnSubmitDate);
        edtex = (EditText) lLayout.findViewById(R.id.edtxDate);
        prg = (ProgressBar) lLayout.findViewById(R.id.prgbrLookUpProgress);

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
}
