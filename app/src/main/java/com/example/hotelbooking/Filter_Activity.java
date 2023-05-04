package com.example.hotelbooking;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbooking.Collector.Collector;
import com.example.hotelbooking.filter.api.ApiService;
import com.example.hotelbooking.filter.model.ProvicesOutFit;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Filter_Activity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    ListView listView;

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog datePickerDialog1;
    private Button checkInButtonSearch;
    private Button checkOutButtonSearch;

    private Button btnNextPage;

    Dialog dialog;

    String[] nameHotelList=new String[]{"Amanoi","Furama","La Vela","Lotte","Muong Thanh","Preidot Grand","Bel Marina","Phuong","Nhat","Nhung"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        textView=findViewById(R.id.search_bar);
        //btnNextPage=findViewById(R.id.btnNextHotelList);
        initDatePicker();
        checkInButtonSearch = findViewById(R.id.checkInButtonSearch);
        checkInButtonSearch.setText(getTodaysDate());
        checkOutButtonSearch = findViewById(R.id.checkOutButtonSearch);
        checkOutButtonSearch.setText(getTodaysDate());

        arrayList =new ArrayList<>();
        //arrayList.add(String.valueOf(nameHotelList));

//        arrayList.add("Amanoi");
//        arrayList.add("Furama");
//        arrayList.add("La Vela");
//        arrayList.add("Lotte");
//        arrayList.add("Muong Thanh");
//        arrayList.add("Preidot Grand");
//        arrayList.add("Bel Marina");

        callApiListProvinces(arrayList);


//        btnNextPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(Filter_Activity.this, HotelList.class);
//                startActivity(intent);
//            }
//
//        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Filter_Activity.this);
                dialog.setContentView(R.layout.dialog_searchble_spinner);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                editText=dialog.findViewById(R.id.edit_search_filter);
                listView=dialog.findViewById(R.id.search_list_name_hotel);

                adapter=new ArrayAdapter<>(Filter_Activity.this, android.R.layout.simple_list_item_1,arrayList);

                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        textView.setText(adapter.getItem(position));
                        Collector.prv=adapter.getItem(position);
                        System.out.println(Collector.prv);
                        dialog.dismiss();
                    }
                });


            }
        });
    }
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                checkInButtonSearch.setText(date);
                System.out.println(datePickerDialog.getDatePicker().getDayOfMonth());
                String datetocall= makeDayToCallApi(day,month,year);
                Collector.ci=makeDayToCallApi(day,month,year);
                System.out.println(Collector.ci);

            }

        };
        DatePickerDialog.OnDateSetListener dateSetListener1 = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date1 = makeDateString(day, month, year);
                checkOutButtonSearch.setText(date1);
                Collector.co=makeDayToCallApi(day,month,year);
                System.out.println(Collector.co);


            }

        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        cal.set(year,month,day);


        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        datePickerDialog1 = new DatePickerDialog(this, style, dateSetListener1, year, month, day);
        datePickerDialog1.getDatePicker().setMinDate(cal.getTimeInMillis());

        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String makeDayToCallApi(int day, int month, int year){
        return year+"-"+month+"-"+day;
    }


    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openCheckInPicker(View view)
    {
        datePickerDialog.show();
    }
    public void openCheckOutPicker(View view)
    {
        datePickerDialog1.show();
    }

    private void callApiListProvinces(ArrayList arrayList){
        ApiService.apiService.provinces().enqueue(new Callback<ProvicesOutFit>() {
            @Override
            public void onResponse(Call<ProvicesOutFit> call, Response<ProvicesOutFit> response) {
                Toast.makeText(Filter_Activity.this, "Call Api Success", Toast.LENGTH_SHORT).show();
                ProvicesOutFit data = response.body();
                for (int i=0;i<data.getData().size();i++) {
                    System.out.println(data.getData().get(i).getName());
                    arrayList.add(data.getData().get(i).getName());
                }
            }

            @Override
            public void onFailure(Call<ProvicesOutFit> call, Throwable t) {
                Toast.makeText(Filter_Activity.this, "Call Api Error", Toast.LENGTH_SHORT).show();

            }
        });
    }




}