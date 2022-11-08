package com.toan.musicapp.AdapterClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.toan.musicapp.DAOClass.DAO_Album;
import com.toan.musicapp.MainActivity;
import com.toan.musicapp.Model.Albums;
import com.toan.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TimKiemAlbum_rvAdapter extends RecyclerView.Adapter<TimKiemAlbum_rvAdapter.TimKiemAlbum_ViewHolder>{
    private Context context;
    private ArrayList<Albums> dsalbums;
    private int maxCount = 0;

    public TimKiemAlbum_rvAdapter(Context context, int maxCount) {
        this.context = context;
        this.dsalbums = new ArrayList<>();
        this.maxCount = maxCount;
    }

    @NonNull
    @Override
    public TimKiemAlbum_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimKiemAlbum_ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_timkiem_album, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TimKiemAlbum_ViewHolder holder, int position) {
        final Albums album = dsalbums.get(position);
        holder.txtTenAlbum.setText(album.getTenAlbum());
        holder.txtTenNgheSi.setText(album.getTenNgheSi());
        String urlAnh = album.getURLAnh();
        if (!urlAnh.equals("NoImage")){
            Picasso.with(context).load(urlAnh).into(holder.ivAnhAlbum);
        }else {
            holder.ivAnhAlbum.setImageResource(R.drawable.album_default_icon);
        }
        holder.txtTenAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).changeToAlbumChiTietFragment(album);
                DAO_Album.updateAlbumViewAmount(album);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dsalbums.size()<maxCount){
            return dsalbums.size();
        }else{
            return maxCount;
        }
    }

    static class TimKiemAlbum_ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenAlbum, txtTenNgheSi;
        ImageView ivAnhAlbum;
        public TimKiemAlbum_ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenAlbum = itemView.findViewById(R.id.txtTenAlbum);
            txtTenNgheSi = itemView.findViewById(R.id.txtTenNgheSi);
            ivAnhAlbum = itemView.findViewById(R.id.ivAnhAlbum);
        }
    }
    public void updateAdapter(Albums albums){
        dsalbums.add(albums);
        notifyDataSetChanged();
    }
    public void resetAdapter(){
        dsalbums.clear();
        notifyDataSetChanged();
    }
    public ArrayList<Albums> getAlbumList(){
        return dsalbums;
    }
}
