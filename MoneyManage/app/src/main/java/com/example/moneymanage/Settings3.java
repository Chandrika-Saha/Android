package com.example.moneymanage;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.moneymanage.Add2.added_price;


public class Settings3 extends Fragment {

    BarChart barChart;
    GoogleSignInClient mGoogleSignInClient;
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    //Add2 data_for_graph = new Add2();
    DatabaseHelper myDb;
    String personEmail;
    ArrayList<Integer> added_price = new ArrayList<>();
    ArrayList<Integer> added_price_all = new ArrayList<>();
    ArrayList<String> all_dates_combined = new ArrayList<>();
    ArrayList<String> dte = new ArrayList<>();
    Button refresh_graph;
   // BarData barData = new BarData();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings3, container, false);

        barChart = rootView.findViewById(R.id.bargraph);

        refresh_graph = rootView.findViewById(R.id.refresh_bar_button);

        //refresh_graph.setVisibility(View.GONE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            personEmail = acct.getEmail();
            //Toast.makeText(getActivity(),personEmail,Toast.LENGTH_SHORT).show();
        }

        myDb = new DatabaseHelper(this.getActivity());

        FetchFromDB();
        UpdateBar();

        refresh_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FetchFromDB();
                UpdateBar();
                //listAdapter.notifyDataSetChanged();
            }
        });


        return rootView;
    }

    private void FetchFromDB() {

        Cursor data = myDb.getAllData(personEmail);
        added_price = new ArrayList<>();
        dates = new ArrayList<>();
        if(data.getCount() == 0){
            Toast.makeText(getActivity(), "No data found !!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                int temp = Integer.valueOf(data.getString(4));
                dates.add(data.getString(5));
                added_price.add(temp);
            }

        }

    }

    private void UpdateBar() {

        if(added_price.size()!=0) {
            all_dates_combined = new ArrayList<>();
            added_price_all = new ArrayList<>();
            String date = dates.get(0);
            int p = added_price.get(0);
            all_dates_combined.add(date);
            int q = 0;

            for (int i = 1; i < dates.size(); i++) {
                if(date.matches(dates.get(i))){
                    p = p  + added_price.get(i);
                }
                else{

                    added_price_all.add(p);
                    date = dates.get(i);
                    all_dates_combined.add(date);
                    //q = added_price.get(i);
                    p = added_price.get(i);

                }
            }
            added_price_all.add(p);
           // all_dates_combined.add(date);
           // Toast.makeText(getActivity(),String.valueOf( all_dates_combined.size()),Toast.LENGTH_LONG).show();
          //  Toast.makeText(getActivity(),String.valueOf( added_price_all.size()),Toast.LENGTH_LONG).show();


            ArrayList<BarEntry> barEntries = new ArrayList<>();
            dte = new ArrayList<>();
            for(int i=0;i<added_price_all.size();i++)
            {
                //barEntries.add(new BarEntry(added_price_all.get(i),i));
                barEntries.add(new BarEntry(added_price_all.get(i),i));
                dte.add(all_dates_combined.get(i));
                if(i==6)
                    break;
            }


            BarDataSet barDataSet = new BarDataSet(barEntries, "Expenses");

            BarData barData = new BarData(dte, barDataSet);

            barChart.setData(barData);


        }
        else
        {
            Toast.makeText(getActivity(), "No data found !!",Toast.LENGTH_LONG).show();
        }

    }

}