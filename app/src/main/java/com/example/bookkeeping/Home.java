package com.example.bookkeeping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    MyOpwnHelper myOpwnHelper;
    private ArrayList<DetailMapper> arrayList = new ArrayList<>();
    private String userId ;
    private TextView total;
    private TextView in;
    private TextView out;
    private TextView userName;
    private ListView lv;
    private Button btnIn;
    private Button btnOut;
    private ImageButton add;
    private MyBaseAdapter mAdapter;                     // 适配器
    private String[] CURREN_TABLE = new String[]{"income", "output"};
    private int[] CURREN_ICON = {R.drawable.in, R.drawable.out};
    private int curren = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myOpwnHelper = new MyOpwnHelper(this);
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        userId = sp.getString("userId", null);

        init();

        }

    @Override
    protected void onResume() {
        super.onResume();
        arrayList = selectInfo();

        mAdapter = new MyBaseAdapter();
        lv.setAdapter(mAdapter);

        update();
        listViewItemOnClick();
    }

    @Override
    public void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("userId", userId);
        Log.i("itent", "onSaveInstanceState");
    }


    public void init(){
        total = findViewById(R.id.total);
        lv = findViewById(R.id.lv);
        userName = findViewById(R.id.username);
        btnIn = findViewById(R.id.btnIn);
        btnOut = findViewById(R.id.btnOut);
        add = findViewById(R.id.add);
        in = findViewById(R.id.in);
        out = findViewById(R.id.out);

        total.setText("0");
        in.setText("0");
        out.setText("0");

        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        userName.setText(sp.getString("userName", null));

        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curren != 0){
                    curren = 0;
                    update();
                }
            }
        });
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curren != 1){
                    curren = 1;
                    update();
                }
            }
        });
    }

    public void listViewItemOnClick(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setMessage("说明：" + arrayList.get(position).getComment())
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                            }
                        }).create();
                builder.show();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setMessage("确定要删除吗？\n删除后资金会有所改变哦！")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SQLiteDatabase db;
                                db = myOpwnHelper.getWritableDatabase();
                                if(curren == 0){
                                    db.execSQL("delete from income where icid=?",
                                            new Object[]{arrayList.get(position).getId()});
                                    Toast.makeText(Home.this, position+"in"+arrayList.get(position).getId(), Toast.LENGTH_SHORT).show();
                                }else {
                                    db.execSQL("delete from output where opid=?",
                                            new Object[]{arrayList.get(position).getId()});
                                    Toast.makeText(Home.this, position+"out"+arrayList.get(position).getId(), Toast.LENGTH_SHORT).show();
                                }

                                db.close();
                                update();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                            }
                        })
                        .create();
                builder.show();
                return true;
            }
        });
    }

    public double getSum(String table){
        double sum = 0;
        SQLiteDatabase db;
        db = myOpwnHelper.getReadableDatabase();
//            Cursor cursor = db.query("income", new String[]{"sum(mount)"}, "uid=?", new String[]{userId}, null, null, null);
        Cursor cursor = db.rawQuery("select sum(mount) from "+table+" where uid=?",new String[]{userId});
        Log.i("cursor.getCount()", String.valueOf(cursor.getCount()));

        try{
            cursor.moveToFirst();
            sum = Double.parseDouble(cursor.getString(0));
        }catch (Exception e){
            e.printStackTrace();
        }
        cursor.close();
        db.close();
        return sum;
    }

    public void update(){
        Log.i("userid", userId);
        double inMount = 0;
        double outMount = 0;
        inMount = getSum("income");
        outMount = getSum("output");
        in.setText(Util.getDoubleString(inMount));
        out.setText(Util.getDoubleString(outMount));
        total.setText(Util.getDoubleString(inMount - outMount));

        arrayList = selectInfo();
        mAdapter.notifyDataSetChanged();
    }

    public void addOnClick(View view){
        Intent intent = new Intent(this, Add.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void signOutOnClick(View view){
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit().clear().commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public ArrayList<DetailMapper> selectInfo(){
        String TABLE = CURREN_TABLE[curren];
        ArrayList<DetailMapper> temp = new ArrayList<>();
        SQLiteDatabase db;
        db = myOpwnHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE+" where uid=? order by time desc", new String[]{userId});
        while (cursor.moveToNext()){
            DetailMapper detailMapper = new DetailMapper();
            detailMapper.setId(Integer.parseInt(cursor.getString(0)));
            detailMapper.setMount(Double.parseDouble(cursor.getString(2)));
            detailMapper.setComment(cursor.getString(3));
            detailMapper.setTime(cursor.getString(4));
            Log.i("mapper", detailMapper.toString());
            temp.add(detailMapper);
        }
        cursor.close();
        db.close();
        return temp;
    }

    /**
     * 自定义BaseAdapter适配器
     */
    class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // 配置数据
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 初始化控件
            View view = View.inflate(Home.this, R.layout.list_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.item_image);
            TextView mTextView = (TextView) view.findViewById(R.id.item_tv);
            TextView timeText = (TextView) view.findViewById(R.id.item_time);
            mTextView.setText(Util.getDoubleString(arrayList.get(position).getMount()));
            timeText.setText(arrayList.get(position).getTime());
            imageView.setBackgroundResource(CURREN_ICON[curren]);
            return view;
        }

    }
}
