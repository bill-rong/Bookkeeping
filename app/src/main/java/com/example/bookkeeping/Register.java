package com.example.bookkeeping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class Register extends AppCompatActivity {
    MyOpwnHelper myOpwnHelper;
    protected EditText rname;
    protected EditText rtel;
    protected EditText rpwd;
    protected Button rregister;
    protected ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        init();
        myOpwnHelper = new MyOpwnHelper(this);

    }

    void init(){
    rname = findViewById(R.id.rname);
    rtel = findViewById(R.id.rtel);
    rpwd = findViewById(R.id.rpwd);
    rregister = findViewById(R.id.rregister);
    back = findViewById(R.id.back);
    }
    public void backOnClick(View view){
        finish();
    }

    public void registerOnClick(View view){
        String name = rname.getText().toString().trim();
        String tel = rtel.getText().toString().trim();
        String pwd = rpwd.getText().toString().trim();
        if(name.equals("") || tel.equals("") || pwd.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("输入框不能为空")
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    }).create();
            builder.show();
        }else if(isExistTel(tel)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("该手机号已注册")
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    }).create();
            builder.show();
        }else{
            SQLiteDatabase db;
            db = myOpwnHelper.getWritableDatabase();
            try{
                db.execSQL("insert into users(name, tel, password, assets) values(?, ?, ?, 0)", new Object[]{name, tel, pwd});
            }catch (Exception e){
                Log.i("TAG", "注册失败");
            } finally {
                db.close();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("注册成功")
                    .setPositiveButton("前往登录", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }).create();
            builder.show();
        }
    }
    public boolean isExistTel(String tel){
        SQLiteDatabase db;
        db = myOpwnHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from users where tel=?", new String[]{tel});
        boolean result = cursor.moveToNext();
        db.close();
        return result;
    }
}
