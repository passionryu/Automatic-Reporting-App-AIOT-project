package com.example.p_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference loginReference;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        loginReference= FirebaseDatabase.getInstance().getReference("firebasetest");

        String email=TokenManager.getIdToken(getApplicationContext(),TokenManager.KEY_EMAIL_ID);
        String password=TokenManager.getIdToken(getApplicationContext(),TokenManager.KEY_PASSWORD);

        Log.d("MainActivity",email);
        Log.d("MainActivity",password);

        if(email.isEmpty()||password.isEmpty()){
            intent=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
                        loginReference.child("UserAccount")
                                .child(firebaseUser.getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            UserAccount account=snapshot.getValue(UserAccount.class);
                                            if(account.isAdmin){
                                                intent=new Intent(getApplicationContext(), AdminActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                Gson gson=new Gson();
                                                intent=new Intent(getApplicationContext(), ClientActivity.class);
                                                intent.putExtra("account",gson.toJson(account));
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(MainActivity.this, "데이터베이스 오류 : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                                        intent=new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }
                    else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }


}

class TokenManager{
    private static final String PREF_NAME = "MyPreferences";
    public static final String KEY_EMAIL_ID = "emailID";
    public static final String KEY_PASSWORD="passWord";

    // 저장된 idToken을 읽어오는 메서드
    public static String getIdToken(Context context,String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    // idToken을 저장하는 메서드
    public static void saveIdToken(Context context,String key, String idToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, idToken);
        editor.apply();
    }

    // idToken을 삭제하는 메서드
    public static void clearIdToken(Context context,String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }
}