package com.telkom.mobilesalesreport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.Statement;

public class EditTransactionActivity extends AppCompatActivity {

    private EditText etTrxCode, etTrxDate, etArticle, etPrice, etQty;
    private TextView tvStoreCode;
    private Button btnUpdate, btnDelete;
    private ConnectionClass connectionClass;
    private SharedPreference sharedPreference;
    private boolean success = false;
    private String getDate, itemRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        etTrxCode = findViewById(R.id.et_trx_code);
        etTrxDate = findViewById(R.id.et_trx_date);
        etArticle = findViewById(R.id.et_article);
        etPrice = findViewById(R.id.et_price);
        etQty = findViewById(R.id.et_qty);
        tvStoreCode = findViewById(R.id.tv_storecode);
        btnDelete = findViewById(R.id.btn_delete);
        btnUpdate = findViewById(R.id.btn_update);
        connectionClass = new ConnectionClass();
        sharedPreference = new SharedPreference(this);
        getDate = sharedPreference.getObjectData("date", String.class);
        String storecode = sharedPreference.getObjectData("store", String.class);
        String itemTrxCode = (String) getIntent().getSerializableExtra("itemTrxCode");
        String itemDate = (String) getIntent().getSerializableExtra("itemDate");
        String itemArticle = (String) getIntent().getSerializableExtra("itemArticle");
        String itemPrice = (String) getIntent().getSerializableExtra("itemPrice");
        String itemQty = (String) getIntent().getSerializableExtra("itemQty");
        itemRowId = (String) getIntent().getSerializableExtra("itemRowId");

        tvStoreCode.setText(storecode);
        etTrxCode.setText(itemTrxCode);
        etTrxDate.setText(itemDate);
        etArticle.setText(itemArticle);
        etPrice.setText(itemPrice);
        etQty.setText(itemQty);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteData deleteData = new DeleteData();// this is the Asynctask, which is used to process in background to reduce load on app process
                deleteData.execute("");
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateData updateData = new UpdateData();// this is the Asynctask, which is used to process in background to reduce load on app process
                updateData.execute("");
            }
        });
    }

    private class DeleteData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(EditTransactionActivity.this,
                    "Delete Data", "Deleting ! Please wait...", true);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection conn = connectionClass.CONN();
                if (conn == null) {
                    success = false;
                } else {
                    String query = "EXEC DB_A4A292_msr.dbo.SP_DELETE_TRANS '" + itemRowId +"'";
                    Log.d("QUERY", query);
                    Statement stmt = conn.createStatement();
                    int status = stmt.executeUpdate(query);
                    if (status != 0) {
                        msg = "Delete Success";
                        success = true;
                    } else {
                        msg = "Delete Failed";
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
            progress.dismiss();
            Toast toast = Toast.makeText(EditTransactionActivity.this,msg, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private class UpdateData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details";
        ProgressDialog progress;
        String trxCode = etTrxCode.getText().toString();
        String trxDate = etTrxDate.getText().toString();
        String article = etArticle.getText().toString();
        String price = etPrice.getText().toString();
        String qty = etQty.getText().toString();
        String storecode= "001";

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(EditTransactionActivity.this,
                    "Update Data", "Updating ! Please wait...", true);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection conn = connectionClass.CONN();
                if (conn == null) {
                    success = false;
                } else {
                    String query = "EXEC DB_A4A292_msr.dbo.SP_UPDATE_TRANS '" + itemRowId + "','" + trxCode + "','" + article + "','" + trxDate + "','" + price + "','" + qty +"'";
                    Log.d("QUERY", query);
                    Statement stmt = conn.createStatement();
                    int status = stmt.executeUpdate(query);
                    if (status != 0) {
                        msg = "Update Success";
                        success = true;
                    } else {
                        msg = "Update Failed";
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
            progress.dismiss();
            Toast.makeText(EditTransactionActivity.this, msg + "", Toast.LENGTH_SHORT).show();
        }
    }

}
