package com.example.expensemanger;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.Calendar;

public class Add2 extends Fragment {

    DatabaseHelper myDb;
    ListView listView;
    ListView listViewCombined;
    RelativeLayout view;
    RelativeLayout viewCombined;
    Button refresh_button;
    Button shortlist_button;
    GoogleSignInClient mGoogleSignInClient;

    ArrayAdapter shortlistAdapter;
    String personEmail;
    int total;
    TextView total_view;
    //TextView total_view_7;
    TextView total_month;
    //private CompleteListAdapter listAdapter;
    public static ArrayList<Integer> all_prices = new ArrayList<>();
    public static ArrayList<String> all_dates = new ArrayList<>();
    public static ArrayList<String> all_cat = new ArrayList<>();
    public static ArrayList<String> all_name = new ArrayList<>();
    public static ArrayList<String> all_des = new ArrayList<>();
    ArrayList<String> all_pay = new ArrayList<>();
    public static ArrayList<Integer> added_price = new ArrayList<>();
    public static ArrayList<String> all_dates_combined = new ArrayList<>();
    public static final int MENU_REMOVE = 2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //FragmentActivity activity = getActivity();
        View rootView = inflater.inflate(R.layout.add2, container, false);
        listView = rootView.findViewById(R.id.listView);
        listViewCombined = rootView.findViewById(R.id.listViewcombined);
        shortlist_button = rootView.findViewById(R.id.shortlistbutton);
        listViewCombined.setVisibility(View.GONE);
        myDb = new DatabaseHelper(this.getActivity());
        refresh_button = rootView.findViewById(R.id.refreshbutton);
       // total_view = rootView.findViewById(R.id.total);
       // total_view_7 =rootView.findViewById(R.id.total7);
        view = rootView.findViewById(R.id.list);
        viewCombined = rootView.findViewById(R.id.listC);
        //total_month = rootView.findViewById(R.id.totalmonth);
        registerForContextMenu(listView);

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

        refresh_button.setBackgroundColor(Color.parseColor("#6D9BF1"));
        shortlist_button.setBackgroundColor(Color.parseColor("#6D9BF1"));
        addToList();
        updateTotal();

        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shortlist_button.setBackgroundColor(Color.parseColor("#436EEE"));
                refresh_button.setBackgroundColor(Color.parseColor("#6D9BF1"));
                addToList();
                updateTotal();
                //listAdapter.notifyDataSetChanged();
            }
        });
        shortlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refresh_button.setBackgroundColor(Color.parseColor("#436EEE"));
                shortlist_button.setBackgroundColor(Color.parseColor("#6D9BF1"));
                addShortList();
                //listAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }


    private void addShortList() {
        listView.setVisibility(View.GONE);
        listViewCombined.setVisibility(View.VISIBLE);
        ArrayList<String> theList = new ArrayList<>();
        theList.add("Date\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTotal");
        for(int i=0;i<all_dates_combined.size();i++) {
            theList.add(all_dates_combined.get(i) + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + added_price.get(i));
            shortlistAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, theList) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    // Get the current item from ListView
                    View view = super.getView(position, convertView, parent);
                    if (position % 2 == 1) {
                        // Set a background color for ListView regular row/item
                        view.setBackgroundColor(Color.parseColor("#ffcccccc"));
                    } else {
                        // Set the background color for alternate row/item
                        view.setBackgroundColor(Color.parseColor("#ffffffff"));
                    }
                    return view;
                }
            };
            listViewCombined.setAdapter(shortlistAdapter);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, MENU_REMOVE, Menu.NONE, "Remove");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()) {
            case MENU_REMOVE:
                delete_item(index);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void delete_item(int item) {

        int index = item;

        Integer deletedRows = myDb.deleteData(personEmail,all_cat.get(index),
                all_name.get(index),all_des.get(index),all_prices.get(index),all_dates.get(index),all_pay.get(index));
        if(deletedRows > 0)
            Toast.makeText(getActivity(),"Data Deleted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getActivity(),"Data not Deleted",Toast.LENGTH_LONG).show();
        addToList();
        updateTotal();
    }


    public void addToList() {

        listView.setVisibility(View.VISIBLE);
        listViewCombined.setVisibility(View.GONE);
        ArrayList<String> theList = new ArrayList<>();
        ArrayAdapter listAdapter;
        Cursor data = myDb.getAllData(personEmail);
            total = 0;
            all_prices = new ArrayList<>();
            all_dates = new ArrayList<>();
            all_cat = new ArrayList<>();
            all_name = new ArrayList<>();
            all_des = new ArrayList<>();
            all_pay = new ArrayList<>();
            //listAdapter = new ArrayAdapter();
        if(data == null)
            theList.add("Nothing found");
        else{
            while(data.moveToNext()) {
                theList.add("Date: " + data.getString(6) + "\nCategory: " +
                        data.getString(2) + "\nName: " +
                        data.getString(3) + " ( " +
                        data.getString(4) + " )\nPrice: " +
                        data.getString(5) + "\nPayment Method: " +
                        data.getString(7));
                all_cat.add(data.getString(2));
                all_des.add(data.getString(4));
                all_name.add(data.getString(3));
                all_pay.add(data.getString(7));
                int temp = Integer.valueOf(data.getString(5));
                total = total + temp;
                all_prices.add(temp);
                all_dates.add(data.getString(6));

            }
                listAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,theList){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        // Get the current item from ListView
                        View view = super.getView(position,convertView,parent);
                        if(position %2 == 1)
                        {
                            // Set a background color for ListView regular row/item
                            view.setBackgroundColor(Color.parseColor("#ffcccccc"));
                        }
                        else
                        {
                            // Set the background color for alternate row/item
                            view.setBackgroundColor(Color.parseColor("#ffffffff"));
                        }
                        return view;
                    }
                };
                listView.setAdapter(listAdapter);
            }
//            total_view.setText(String.valueOf(total));


    }

    public void updateTotal() {
        added_price = new ArrayList<>();
        all_dates_combined = new ArrayList<>();

        if(all_prices.size()== 0) {
            Toast.makeText(getActivity(), "No data found !!", Toast.LENGTH_LONG).show();
        }
        else {

            String date = all_dates.get(0);
            int p = all_prices.get(0);
            all_dates_combined.add(date);

            for (int i = 1; i < all_dates.size(); i++) {
                if (date.matches(all_dates.get(i))) {
                    p = p + all_prices.get(i);
                } else {

                    added_price.add(p);
                    date = all_dates.get(i);
                    all_dates_combined.add(date);
                    p = all_prices.get(i);
                }
            }
            added_price.add(p);
            int seven = 0;
            for (int i = 0; i < added_price.size(); i++) {
                seven = seven + added_price.get(i);
                if (i == 6)
                    break;

            }

           // total_view_7.setText(String.valueOf(seven));
        }

        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH)+1;
        int thismonth=0;
        String[] splitted = new String[2];
        for(int i=0;i<all_dates_combined.size();i++)
        {
            splitted = all_dates_combined.get(i).split("-");
            if(splitted[0].matches(String.valueOf(year))&&splitted[1].matches(String.valueOf(month))){
                thismonth = thismonth + added_price.get(i);
            }
        }
        //total_month.setText(String.valueOf(thismonth));
       // Toast.makeText(getActivity(),splitted[0]+"   "+splitted[1],Toast.LENGTH_LONG).show();

    }


    }

