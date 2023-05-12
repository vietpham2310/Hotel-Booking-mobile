package com.example.hotelbooking.hotelListp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelbooking.R;
import com.example.hotelbooking.hotelListp.model.Hotel;

import java.util.ArrayList;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.HotelListHoler>{
    private final ArrayList<Hotel> listHotel;
    private static Context context;

    public HotelListAdapter(Context context, ArrayList<Hotel> listHotel) {
        this.context= context;
        this.listHotel = listHotel;
    }
    @NonNull
    @Override
    public HotelListHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hotel_list_item,parent,false);
        return new HotelListHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelListHoler holder, int position) {
        holder.blind(listHotel.get(position));

    }

    @Override
    public int getItemCount() {
        return listHotel.size();
    }

    public static class HotelListHoler extends RecyclerView.ViewHolder{
        private final TextView txtNameHotel;
        private final TextView txtRating;
        private final TextView txtNumrating;
        private final TextView txtProvice;
        private final TextView txtAddress;
        private final TextView txtPhone;
        private final TextView txtPrice;
        private  ImageView imgViewHotel;
        public HotelListHoler(@NonNull View itemView) {
            super(itemView);
            txtNameHotel=itemView.findViewById(R.id.txtNameHotel);
            txtRating = itemView.findViewById(R.id.txtRatingHotel);
            txtNumrating = itemView.findViewById(R.id.txtNumRatingHotel);
            txtProvice = itemView.findViewById(R.id.txtProviceHotel);
            txtAddress=itemView.findViewById(R.id.txtAddressHotel);
            txtPhone=itemView.findViewById(R.id.txtPhoneHotel);
            txtPrice= itemView.findViewById(R.id.txtPriceHotel);
            imgViewHotel=itemView.findViewById(R.id.imgViewHotel);
        }
        public void blind(Hotel hotel){
            txtNameHotel.setText(hotel.getName());
            txtRating.setText(String.valueOf(hotel.getRating()));
            txtNumrating.setText("( "+String.valueOf(hotel.getNumRating())+" reviews)");
            txtProvice.setText(hotel.getProvinceId());
            txtAddress.setText(hotel.getAddress());
            txtPhone.setText(hotel.getPhone());
            Glide.with(context).load(hotel.getAvatar()).into(imgViewHotel);
        }
    }
}
