package com.example.expensemanger;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Calendar;


public class Income extends Fragment {

    EditText income_in,type_in;
    TextView date_income;
    String type,date;
    int amount;
    Button button;
    String personEmail;
    private DatePickerDialog.OnDateSetListener datePickerDialogincome;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseHelper myDb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.income, container, false);
        income_in = rootView.findViewById(R.id.incomein);
        type_in = rootView.findViewById(R.id.income_type);

        myDb = new DatabaseHelper(this.getActivity());
        button = rootView.findViewById(R.id.addincomebutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonPressAddIncome();
            }
        });

        date_income = rootView.findViewById(R.id.dateincome);
        date_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePressIncome();
            }
        });
        datePickerDialogincome = new DatePickerDialog.OnDateSetListener()
        {
            public void onDateSet(DatePicker datepicker,int year,int month,int day){
                month = month+1;
                String date1 = day + "/"+month+"/"+year;
                date_income.setText(date1);
                date = year+"-"+month+"-"+day;
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            personEmail = acct.getEmail();
        }


        return rootView;
    }

    private void ButtonPressAddIncome() {
        type = type_in.getText().toString();
        String amnt = income_in.getText().toString();
        if(type.isEmpty() || amnt.isEmpty() || date == null){
            Toast.makeText(getActivity(),"Please Enter All Data",Toast.LENGTH_SHORT).show();
        }else{
            amount = Integer.valueOf(amnt);
            boolean isInserted = myDb.insertDataIncome(personEmail,type,date, amount);
            if(isInserted)
            {
                Toast.makeText(getActivity(),"Data Inserted Successfully",Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(getActivity(),"ERROR Inserting Data",Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void DatePressIncome() {
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                datePickerDialogincome,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
