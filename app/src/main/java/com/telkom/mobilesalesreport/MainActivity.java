package com.telkom.mobilesalesreport;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    LinearLayout btnAdd;
    private boolean success = false;
    //untuk get data
    private RecyclerView myRecyclerView;
    private DateUtils dateUtils;
    private TextView tvHeaderDate;
    private int y_code,m_code;
    private List<DataClass> listData = new ArrayList<>();
    private ConnectionClass connectionClass;
    private DataAdapter MyAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String date_out;
    public String getDate_out() {
        return date_out;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionClass = new ConnectionClass();
        myRecyclerView = findViewById(R.id.rv_main);
        MyAdapter = new DataAdapter(listData);
        tvHeaderDate = findViewById(R.id.tv_header_date);
        dateUtils = new DateUtils();
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        myRecyclerView.setAdapter(MyAdapter);
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        new SyncData().execute();
        btnAdd = findViewById(R.id.btn_add_transaction);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoadd = new Intent(MainActivity.this,AddTransactionActivity.class);
                startActivity(gotoadd);
            }
        });

        //init Data
        //tvStoreCode.setText(sharedPreference.getObjectData("username", String.class));

        String today = dateUtils.formatCurrentDate(DateUtils.SDF_FORMAT2);
        int firstDay = dateUtils.getCurrentDays();
        String firstDayString = firstDay + "/" + (dateUtils.getCurrentMonth() + 1) + "/" + dateUtils.getCurrentYear();
        try {
            tvHeaderDate.setText(dateUtils.reFormatDate(firstDayString, DateUtils.SDF_FORMAT1, DateUtils.SDF_FORMAT2));
             } catch (ParseException e) {
            e.printStackTrace();
        }
        date_out = tvHeaderDate.getText().toString();

        //sharedPreference.storeData("today", today);

        //init data end

        tvHeaderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        String string_date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        try {
                            date_out = dateUtils.reFormatDate(string_date, DateUtils.SDF_FORMAT1, DateUtils.SDF_FORMAT2);
                            tvHeaderDate.setText(date_out);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, dateUtils.getCurrentYear(), dateUtils.getCurrentMonth(), dateUtils.getCurrentDays());
                dialog.show();
            }
        });


    }



    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details";
        ProgressDialog progress;

        String storecode = "001";
        String trxDate = "21-OCT-2019";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection conn = connectionClass.CONN();
                if (conn == null) {
                    success = false;
                } else {
                    String query = "EXEC DB_A4A292_msr.dbo.SP_GET_TRANSACTION_STORE_DATE '" + storecode + "','" + trxDate + "'";//nah ini tglnya masih dr query blm dari varnya
                    Log.d("QUERY", query);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null) {
                        listData.clear(); // kalau datanya ada listnya di clear dulu
                        while (rs.next()) {
                            try {
                                // kalau ada data masukkan ke list
                                listData.add(new DataClass(rs.getString("TRX_DATE"),rs.getString("ARTICLE"),rs.getString("QTY"),rs.getString("PRICE")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        MyAdapter.setmData(listData);
                        msg = "Found";
                        success = true;
                    } else {
                        msg = "No Data Found";
                        success = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;

            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            MyAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);// setelah datanya masuk jangan lupa kasih tau adapternya
        }
    }
    @Override
    public void onRefresh() {
            new SyncData().execute();
    }
}
