package com.toan.musicapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.toan.musicapp.AdditionalFunctions.AdditionalFunctions;
import com.toan.musicapp.DAOClass.DAO_Admin;
import com.toan.musicapp.DAOClass.DAO_NguoiDung;
import com.toan.musicapp.Model.Admin;
import com.toan.musicapp.Model.NguoiDung;
import com.toan.musicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class DangKyActivity extends AppCompatActivity {
    EditText txtUserDisplayName, txtUserPassword, txtUserEmail, txtUserReconfirmPassword;
    Button btnRegister;
    FirebaseAuth myFirebaseAuth = FirebaseAuth.getInstance();
    String default_icon = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQa0PewBfIvXFkO6FUKjCrbs1gRFjv-Bc2eReJAw_4&s";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        findView();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }
    private void findView(){
        txtUserDisplayName = findViewById(R.id.txtUserDisplayName);
        txtUserPassword = findViewById(R.id.txtUserPassword);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtUserReconfirmPassword = findViewById(R.id.txtUserReconfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
    }
    private void createUser(){
        final String email = txtUserEmail.getText().toString();
        final String matKhau = txtUserPassword.getText().toString();
        final String tenHienThi = txtUserDisplayName.getText().toString();
        String nhapLaiMatKhau = txtUserReconfirmPassword.getText().toString();
        if (!inputCheck(tenHienThi, email, matKhau, nhapLaiMatKhau)){
            return;
        }
        Log.d("CreateUser", "Still running");
        myFirebaseAuth.createUserWithEmailAndPassword(email, matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    final FirebaseUser user = myFirebaseAuth.getCurrentUser();
                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                            .setDisplayName(tenHienThi)
                            .setPhotoUri(Uri.parse(default_icon))
                            .build();
                    user.updateProfile(request);
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Thông báo email đã được gửi
                            if (task.isSuccessful()){
                                Toast.makeText(DangKyActivity.this, "Email xác thực đã được gửi", Toast.LENGTH_SHORT).show();
                                String hashedPassword = BCrypt.withDefaults().hashToString(8, matKhau.toCharArray());

                                final boolean isAdmin = false;
                                if(!isAdmin) {
                                    NguoiDung nguoiDung = new NguoiDung(user.getUid(), tenHienThi, hashedPassword, email, default_icon);
                                    DAO_NguoiDung.addUser(nguoiDung, DangKyActivity.this);
                                } else {
                                    Admin admin = new Admin(user.getUid(), tenHienThi, hashedPassword, email, default_icon);
                                    DAO_Admin.addUser(admin, DangKyActivity.this);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }else {
                    Toast.makeText(DangKyActivity.this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean inputCheck(String displayName,String email, String password, String reconfirmPassword){
        if (AdditionalFunctions.isStringEmpty(getBaseContext(),displayName, email, password, reconfirmPassword)){
            return false;
        }
        if (!AdditionalFunctions.isEmailValid(getBaseContext(),email)){
            return false;
        }
        if (!password.equals(reconfirmPassword)){
            Toast.makeText(this, "Nhập lại mật khẩu sai!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AdditionalFunctions.isPasswordValid(getBaseContext(), password, reconfirmPassword)){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DangKyActivity.this, LoginActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }
}
