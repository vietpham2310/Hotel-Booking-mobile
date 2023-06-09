package com.example.hotelbooking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.hotelbooking.constant.Constant;
import com.example.hotelbooking.hotelinformation.adapter.CommentAdapter;
import com.example.hotelbooking.hotelinformation.adapter.RoomAdapter;
import com.example.hotelbooking.hotelinformation.api.Api;
import com.example.hotelbooking.hotelinformation.api.Appclient;
import com.example.hotelbooking.hotelinformation.model.Comments;
import com.example.hotelbooking.hotelinformation.model.CommentsOutfit;
import com.example.hotelbooking.hotelinformation.model.HotelOutfit;
import com.example.hotelbooking.hotelinformation.model.ImageOutFit;
import com.example.hotelbooking.hotelinformation.model.Room;
import com.example.hotelbooking.hotelinformation.model.RoomOutFit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelInformationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    public static final String SHARED_PREFS = "bookingApp";
    public static final String CHECK_IN = "checkin";
    public static final String CHECK_OUT = "checkout";
    public static final String HOTEL_NAME = "hotel_name";
    public static final String RATING = "rating";
    public static final String ROOM_TYPE = "room_type";
    public static final String PRICE = "price";
    public static final String ROOM_TYPE_ID = "room_type_id";
    public static final String QUANTITY = "quantity";
    public static final String HOTEL_ID = "hotel_id";
    public static final String HOTEL_IMG_URL = "hotel_img_url";
    public static final String CUSTOMER_NAME = "customer_name";
    private String customerName;
    private int idHotel;
    private String checkIn;
    private String checkOut;
    private TextView txtNoiDung;
    private Button btnDescription;
    private Button btnCustomerService;
    private Button btnSafety;
    private TextView txtNameHotelInf;
    private TextView txtRatingHotelInf;
    private TextView txtNumRatingHotelInf;
    private TextView txtProvinceHotelInf;
    private TextView txtNameHotelInfSub;
    private TextView txtAddressHotelInf;
    private TextView customerNameTxtView;
    private TextView removeHomePage;
    private RecyclerView rcvRoomList;
    private RoomAdapter roomAdapter;
    private ArrayList<Room> mRoomList;
    private RecyclerView rcvCommentList;
    private CommentAdapter commentAdapter;
    private ArrayList<Comments> mCommnetList;

    private ImageSlider imgSlider;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_information);

        showDataToConsole();


        //Declare

            //Navigation-Bar
            removeHomePage=findViewById(R.id.txtTripGuide);
            customerNameTxtView = findViewById(R.id.customerNameTxtView);
            Spinner spinner = findViewById(R.id.spinner);

            //Header
                txtNameHotelInf=findViewById(R.id.txtNameHotelInf);
                txtRatingHotelInf=findViewById(R.id.txtRatingHotelInf);
                txtNumRatingHotelInf=findViewById(R.id.txtNumRatingHotelInf);
                txtProvinceHotelInf=findViewById(R.id.txtProviceHotelInf);

            //Body
                txtNameHotelInfSub=findViewById(R.id.txtNameHotelInfSub);
                txtAddressHotelInf=findViewById(R.id.txtAddressHotelInf);

            //View ImageHotel
                imgSlider=findViewById(R.id.img_slider);

            //Description-Features-Roomandprice
                btnDescription =  findViewById(R.id.btnDescription);
                btnCustomerService =  findViewById(R.id.btnCustomerService);
                btnSafety = findViewById(R.id.btnSafety);
                txtNoiDung = findViewById(R.id.textDescription);

            //Room
                rcvRoomList=findViewById(R.id.rcvRoomList);

            //Comment
                rcvCommentList=findViewById(R.id.rcvCommentList);


        //Navigation-Bar

            //Btn-RemoveHomePage
                removeHomePage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeHomePage();
                    }
                });

            //Customer-Name
                customerNameTxtView.setText(customerName);
                customerNameTxtView.setOnClickListener(view -> {
                    openProfile();
                });

            //Language
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lang, R.layout.payment_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setSelected(false);
                spinner.setSelection(0,true);
                spinner.setOnItemSelectedListener(this);


        //Description-Features-Roomandprice

        btnCustomerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCustomerService.setTextColor(Color.parseColor("#3F72AF"));
                btnSafety.setTextColor(Color.parseColor("#99000000"));
                btnDescription.setTextColor(Color.parseColor("#99000000"));
                txtNoiDung.setText("Hotel is all about people and service provision. A Hotel that puts its customer’s needs first always have a good reputation. Guests want hotels where they are given personalized services. When they get such kind of hotels, they can come back over and over again because they cannot forget the experience they had at that particular hote");
            }
        });

        btnSafety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSafety.setTextColor(Color.parseColor("#3F72AF"));
                btnDescription.setTextColor(Color.parseColor("#99000000"));
                btnCustomerService.setTextColor(Color.parseColor("#99000000"));
                txtNoiDung.setText("For many, a hotel functions as a home away from home. With that comes a hefty expectation for the most diligent safety and security measures. Many hotels now focus on providing personalized safety and security measures for different guest profiles such as women, children, and the elderly.");
            }
        });

        btnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDescription.setTextColor(Color.parseColor("#3F72AF"));
                btnSafety.setTextColor(Color.parseColor("#99000000"));
                btnCustomerService.setTextColor(Color.parseColor("#99000000"));
                txtNoiDung.setText("Claiming a spectacular stretch of Vietnam’s coastline withinthe verdant embrace of Nui Chua National Park and UNESCO Biosphere Reserve,Amanoi is a natural paradise overlooking Vinh Hy Bay. From its remote location - a rich and diverse mosaic of ecosystems – the resort’s clifftop restaurants and pool, lakeside AmanSpa and private golden sand beach, offer limitless opportunities for outdoor exploration, cultural immersion and serene time out.");
            }
        });


        //Image-Slider-Hotel
        callApiImageHotel(idHotel);

        //Rooms
        mRoomList=new ArrayList<Room>();
        rcvRoomList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        roomAdapter=new RoomAdapter(HotelInformationActivity.this,mRoomList);
        rcvRoomList.setAdapter(roomAdapter);
        callApiRoomInHotel(idHotel,checkIn,checkOut);

        //Comments
        mCommnetList=new ArrayList<>();
        rcvCommentList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentAdapter=new CommentAdapter(HotelInformationActivity.this,mCommnetList);
        rcvCommentList.setAdapter(commentAdapter);
        callApiCommentInHotel(idHotel,5,1);

        //Call-API
//        System.out.println(idHotel+"mmmm");

        callApiHotelInformation(idHotel);
//        callApiRoomInHotel(1,"2023-04-16","2023-04-20");
//        callApiHotelInformation(1);
//        callApiCommentInHotel(1,5,1);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}


    private void callApiHotelInformation(int id){
        Appclient.getClient().create(Api.class).getHotelInformation(id).enqueue(new Callback<HotelOutfit>() {
            @Override
            public void onResponse(Call<HotelOutfit> call, Response<HotelOutfit> response) {
                HotelOutfit hotel=response.body();
                if (hotel!=null) {
                    txtNameHotelInf.setText(hotel.getData().getName());
                    txtNameHotelInfSub.setText(hotel.getData().getName());
                    txtRatingHotelInf.setText(String.valueOf((double) Math.round(hotel.getData().getRating())));
                    txtNumRatingHotelInf.setText("( " + String.valueOf(hotel.getData().getNumRating()) + " reviews)");
                    txtProvinceHotelInf.setText(hotel.getData().getProvinceId());
                    txtAddressHotelInf.setText(hotel.getData().getAddress());
                    saveData(hotel.getData().getName(),Float.valueOf(String.valueOf((double)Math.round(hotel.getData().getRating()))),hotel.getData().getId(), Constant.HOST+hotel.getData().getAvatar());
//                    if(hotel.getData().getRoomTypes()!=null) {
//                        mRoomTypesList.addAll(hotel.getData().getRoomTypes());
//                        roomTypesAdapter.notifyDataSetChanged();
//                        Toast.makeText(HotelInformationActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
//                        System.out.println("Success1");
//                    } else System.out.println("error1");
                    Toast.makeText(HotelInformationActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
                    System.out.println("Success");
                }
//                else System.out.println("error");
            }
            @Override
            public void onFailure(Call<HotelOutfit> call, Throwable t) {
                System.out.println("Error");
            }
        });
    }
    private void callApiImageHotel(int id){
        Appclient.getClient().create(Api.class).getImage(id).enqueue(new Callback<ImageOutFit>() {
            @Override
            public void onResponse(Call<ImageOutFit> call, Response<ImageOutFit> response) {
                Toast.makeText(HotelInformationActivity.this,"Success",Toast.LENGTH_SHORT).show();
                ArrayList<SlideModel> images = new ArrayList<>();
                ImageOutFit image=response.body();
                for (int i=0;i<image.getData().size();i++) {
                    //System.out.println("http://14.225.255.238/booking"+image.getData().get(i).getUrl());
                    images.add(new SlideModel(Constant.HOST+image.getData().get(i).getUrl(),null));
                    imgSlider.setImageList(images);
                }

            }

            @Override
            public void onFailure(Call<ImageOutFit> call, Throwable t) {
                Toast.makeText(HotelInformationActivity.this,"Error",Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void callApiRoomInHotel(int id, String ci, String co){
        Appclient.getClient().create(Api.class).getRoomInHotel(id, ci, co).enqueue(new Callback<RoomOutFit>() {
            @Override
            public void onResponse(Call<RoomOutFit> call, Response<RoomOutFit> response) {
                Toast.makeText(HotelInformationActivity.this,"Success",Toast.LENGTH_SHORT).show();
                RoomOutFit room = response.body();
                for(int i=0;i<room.getData().size();i++){
                    if(room.getData().get(i).getQuantity()>0){
                        mRoomList.add(room.getData().get(i));
                        roomAdapter.notifyDataSetChanged();
//                       Collector.priceRoom = new ArrayList<>();
//                       Collector.priceRoom.add(i,String.valueOf(room.getData().get(i).getPrice()));
//                       System.out.println(Collector.priceRoom);

                    }
                }

//               for (int i = 0; i < Collector.priceRoom.size(); i++) {
//                   System.out.println(Collector.priceRoom.get(i));}
//               if(Collector.priceRoom==null) System.out.println("NUll");
            }

            @Override
            public void onFailure(Call<RoomOutFit> call, Throwable t) {
                Toast.makeText(HotelInformationActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void callApiCommentInHotel(int id, int size, int page){
        Appclient.getClient().create(Api.class).getCommentsHotel(id,page,size).enqueue(new Callback<CommentsOutfit>() {
            @Override
            public void onResponse(Call<CommentsOutfit> call, Response<CommentsOutfit> response) {
                Toast.makeText(HotelInformationActivity.this,"Success",Toast.LENGTH_SHORT).show();
                CommentsOutfit comment= response.body();
                mCommnetList.addAll(comment.getData().getData());
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CommentsOutfit> call, Throwable t) {
                Toast.makeText(HotelInformationActivity.this,"Error",Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void saveData(String hotelName, Float rating,int idHotel,String hotelImgUrl){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HOTEL_NAME, hotelName);
        editor.putFloat(RATING, rating);
        editor.putInt(HOTEL_ID, idHotel);
        editor.putString(HOTEL_IMG_URL, hotelImgUrl);
        editor.apply();
    }


    public void showDataToConsole(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
//        price = sharedPreferences.getFloat(PRICE, 0f);
//        System.out.println(price + "+++++++++++");
        idHotel= sharedPreferences.getInt(HOTEL_ID,0);
        checkIn = sharedPreferences.getString(CHECK_IN, "");
        checkOut = sharedPreferences.getString(CHECK_OUT, "");
        customerName = sharedPreferences.getString(CUSTOMER_NAME, "");
        System.out.println(idHotel+"llllllll");
    }


    public void openProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    public void removeHomePage(){
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

}