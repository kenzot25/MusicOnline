package com.toan.musicapp.DAOClass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.toan.musicapp.LoginActivity;
import com.toan.musicapp.MainActivity;
import com.toan.musicapp.Model.Admin;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class DAO_Admin {
    public static void loginAsAdmin(FirebaseUser user, final Context context, String password){
        final String email = user.getEmail();
        String displayName = user.getDisplayName();
        String photoURL = user.getPhotoUrl().toString();
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        final Bundle bundle = new Bundle();
        bundle.putInt("LoginAs", 4);
        bundle.putString("Email", email);
        bundle.putString("DisplayName", displayName);
        bundle.putString("Password", hashedPassword);
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("Admin");
        myDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        Admin admin = i.getValue(Admin.class);
                        if (admin.getEmail().equals(email)){
                            bundle.putString("UserID", admin.getMaAdmin());
                            bundle.putString("PhotoURL", admin.getUrlAnh());
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("UserProfile", bundle);
                            context.startActivity(intent);
                            break;
                        }
                    }
                    Toast.makeText(context, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void addUser(Admin admin, final Context context){
        final DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("Admin");
        String maAdmin = admin.getMaAdmin();
        myDatabaseRef.child(maAdmin).setValue(admin).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
    }

    public static void resetMonthlyViewAmount(TextView textView1, TextView textView2, TextView textView3){
        DAO_Nhac.resetMonthlyViewAmount(textView1);
        DAO_Album.resetMonthlyViewAmount(textView2);
        DAO_TheLoai.resetMonthlyViewAmount(textView3);
    }
}
