package com.example.p_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    LinearLayout layoutInform, layoutLog;
    private boolean oncePressed=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        layoutInform=(LinearLayout) findViewById(R.id.informLayout_admin_activity);
        layoutLog=(LinearLayout) findViewById(R.id.logLayout_admin_activity);
        layoutInform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent=new Intent(getApplicationContext(), MemberSearchActivity.class);
                startActivity(intent);
            }
        });

        layoutLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent=new Intent(getApplicationContext(), LogSearchActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.logoutBtn_admin_actvity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        FirebaseAuth.getInstance().signOut();
        super.onDestroy();
    }
}
