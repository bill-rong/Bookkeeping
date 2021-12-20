package com.example.bookkeeping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Add extends AppCompatActivity {
    private ImageButton back;
    private Button btnConfirm;
    private Spinner spinner;
    private EditText etMoney;
    private EditText etRemark;
    MyOpwnHelper myOpwnHelper;
    private String userId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        myOpwnHelper = new MyOpwnHelper(this);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        back = findViewById(R.id.back);
        btnConfirm = findViewById(R.id.bt_confirm);
        spinner = findViewById(R.id.sp_type);
        etMoney = findViewById(R.id.et_money);
        etRemark = findViewById(R.id.et_remark);
    }
    public void backOnClick(View view){
        finish();
    }


    public void btnConfirmOnClick(View view){
        int type = spinner.getSelectedItemPosition();
        String money = etMoney.getText().toString().trim();
        String remark = etRemark.getText().toString().trim();
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = dateFormat.format(date);

        if(money.equals("") || remark.equals("")){
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
        if(type == 0){
            db.execSQL("insert into income(uid, mount, comment, time) values(?, ?, ?, ?)", new Object[]{userId, money, remark, time});
        }else if(type == 1){
            db.execSQL("insert into output(uid, mount, comment, time) values(?, ?, ?, ?)", new Object[]{userId, money, remark, time});
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("添加成功")
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                }).create();
        builder.show();
        db.close();
    }
}
