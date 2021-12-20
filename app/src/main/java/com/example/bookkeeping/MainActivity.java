package com.example.bookkeeping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MyOpwnHelper myOpwnHelper;
    protected EditText ettel;
    protected EditText etpwd;
    protected Button register;
    protected Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        myOpwnHelper = new MyOpwnHelper(this);

    }
    public void init(){
        register = findViewById(R.id.register);
        loginBtn = findViewById(R.id.login);
        ettel = findViewById(R.id.username);
        etpwd = findViewById(R.id.password);
    }

    public void intentRegister(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
    public void loginOnClick(View view){
        String tel = ettel.getText().toString().trim();
        String pwd = etpwd.getText().toString().trim();
        if(tel.equals("") || pwd.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("输入框不能为空")
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    }).create();
            builder.show();
            return;
        }
        SQLiteDatabase db;
        db = myOpwnHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where tel=? and password=?", new String[]{tel, pwd});
        if(cursor.moveToNext()){
            SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
            sp.edit()
                    .putString("userId", cursor.getString(0))
                    .putString("userName", cursor.getString(3))
                    .apply();
            Log.i("TAG", "successful login");
            Intent intent = new Intent(this, Home.class);
            intent.putExtra("userId", cursor.getString(0));
            cursor.close();
            startActivity(intent);
            finish();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("账号或密码错误")
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    }).create();
            builder.show();
        }
        db.close();
    }

}
