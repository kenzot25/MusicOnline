package com.toan.musicapp.DAOClass;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.toan.musicapp.AdapterClass.ChudeTrangchu_rvAdapter;
import com.toan.musicapp.AdditionalFunctions.AdditionalFunctions;
import com.toan.musicapp.Model.TheLoai;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DAO_TheLoai {
    public static void readSpecificCategory(final String tenTheLoai, final ChudeTrangchu_rvAdapter adapter, final int max){
        final int[] count = {0};
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("TheLoai");
        myDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        TheLoai theLoai = i.getValue(TheLoai.class);
                        if (theLoai.getTenTheLoai().toLowerCase().contains(tenTheLoai.toLowerCase()) && count[0]<=max){
                            adapter.updateAdapter(theLoai);
                        }else if (count[0]>max){
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void readPopularCategory(int max, final ChudeTrangchu_rvAdapter adapter){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("TheLoai");
        Query query = myDatabaseRef.orderByChild("luotXemThang").limitToLast(max);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        TheLoai theLoai = i.getValue(TheLoai.class);
                        adapter.updateAdapter(theLoai);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void uploadCategory(String tenTheLoai, final TextView textView){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("TheLoai");
        String maTheLoai = myDatabaseRef.push().getKey();
        TheLoai theLoai = new TheLoai(maTheLoai, tenTheLoai, 0);
        myDatabaseRef.child(maTheLoai).setValue(theLoai).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AdditionalFunctions.changeTextInMilisecond(textView, "Th??m Th??nh C??ng", "Th??m Th??? Lo???i");
            }
        });
    }
    public static void updateCategory(String maTheLoai, String tenTheLoai, final TextView textView){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("TheLoai");
        Map<String , Object> postValues = new HashMap<>();
        postValues.put("tenTheLoai", tenTheLoai);
        myDatabaseRef.child(maTheLoai).updateChildren(postValues).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AdditionalFunctions.changeTextInMilisecond(textView, "C???p Nh???t Th??nh C??ng", "S???a Th??? Lo???i");
            }
        });
    }
    public static void deleteCategory(String maTheLoai, final TextView textView){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("TheLoai");
        myDatabaseRef.child(maTheLoai).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AdditionalFunctions.changeTextInMilisecond(textView, "X??a th??nh C??ng", "X??a Th??? Lo???i");
            }
        });
    }
    public static void resetMonthlyViewAmount(final TextView textView){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("TheLoai");
        Map<String, Object> postValue = new HashMap<>();
        postValue.put("luotXemThang", 0);
        myDatabaseRef.updateChildren(postValue).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AdditionalFunctions.changeTextInMilisecond(textView, "Reset Th??nh C??ng", "Reset View Th??ng Nh???c");
            }
        });
    }
    public static void updateMonthlyViewAmount(String maTheLoai, int luotXemThang){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("TheLoai");
        Map<String, Object> postValue = new HashMap<>();
        postValue.put("luotXemThang", luotXemThang + 1);
        myDatabaseRef.child(maTheLoai).updateChildren(postValue);
    }
}