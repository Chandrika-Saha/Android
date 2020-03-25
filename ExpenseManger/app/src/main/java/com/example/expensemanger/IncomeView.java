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

public class IncomeView extends Fragment {

    DatabaseHelper myDb;
    ListView listView, listViewCombined;
    RelativeLayout view, viewCombined;
    Button refresh_button;
    Button shortlist_button;
    GoogleSignInClient mGoogleSignInClient;
    ArrayAdapter shortlistAdapter;
    String personEmail;
    ArrayList<Integer> all_amounts = new ArrayList<>();
    ArrayList<Integer> all_id = new ArrayList<>();
    ArrayList<String> all_dates = new ArrayList<>();
    ArrayList<String> all_types = new ArrayList<>();
    ArrayList<Integer> added_amounts = new ArrayList<>();
    ArrayList<String> all_dates_combined = new ArrayList<>();
    public static final int MENU_REMOVE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.incomeview, container, false);
        listView = rootView.findViewById(R.id.listViewi);
        listViewCombined = rootView.findViewById(R.id.listViewcombinedi);
        shortlist_button = rootView.findViewById(R.id.shortlistbuttoni);
        refresh_button = rootView.findViewById(R.id.refreshbuttoni);
        view = rootView.findViewById(R.id.listi);
        viewCombined = rootView.findViewById(R.id.listCi);

        listViewCombined.setVisibility(View.GONE);

        myDb = new DatabaseHelper(this.getActivity());

        registerForContextMenu(listView);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            personEmail = acct.getEmail();
        }

        refresh_button.setBackgroundColor(Color.parseColor("#6D9BF1"));
        shortlist_button.setBackgroundColor(Color.parseColor("#6D9BF1"));
        addToListi();
        updateTotali();

        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortlist_button.setBackgroundColor(Color.parseColor("#436EEE"));
                refresh_button.setBackgroundColor(Color.parseColor("#6D9BF1"));
                addToListi();
                updateTotali();
            }
        });

        shortlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_button.setBackgroundColor(Color.parseColor("#436EEE"));
                shortlist_button.setBackgroundColor(Color.parseColor("#6D9BF1"));
                addShortListi();
            }
        });

        return rootView;
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

        Integer deletedRows = myDb.deleteDataIncome(all_id.get(index));
        if (deletedRows > 0)
            Toast.makeText(getActivity(), "Data Deleted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getActivity(), "Data not Deleted", Toast.LENGTH_LONG).show();
        addToListi();
        updateTotali();
    }


    public void addToListi() {
        listView.setVisibility(View.VISIBLE);
        listViewCombined.setVisibility(View.GONE);
        ArrayList<String> theList = new ArrayList<>();
        ArrayAdapter listAdapter;
        Cursor data = myDb.getAllDataIncome(personEmail);
        all_amounts = new ArrayList<>();
        all_dates = new ArrayList<>();
        all_types = new ArrayList<>();
        all_id = new ArrayList<>();

        if (data == null)
            theList.add("Nothing found");
        else {
            while (data.moveToNext()) {
                theList.add("Date: " + data.getString(3) + "\nIncome Type: " +
                        data.getString(2) + "\nAmount: " +
                        data.getString(4));
                all_types.add(data.getString(2));
                all_amounts.add(Integer.valueOf(data.getString(4)));
                all_dates.add(data.getString(3));
                all_id.add(Integer.valueOf(data.getString(0)));
            }
            listAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, theList) {
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
            listView.setAdapter(listAdapter);
        }

    }

    public void updateTotali() {
        added_amounts = new ArrayList<>();
        all_dates_combined = new ArrayList<>();

        if (all_amounts.size() == 0) {
            Toast.makeText(getActivity(), "No data found !!", Toast.LENGTH_LONG).show();
        } else {
            String date = all_dates.get(0);
            int p = all_amounts.get(0);
            all_dates_combined.add(date);
            for (int i = 1; i < all_dates.size(); i++) {
                if (date.matches(all_dates.get(i))) {
                    p = p + all_amounts.get(i);
                } else {
                    added_amounts.add(p);
                    date = all_dates.get(i);
                    all_dates_combined.add(date);
                    p = all_amounts.get(i);
                }
            }
            added_amounts.add(p);

            Calendar calender = Calendar.getInstance();
            int year = calender.get(Calendar.YEAR);
            int month = calender.get(Calendar.MONTH) + 1;
            int thismonth = 0;
            String[] splitted = new String[2];
            for (int i = 0; i < all_dates_combined.size(); i++) {
                splitted = all_dates_combined.get(i).split("-");
                if (splitted[0].matches(String.valueOf(year)) && splitted[1].matches(String.valueOf(month))) {
                    thismonth = thismonth + added_amounts.get(i);
                }
            }
            //total_month.setText(String.valueOf(thismonth));
            //Toast.makeText(getActivity(),splitted[0]+"   "+splitted[1],Toast.LENGTH_LONG).show();
        }
    }


    private void addShortListi() {
        listView.setVisibility(View.GONE);
        listViewCombined.setVisibility(View.VISIBLE);
        ArrayList<String> theList = new ArrayList<>();
        theList.add("Date\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTotal");
        for (int i = 0; i < all_dates_combined.size(); i++) {
            theList.add(all_dates_combined.get(i) + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + added_amounts.get(i));
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

}

