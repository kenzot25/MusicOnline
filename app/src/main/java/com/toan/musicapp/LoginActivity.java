package com.toan.musicapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.toan.musicapp.AdditionalFunctions.AdditionalFunctions;
import com.toan.musicapp.DAOClass.DAO_Admin;
import com.toan.musicapp.DAOClass.DAO_NguoiDung;

public class LoginActivity extends AppCompatActivity {
    EditText txtUserEmail, txtPassword;
    TextView txtRegisterNow, txtForgotPassword;
    CheckBox chkRemember;
    Button btnLogin;

    private boolean ADMIN_STATUS = false;
    boolean isLoggingIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        btnLogin.setOnClickListener(v -> {
            Log.d("isLoggingIn", String.valueOf(isLoggingIn));
            if(!isLoggingIn){
                isLoggingIn = true;
                checkAccountVerification();
            }
        });
        txtRegisterNow.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, DangKyActivity.class)));
        txtForgotPassword.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, QuenMatKhauActivity.class)));
        getUserAccount();
    }

    private void findView(){
        txtPassword = findViewById(R.id.txtPassword);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        chkRemember = findViewById(R.id.chkRemember);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegisterNow = findViewById(R.id.txtRegisterNow);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
    }

    private void saveUserAccount(String email, String password){
        if (!email.isEmpty()&&!password.isEmpty()){
            SharedPreferences sp = getSharedPreferences("myAccount", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            boolean chk = chkRemember.isChecked();
            if(!chk){
                editor.clear();
            }else{
                editor.putString("email",email);
                editor.putString("password",password);
                editor.putBoolean("savestatus",chk);
            }
            editor.apply();
        }

    }
    private void getUserAccount(){
        SharedPreferences pref = getSharedPreferences("myAccount",MODE_PRIVATE);
        boolean chk = pref.getBoolean("savestatus",false);
        if (chk){
            final String username = pref.getString("email","");
            final String password = pref.getString("password","");
            txtUserEmail.setText(username);
            txtPassword.setText(password);
        }
        chkRemember.setChecked(chk);
    }

    private void checkAccountVerification(){
        final String email = txtUserEmail.getText().toString();
        final String password = txtPassword.getText().toString();
        Log.d("TK MK", email + password );
//        Development
//        ADMIN_STATUS = true;
        if (email.equals("AdminStatusTrue")){
            Toast.makeText(this, "Chế độ login admin", Toast.LENGTH_SHORT).show();
            ADMIN_STATUS = true;
            return;
        }else if (email.equals("AdminStatusFalse")){
            Toast.makeText(this, "Chế độ login bình thường", Toast.LENGTH_SHORT).show();
            ADMIN_STATUS = false;
            return;
        }
        if (
                AdditionalFunctions.isStringEmpty(getBaseContext(),email, password) ||
                !AdditionalFunctions.isEmailValid(getBaseContext(),email) ||
                !AdditionalFunctions.isPasswordValid(getBaseContext(),password)
            )
        {
            isLoggingIn = false;
            return;
        }
        Log.d("D", String.valueOf(isLoggingIn));
        FirebaseAuth myFirebaseAuth = FirebaseAuth.getInstance();
        myFirebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            Log.d("onSuccess","onSuccess");
            FirebaseUser user = authResult.getUser();
            Log.d("user.isEmailVerified()", String.valueOf(user.isEmailVerified()));
            if (user.isEmailVerified()){
                if (ADMIN_STATUS){
                    DAO_Admin.loginAsAdmin(user, LoginActivity.this, password);
                }else {
                    DAO_NguoiDung.loginAsUser(user, LoginActivity.this, password);
                }
                saveUserAccount(email, password);
            }else{
                Toast.makeText(getBaseContext(), "Tài khoản chưa được xác thực!", Toast.LENGTH_SHORT).show();
            }
            isLoggingIn = false;
        }).addOnFailureListener(e -> {
            Log.d("onFailure","onFailure");

            Log.d("fail",e.toString());
            Toast.makeText(getBaseContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
            isLoggingIn = false;
        });
    }
    private void logOut(){
        if (FirebaseAuth.getInstance().getCurrentUser()!= null){
            FirebaseAuth.getInstance().signOut();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
