package com.example.expensemanger;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.Calendar;


public class Settings3 extends Fragment {

    TextView total_expense,total_income, month_expense, month_income,total_balance, month_balance;
    BarChart barChart;
    int tot_e,tot_m_e,tot_i,tot_m_i,tot_b,tot_m_b;
    GoogleSignInClient mGoogleSignInClient;
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    DatabaseHelper myDb;
    String personEmail;
    ArrayList<Integer> added_price = new ArrayList<>();
    ArrayList<Integer> added_price_all = new ArrayList<>();
    ArrayList<String> all_dates_combined = new ArrayList<>();
    ArrayList<Integer> all_amounts = new ArrayList<>();
    ArrayList<String> all_dates = new ArrayList<>();
    ArrayList<String> all_types = new ArrayList<>();
    ArrayList<Integer> all_id = new ArrayList<>();
    ArrayList<String> dte = new ArrayList<>();
    ArrayList<Integer> added_amounts = new ArrayList<>();
    ArrayList<String> all_dates_combined_income = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings3, container, false);

        barChart = rootView.findViewById(R.id.bargraph);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            personEmail = acct.getEmail();
        }

        total_expense = rootView.findViewById(R.id.total);
        total_income = rootView.findViewById(R.id.totalincome);
                month_expense = rootView.findViewById(R.id.totalmonth);
                        month_income = rootView.findViewById(R.id.totalmonthincome);
                                total_balance = rootView.findViewById(R.id.totalremain);
                                        month_balance = rootView.findViewById(R.id.totalmonthremain);


        myDb = new DatabaseHelper(this.getActivity());

        FetchFromDB();
        UpdateBar();
        UpdateText();

        return rootView;
    }

    private void FetchFromDB() {
        Cursor data = myDb.getAllData(personEmail);
        added_price = new ArrayList<>();
        dates = new ArrayList<>();
        tot_e = 0;
        tot_i = 0;
        tot_b = 0;

        if (data.getCount() == 0) {
            Toast.makeText(getActivity(), "No data found !!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                int temp = Integer.valueOf(data.getString(5));
                dates.add(data.getString(6));
                added_price.add(temp);
                tot_e = tot_e + temp;
            }
        }
        all_amounts = new ArrayList<>();
        all_dates = new ArrayList<>();
        all_types = new ArrayList<>();
        all_id = new ArrayList<>();
        Cursor datai = myDb.getAllDataIncome(personEmail);
        if (datai == null)
            Toast.makeText(getActivity(), "No data found !!", Toast.LENGTH_LONG).show();
        else {
            while (datai.moveToNext()) {
                all_types.add(datai.getString(2));
                all_amounts.add(Integer.valueOf(datai.getString(4)));
                all_dates.add(datai.getString(3));
                all_id.add(Integer.valueOf(datai.getString(0)));
                tot_i = tot_i + Integer.valueOf(datai.getString(4));
            }
        }
        tot_b = tot_i - tot_e;
        total_expense.setText(String.valueOf(tot_e));
        total_income.setText(String.valueOf(tot_i));
        total_balance.setText(String.valueOf(tot_b));
    }

    private void UpdateBar() {
        if(added_price.size()!=0) {
            all_dates_combined = new ArrayList<>();
            added_price_all = new ArrayList<>();
            String date = dates.get(0);
            int p = added_price.get(0);
            all_dates_combined.add(date);

            for (int i = 1; i < dates.size(); i++) {
                if(date.matches(dates.get(i))){
                    p = p  + added_price.get(i);
                }
                else{
                    added_price_all.add(p);
                    date = dates.get(i);
                    all_dates_combined.add(date);
                    p = added_price.get(i);
                }
            }
            added_price_all.add(p);
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            dte = new ArrayList<>();
            for(int i=0;i<added_price_all.size();i++)
            {
                barEntries.add(new BarEntry(added_price_all.get(i),i));
                dte.add(all_dates_combined.get(i));
                if(i==30)
                    break;
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, "Expenses");
            barDataSet.setColor(Color.parseColor("#4d4dff"));
            BarData barData = new BarData(dte, barDataSet);

            barChart.setData(barData);

        }
        else
        {
            Toast.makeText(getActivity(), "No data found !!",Toast.LENGTH_LONG).show();
        }
    }

    private void UpdateText()
    {
        tot_m_b = 0;
        tot_m_i = 0;
        tot_m_e = 0;
        added_amounts = new ArrayList<>();
        all_dates_combined_income = new ArrayList<>();

        if (all_amounts.size() == 0 || all_dates_combined.size()==0) {
            Toast.makeText(getActivity(), "No data found !!", Toast.LENGTH_LONG).show();
        } else {
            String date = all_dates.get(0);
            int p = all_amounts.get(0);
            all_dates_combined_income.add(date);
            for (int i = 1; i < all_dates.size(); i++) {
                if (date.matches(all_dates.get(i))) {
                    p = p + all_amounts.get(i);
                } else {
                    added_amounts.add(p);
                    date = all_dates.get(i);
                    all_dates_combined_income.add(date);
                    p = all_amounts.get(i);
                }
            }
            added_amounts.add(p);

            Calendar calender = Calendar.getInstance();
            int year = calender.get(Calendar.YEAR);
            int month = calender.get(Calendar.MONTH) + 1;

            String[] splitted = new String[3];
            if (added_amounts.size() == 0) {
                //Toast.makeText(getActivity(), "No data found !!", Toast.LENGTH_LONG).show();
            }
            else {
                for (int i = 0; i < all_dates_combined_income.size(); i++) {
                    splitted = all_dates_combined_income.get(i).split("-");
                    if (splitted[0].matches(String.valueOf(year)) && splitted[1].matches(String.valueOf(month))) {
                        tot_m_i = tot_m_i + added_amounts.get(i);
                    }
                }
            }
            for(int i=0;i<all_dates_combined.size();i++)
            {
                splitted = all_dates_combined.get(i).split("-");
                if(splitted[0].matches(String.valueOf(year))&&splitted[1].matches(String.valueOf(month))){
                    tot_m_e = tot_m_e + added_price.get(i);
                }
            }
            tot_m_b = tot_m_i - tot_m_e;
            month_expense.setText(String.valueOf(tot_m_e));
            month_income.setText(String.valueOf(tot_m_i));
            month_balance.setText(String.valueOf(tot_m_b));
            //Toast.makeText(getActivity(),splitted[0]+"   "+splitted[1],Toast.LENGTH_LONG).show();
        }
    }

}