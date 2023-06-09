package com.example.hotelbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.hotelbooking.homepage.adapter.ListViewAdapter;
import com.example.hotelbooking.Collector.Collector;
import com.example.hotelbooking.filter.api.ApiService;
import com.example.hotelbooking.filter.model.ProvicesOutFit;
import com.example.hotelbooking.homepage.api.ApiClient;
import com.example.hotelbooking.homepage.model.HomepageListApiResponse;
import com.example.hotelbooking.homepage.adapter.HomepageListAdapter;
import com.example.hotelbooking.homepage.api.ApiHomepage;
import com.example.hotelbooking.homepage.model.HomepageList;
import com.example.hotelbooking.hotelinformation.SliderAdapter;
import com.example.hotelbooking.hotelinformation.SliderItem;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private HomepageListAdapter homepageListAdapter;
//    private ListViewAdapter mlistviewAdapter;
    private RecyclerView hp_listitem;
    private ArrayList<HomepageList> mListHomePage;
    //ArrayList<Homepage> homepages;
    public static final String SHARED_PREFS = "bookingApp";
    public static final String TRAVELLER = "traveller";
    private ViewPager2 viewPager;
    private DatePickerDialog datePickerDialogHp;
    private DatePickerDialog datePickerDialogHp1;
    private Button checkInButton;
    private Button checkOutButton;
    private TextView customerNameTxtView;
    private String customerName;
    private Button btnSearch;
    TextView textView;
    Dialog dialog;
    EditText editText;
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter3;
    private Handler sliderHandler = new Handler();


    private String token;

//    private static final String[] paths = {
//            "PASSENGER",
//            "2 người lớn",
//            "2 người lớn 1 trẻ em",
//            "2 người lớn 2 trẻ em",
//            "3 người lớn",
//            "3 người lớn 1 trẻ em",
//            "4 người lớn"};

//    private static final String[] paths = {
//            "2 người lớn",
//            "2 người lớn 1 trẻ em",
//            "2 người lớn 2 trẻ em",
//            "3 người lớn",
//            "3 người lớn 1 trẻ em",
//            "4 người lớn"
//    };

    private static final Map<String, String> pathMap;

    static {
        pathMap = new HashMap<>();
        pathMap.put("2 người lớn", "2");
        pathMap.put("2 người lớn 1 trẻ em", "3");
        pathMap.put("2 người lớn 2 trẻ em", "4");
        pathMap.put("3 người lớn", "3");
        pathMap.put("3 người lớn 1 trẻ em", "4");
        pathMap.put("4 người lớn", "4");

    }


    private Spinner spinner;

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);


        //item payment
        Spinner spinner = findViewById(R.id.spinner);
        final ArrayAdapter<CharSequence>[] adapter = new ArrayAdapter[]{ArrayAdapter.createFromResource(this, R.array.lang, R.layout.payment_spinner_item)};
        adapter[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter[0]);
        spinner.setSelected(false);
        spinner.setSelection(0,true);
        spinner.setOnItemSelectedListener(this);

        //id customer
        customerNameTxtView = findViewById(R.id.customerNameTxtView);
        setData();

        customerNameTxtView.setText(customerName);
        customerNameTxtView.setOnClickListener( view -> startActivity(new Intent(HomePageActivity.this, ProfileActivity.class)));

        btnSearch = findViewById(R.id.btnSearch);

        arrayList =new ArrayList<>();
//        callApiListProvinces(arrayList);

        //banner
        viewPager = findViewById(R.id.pager);
        List<SliderItem> sliderItemArrayList = new ArrayList<>();
        sliderItemArrayList.add(new SliderItem(R.drawable.home_amanoi2));
        sliderItemArrayList.add(new SliderItem(R.drawable.home_amina));
        sliderItemArrayList.add(new SliderItem(R.drawable.home_banyan));
        sliderItemArrayList.add(new SliderItem(R.drawable.home_majestic));
        sliderItemArrayList.add(new SliderItem(R.drawable.home_six_senses));
        sliderItemArrayList.add(new SliderItem(R.drawable.home_topas));

        viewPager.setAdapter(new SliderAdapter(sliderItemArrayList, viewPager));
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(5);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(250));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager.setPageTransformer(compositePageTransformer);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);//Slide Duration 3 sec
            }
        });

        //check in - out
        initDatePicker();
        checkInButton = findViewById(R.id.checkInButton);
        checkInButton.setText(getTodaysDate());
        checkOutButton = findViewById(R.id.checkOutButton);
        checkOutButton.setText(getTodaysDate());

        spinner = (Spinner) findViewById(R.id.btnpsg);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(HomePageActivity.this,
                R.layout.spinner_item, pathMap.keySet().toArray(new String[0]));
        adapter[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("selected" + adapter2.getItem(position));
                String selectedKey = adapter2.getItem(position);
                Collector.traveller = pathMap.get(selectedKey);
                System.out.println("selected " + pathMap.get(selectedKey));
                saveData(Integer.parseInt(pathMap.get(selectedKey)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //SearchBar
        arrayList =new ArrayList<>();
        callApiListProvinces(arrayList);

        textView = findViewById(R.id.search_barhomepage);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dialog = new Dialog(HomePageActivity.this);
                 dialog.setContentView(R.layout.dialog_searchble_spinner);
                 dialog.getWindow().setLayout(650,800);
                 dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 dialog.show();
                 editText = dialog.findViewById(R.id.edit_search_filter);
                 listView = dialog.findViewById(R.id.search_list_name_hotel);


                 adapter3 = new ArrayAdapter<>(HomePageActivity.this, android.R.layout.simple_list_item_1,arrayList);
                 listView.setAdapter(adapter3);
                 editText.addTextChangedListener(new TextWatcher() {
                     @Override
                     public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                     @Override
                     public void onTextChanged(CharSequence s, int start, int before, int count) {
                         adapter3.getFilter().filter(s);
                     }
                     @Override
                     public void afterTextChanged(Editable s) {}
                 });
                 listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                         textView.setText(adapter3.getItem(position));
                         Collector.prv= adapter3.getItem(position);
                         System.out.println(Collector.prv);
                         dialog.dismiss();
                     }
                 });
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Collector.prv==null) Toast.makeText(HomePageActivity.this,"Province Blank",Toast.LENGTH_SHORT).show();
                else if (Collector.traveller==null) Toast.makeText(HomePageActivity.this,"Traveller Blank",Toast.LENGTH_SHORT).show();
                else if (compareDate(Collector.ci,Collector.co)) Toast.makeText(HomePageActivity.this,"Check Out Error",Toast.LENGTH_SHORT).show();
                else nextPageHotelList();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        Log.d("token at home page", token);
    }

    //Call Api
    public void populateHomepage(){
        ApiClient.getClient().create(ApiHomepage.class).getHomepage().enqueue(new Callback<HomepageListApiResponse>() {
            @Override
            public void onResponse(Call<HomepageListApiResponse> call, Response<HomepageListApiResponse> response) {
                System.out.println("SUCCESS");
                if (response.code() == 200){
                        mListHomePage.addAll(response.body().getData());
                        homepageListAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<HomepageListApiResponse> call, Throwable t) {
                System.out.println("error");
            }
        });
    }

    public void setData(){
        SharedPreferences sharedPreferences = getSharedPreferences(PaymentActivity.SHARED_PREFS,MODE_PRIVATE);
        customerName = sharedPreferences.getString(PaymentActivity.CUSTOMER_NAME, "");
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            checkInButton.setText(date);
            String checkIn=makeDayToCallApi(day,month,year);
            Collector.ci=checkIn;
            System.out.println(Collector.ci);
        };
        DatePickerDialog.OnDateSetListener dateSetListener1 = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            String checkOut=makeDayToCallApi(day,month,year);
            checkOutButton.setText(date);
            Collector.co=checkOut;
            System.out.println(Collector.co);
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        cal.set(year,month,day);

        datePickerDialogHp = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialogHp.getDatePicker().setMinDate(cal.getTimeInMillis());
        datePickerDialogHp1 = new DatePickerDialog(this, style, dateSetListener1, year, month, day);
        datePickerDialogHp1.getDatePicker().setMinDate(cal.getTimeInMillis());
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
        datePickerDialogHp.show();
    }
    public void openCheckOutPicker(View view)
    {
        datePickerDialogHp1.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {return;}
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {return;}
    public static boolean compareDate(String ci,String co){
        Date date1 = Date.valueOf(ci);
        Date date2 = Date.valueOf(co);

        if (date2.equals(date1)) return false;
        else if (date2.after(date1)) return false;
        else return true;

    }
    public void nextPageHotelList(){
        Intent intent=new Intent(this,HotelListActivity.class);
        startActivity(intent);
    }

    private void callApiListProvinces(ArrayList arrayList){
        ApiService.apiService.provinces().enqueue(new Callback<ProvicesOutFit>() {
            @Override
            public void onResponse(Call<ProvicesOutFit> call, Response<ProvicesOutFit> response) {
//                Toast.makeText(HomePageActivity.this, "Call Api Success", Toast.LENGTH_SHORT).show();
                ProvicesOutFit data = response.body();
                for (int i=0;i<data.getData().size();i++) {
                    System.out.println(data.getData().get(i).getId());
                    arrayList.add(data.getData().get(i).getId());
                }
            }
            @Override
            public void onFailure(Call<ProvicesOutFit> call, Throwable t) {
                Toast.makeText(HomePageActivity.this, "Call Api Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void saveData(int traveler){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putString(CHECK_IN, checkIn);
//        editor.putString(CHECK_OUT, checkOut);
//        System.out.println(checkIn+"++++++"+checkOut);
        editor.putInt(TRAVELLER, traveler);
//        editor.putString(HOTEL_NAME, "LOTUS RESIDENCE - Landmark 81 Vinhomes Central Park");
//        editor.putFloat(RATING, new Float(4.8));
//        editor.putString(ROOM_TYPE, "Phòng đơn");
//        editor.putFloat(PRICE, new Float(125.0));
//        editor.putFloat(TAX, new Float(10.0));
//        editor.putFloat(SERVICE_FEE,new Float(10.0));
//        editor.putInt(ROOM_TYPE_ID, 1);
//        editor.putString(PHONE, "0325542310");
//        editor.putInt(USER_ID, 6);
//        editor.putString(USERNAME,"viet pham");
//        editor.putInt(HOTEL_ID, 1);
//        editor.putInt(QUANTITY, 1);
//        editor.putString(HOTEL_IMG_URL, "https://media-cdn.tripadvisor.com/media/photo-s/23/ca/38/3a/au-lac-charner-hotel.jpg");
//        editor.putString(CUSTOMER_NAME, "Viet Pham");
        editor.apply();
    }
}