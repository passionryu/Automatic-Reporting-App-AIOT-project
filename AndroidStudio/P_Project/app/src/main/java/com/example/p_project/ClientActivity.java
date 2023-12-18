package com.example.p_project;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ClientActivity extends AppCompatActivity {
    public static ClientActivity thisActivity;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private static final int MAP_PERMISSION_REQUEST_CODE=101;
    private static final int SMS_PERMISSION_REQUEST_CODE=102;
    private static ListView numberListView;
    private Button addNumber, logoutBtn;
    private EditText numberEdt;
    private Switch callonoffswitch;
    private View logoutDialog;

    public static ArrayList<String> numberList;
    private ArrayAdapter<String> numberAdapter;

    private Boolean addingNumber=false, oncePressed=false;
    private boolean isFirst=true;
    public static UserAccount account;
    DatabaseReference accountRef,listeningReference,writingReference,readingReference,tempRef;
    private ValueEventListener valueEventListener;
    private PermissionRequester permissionRequester;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(LoginActivity.thisActivity!=null){
            LoginActivity.thisActivity.finish();
        }
        thisActivity=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Gson gson = new Gson();
        String userJson = (String) getIntent().getStringExtra("account");
        account = gson.fromJson(userJson, UserAccount.class);

        //권한요청
        permissionRequester=new PermissionRequester(ClientActivity.this);
        permissionRequester.requestLocationAndSmsPermission();
        PermissionRequester.granted.sms=permissionRequester.checkPermission(Manifest.permission.SEND_SMS);
        PermissionRequester.granted.location=permissionRequester.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        writingReference=FirebaseDatabase.getInstance().getReference("firebasetest/accidentLogs");
        readingReference=FirebaseDatabase.getInstance().getReference("firebasetest/RawLogs");
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RawLog rawLog=snapshot.getValue(RawLog.class);
                getCurrentLocation(rawLog);
                tempRef.removeEventListener(valueEventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        listeningReference = FirebaseDatabase.getInstance().getReference("notify/notify");
        listeningReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(isFirst) {
                    isFirst=false;
                }
                else {
                    String key=snapshot.getValue(String.class);
                    tempRef=readingReference.child(key);
                    tempRef.addValueEventListener(valueEventListener);
                    //tempRef.removeEventListener(valueEventListener);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        accountRef = FirebaseDatabase.getInstance().getReference("firebasetest").child("UserAccount/"+account.idToken);

        numberListView = (ListView) findViewById(R.id.numberListView_client_activity);
        numberList = account.getCalling_list();

        //checkPermissions();//지도 권한 요청

        numberAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, account.calling_list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                //글씨 크기 수정
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextSize(25);

                accountRef.child("calling_list").setValue(account.calling_list);

                return view;
            }
        };
        numberListView.setAdapter(numberAdapter);

        //전화번호 롱클릭 시 삭제
        numberListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedNumber = (String) adapterView.getItemAtPosition(position);
                Toast.makeText(ClientActivity.this, "번호 " + selectedNumber + "가 삭제되었습니다.", Toast.LENGTH_LONG);

                account.calling_list.remove(position);
                numberAdapter.notifyDataSetChanged();

                accountRef.child("calling_list").setValue(account.calling_list);

                return true;
            }
        });

        //View 와 ID 연결
        logoutBtn = (Button) findViewById(R.id.logoutBtn_client_acitivity);
        addNumber = (Button) findViewById(R.id.addBtn_client_activity);
        numberEdt = (EditText) findViewById(R.id.numberEdt_client_activity);
        //mapBtn=(Button) findViewById(R.id.mapBtn);
        callonoffswitch = (Switch) findViewById(R.id.callonoffswitch_client_layout);

        callonoffswitch.setChecked(account.call_onoff);

        //자동신고 ON/OFF
        callonoffswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                account.setCall_onoff(isChecked);
                accountRef.child("call_onoff").setValue(isChecked);
            }
        });

        addNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addingNumber) {
                    String number = numberEdt.getText().toString();
                    account.calling_list.add(number);
                    numberAdapter.notifyDataSetChanged();

                    accountRef.child("calling_list").setValue(account.calling_list);
                    numberEdt.setText("");
                    numberEdt.setVisibility(View.GONE);
                } else {
                    numberEdt.setText("");
                    numberEdt.setVisibility(View.VISIBLE);
                }
                addingNumber = !addingNumber;
            }
        });

        //로그아웃 버튼
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog = (View) View.inflate(ClientActivity.this, R.layout.dialog_logout, null);
                AlertDialog.Builder lodlg = new AlertDialog.Builder(ClientActivity.this);
                lodlg.setTitle("Log out");
                lodlg.setView(logoutDialog);
                lodlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        TokenManager.clearIdToken(getApplicationContext(),TokenManager.KEY_EMAIL_ID);
                        TokenManager.clearIdToken(getApplicationContext(),TokenManager.KEY_PASSWORD);

                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                });
                lodlg.setNegativeButton("취소", null);
                lodlg.show();
            }
        });
    }

    private void getCurrentLocation(RawLog rawLog) {
        // 위치 권한 확인
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // 위치 권한이 있는 경우 현재 위치 가져오기
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    //SMS권한이 있는 경우 번호 전송
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    if(PermissionRequester.granted.sms){

                        String text="Latitude: " + latitude + ", Longitude: " + longitude;
                        text="전동킥보드 사고 발생! \n\n" +
                                text+
                                "\n\n구조 바랍니다";

                        for(String number:ClientActivity.numberList){
                            sendSMS(number,text);
                        }
                    }
                    writingReference.child(rawLog.getDate()).setValue(new AccidentLog(rawLog,account.idToken,latitude,longitude));

                } else {
                    Log.d("Location", "Location is null");
                }
            }).addOnFailureListener(e -> {
                // 위치 정보를 가져오는 데 실패한 경우
                Toast.makeText(getApplicationContext(),"위치 정보 가져오기에 실패했습니다.",Toast.LENGTH_SHORT).show();
                if(PermissionRequester.granted.sms){
                    String text="사고 발생!\n\n구조 바랍니다";
                    for(String number:ClientActivity.numberList){
                        sendSMS(number,text);
                    }
                }

            });
        } else {
            if(PermissionRequester.granted.sms){
                String text="사고 발생!\n\n구조 바랍니다";
                for(String number:ClientActivity.numberList){
                    sendSMS(number,text);
                }
            }
        }
    }

    private void sendSMS(String phoneNumber, String message){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS sending failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PermissionRequester.LOCATION_PERMISSION_REQUEST_CODE:
                // 위치 권한에 대한 처리
                if (PermissionRequester.granted.location
                        =(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    PermissionRequester.granted.location=true;
                } else {
                    // 권한이 거부된 경우
                    Toast.makeText(getApplicationContext(),"위치 권한이 거부된 경우 사고 발생 시" +
                            "메시지에 위치 정보를 표시하실 수 없습니다.",Toast.LENGTH_LONG).show();
                }
                break;

            case PermissionRequester.SMS_PERMISSION_REQUEST_CODE:
                // SMS 권한에 대한 처리
                if (PermissionRequester.granted.sms
                        =(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // 권한이 허용된 경우
                    PermissionRequester.granted.sms=true;
                    // 추가로 실행할 코드 작성
                } else {
                    // 권한이 거부된 경우

                    Toast.makeText(getApplicationContext(),"SMS권한을 설정하지 않으신 경우 신고 기능을" +
                            " 이용하실 수 없습니다.",Toast.LENGTH_LONG).show();
                }
                break;

            // 다른 권한에 대한 처리도 추가 가능

            default:
                break;
        }
    }
}


