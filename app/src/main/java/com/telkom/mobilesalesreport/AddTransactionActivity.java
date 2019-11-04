package com.telkom.mobilesalesreport;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.Statement;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText etTrxCode, etTrxDate, etArticle, etPrice, etQty;
    private Button btnInput;
    private ConnectionClass connectionClass;
    private boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        etTrxCode = findViewById(R.id.et_trx_code);
        etTrxDate = findViewById(R.id.et_trx_date);
        etArticle = findViewById(R.id.et_article);
        etPrice = findViewById(R.id.et_price);
        etQty = findViewById(R.id.et_qty);
        btnInput = findViewById(R.id.btn_input);
        connectionClass = new ConnectionClass();

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputData inputData = new InputData();// this is the Asynctask, which is used to process in background to reduce load on app process
                inputData.execute("");
            }
        });
    }

    private class InputData extends AsyncTask<String, String, String> {
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
            progress = ProgressDialog.show(AddTransactionActivity.this,
                    "Input Data", "Inserting ! Please wait...", true);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection conn = connectionClass.CONN(); //----1
                if (conn == null) { //----2
                    success = false;//----3
                } else { //----4
                    //----5
                    String query = "EXEC DB_A4A292_msr.dbo.SP_INSERT_TRANSACTION '" + trxCode + "','" + storecode + "','" + trxDate + "','" + article + "','" + price + "','" + qty +"'";
                    Log.d("QUERY", query);
                    Statement stmt = conn.createStatement();
                    int status = stmt.executeUpdate(query);
                    if (status != 0) { //----6
                        msg = "Input Success"; //----7
                        success = true;
                    } else { //----8
                        msg = "Input Failed"; //----9
                        success = false;
                    }
                }
            } catch (Exception e) { //10
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
            }
            return msg; //----11
        }

        @Override
        protected void onPostExecute(String msg) {
            progress.dismiss();
            Toast toast = Toast.makeText(AddTransactionActivity.this,msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            if (success == false) {
            } else {
                try {
                } catch (Exception ex) {
                }
            }

        }
    }
}
