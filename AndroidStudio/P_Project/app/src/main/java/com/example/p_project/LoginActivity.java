package com.example.p_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.concurrent.CountDownLatch;

public class LoginActivity extends AppCompatActivity {
    public static LoginActivity thisActivity=null;
    EditText idEdit, pwEdit;
    Button loginBTN;
    TextView signinText;
    View signDialog;

    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDataRef;

    EditText nameEdt_r, phoneEdt_r, idEdt_r, pwEdt_r;
    private UserAccount account;

    //private boolean readData=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.thisActivity=LoginActivity.this;

        if(ClientActivity.thisActivity!=null)
            ClientActivity.thisActivity.finish();

        idEdit=findViewById(R.id.idEdt_login_activity);
        pwEdit=findViewById(R.id.pwEdt_login_activity);
        loginBTN=findViewById(R.id.loginBtn_login_activity);
        signinText=findViewById(R.id.signTxt_login_activity);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mDataRef=FirebaseDatabase.getInstance().getReference("firebasetest");

        loginBTN.setOnClickListener(new View.OnClickListener() {
            //로그인
            @Override
            public void onClick(View view) {

                String email = idEdit.getText().toString();
                String pw = pwEdit.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("MainActivity","93");
                            //로그인 성공
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            mDataRef.child("UserAccount").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        UserAccount userAccount = snapshot.getValue(UserAccount.class);
                                        if (userAccount != null) {
                                            boolean isAdmin = userAccount.getAdmin();
                                            Gson gson=new Gson();
                                            String accountJSON=gson.toJson(userAccount);
                                            if (isAdmin) {
                                                // Admin일 때의 처리
                                                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                                startActivity(intent);
                                            } else {
                                                // 일반 사용자일 때의 처리
                                                TokenManager.saveIdToken(getApplicationContext(),TokenManager.KEY_EMAIL_ID,email);
                                                TokenManager.saveIdToken(getApplicationContext(),TokenManager.KEY_PASSWORD,pw);
                                                Intent intent = new Intent(getApplicationContext(), ClientActivity.class);
                                                intent.putExtra("account",accountJSON);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // 오류 처리
                                    Toast.makeText(getApplicationContext(), "데이터베이스 오류", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // 로그인 실패 시 처리
                            Log.d("MainActivity","126");
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        signinText.setOnClickListener(new View.OnClickListener() {
            //회원가입
            @Override
            public void onClick(View view) {
                signDialog = (View) View.inflate(LoginActivity.this, R.layout.dialog_signin, null);
                AlertDialog.Builder sdlg = new AlertDialog.Builder(LoginActivity.this);
                sdlg.setTitle("회원가입");
                sdlg.setView(signDialog);

                idEdt_r = signDialog.findViewById(R.id.idEdt_signin_dialog);
                pwEdt_r = signDialog.findViewById(R.id.pwEdt_signin_dialog);
                phoneEdt_r = signDialog.findViewById(R.id.phoneEdt_signin_dialog);
                nameEdt_r = signDialog.findViewById(R.id.nameEdt_signin_dialog);

                sdlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = idEdt_r.getText().toString();
                        String pw = pwEdt_r.getText().toString();

                        mFirebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //회원가입 성공
                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                                    UserAccount account = new UserAccount();
                                    account.setEmailID(email);
                                    account.setPassword(pw);
                                    account.setPhonenumber(phoneEdt_r.getText().toString());
                                    account.setUsername(nameEdt_r.getText().toString());
                                    account.setIdToken(firebaseUser.getUid());

//                                    account.calling_list.add("112");
//                                    account.calling_list.add("119");
                                    //UserAccount 객체 설정

                                    mDataRef.child("UserAccount").child(account.getIdToken()).setValue(account);
                                    //mDataRef.child("UserAccount").child(account.getIdToken()).child("calling_list").setValue(account.calling_list);
                                    //firebasetest->UserAccount->토큰값

                                    Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_LONG).show();
                                    String errorMessage = task.getException().getMessage();
                                    Log.e("SignUp", "Registration failed: " + errorMessage);
                                }
                            }
                        });
                    }
                });
                sdlg.setNegativeButton("취소", null);
                sdlg.show();
            }
        });
//        String idToken=mFirebaseAuth.getCurrentUser().getUid();
//
//        final CountDownLatch latch = new CountDownLatch(1);
//
//        mDataRef= FirebaseDatabase.getInstance().getReference("firebasetest/UserAccount/"+idToken);
//        mDataRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("test","51");
//                account=snapshot.getValue(UserAccount.class);
//                latch.countDown();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        try {
//            latch.await(); // onDataChange가 호출될 때까지 기다림
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }
}
