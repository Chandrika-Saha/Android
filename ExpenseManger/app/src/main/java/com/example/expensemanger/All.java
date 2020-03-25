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


public class All extends Fragment implements AdapterView.OnItemSelectedListener {

    EditText name_in,description_in,price_in;
    String name,description,category_s,payment_s;
    int price;
    Button button;
    TextView dateDisplay;
    String date;
    private DatePickerDialog.OnDateSetListener datePickerDialog;
    String personEmail;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseHelper myDb;
    Add2 update_list = new Add2();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.all, container, false);
        name_in = rootView.findViewById(R.id.namein);
        description_in = rootView.findViewById(R.id.descriptionin);
        price_in = rootView.findViewById(R.id.pricein);
        button = rootView.findViewById(R.id.addexpensebutton);
        dateDisplay = rootView.findViewById(R.id.datein);
        myDb = new DatabaseHelper(this.getActivity());


        Spinner cat = rootView.findViewById(R.id.expensein);
        ArrayAdapter<CharSequence> cat_data = ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.categories,
                android.R.layout.simple_spinner_item);
        cat_data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat.setAdapter(cat_data);
        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                category_s = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getActivity(),category_s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner pay = rootView.findViewById(R.id.payin);
        ArrayAdapter<CharSequence> pay_data = ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.payment,
                android.R.layout.simple_spinner_item);
        pay_data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pay.setAdapter(pay_data);
        pay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                payment_s = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getActivity(),payment_s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonPressAdd();
            }
        });
        dateDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePress();
            }
        });
        datePickerDialog = new DatePickerDialog.OnDateSetListener()
        {
            public void onDateSet(DatePicker datepicker,int year,int month,int day){
                month = month+1;
                String date1 = day + "/"+month+"/"+year;
                dateDisplay.setText(date1);
                date = year+"-"+month+"-"+day;
        }
        };

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
        return rootView;
    }

    private void DatePress() {
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                datePickerDialog,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void ButtonPressAdd() {
        name = name_in.getText().toString();
        description = description_in.getText().toString();
        String p = price_in.getText().toString();
        if(p.isEmpty() || description.isEmpty() || name.isEmpty() || date == null){
            Toast.makeText(getActivity(),"Please Enter All Data",Toast.LENGTH_SHORT).show();
        }else{
            price = Integer.valueOf(p);
            boolean isInserted = myDb.insertData(personEmail,category_s,name,description,price,date,payment_s);
            if(isInserted)
            {
                Toast.makeText(getActivity(),"Data Inserted Successfully",Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(getActivity(),"ERROR Inserting Data",Toast.LENGTH_SHORT).show();
            }

        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
