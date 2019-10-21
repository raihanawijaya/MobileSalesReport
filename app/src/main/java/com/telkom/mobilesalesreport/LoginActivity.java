package com.telkom.mobilesalesreport;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText etUserCode, etUserPass;
    private ConnectionClass connectionClass;
    String usercode, userpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //inisiasi
        btnLogin = findViewById(R.id.btn_login);
        etUserPass = findViewById(R.id.et_userpass);
        etUserCode = findViewById(R.id.et_usercode);
        connectionClass = new ConnectionClass();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usercode = etUserCode.getText().toString();
                userpass = etUserPass.getText().toString();
                CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                checkLogin.execute("");
            }
        });
    }

    public class CheckLogin extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }
        //DO IN BACKGROUOND, MAKE QUERY, CALL CONNECTION CLASS, RETURN VALUE OF Z AND ISSUCCESS
        @Override
        protected String doInBackground(String... params) {
            if (usercode.trim().equals("") || userpass.trim().equals(""))
                z = "Please enter Username/StoreCode and Password";
            else {
                try {
                    Connection conn = connectionClass.CONN();
                    if (conn == null) {
                        z = "Check Your Internet Access!";
                    } else {
                        String query = "EXEC DB_A4A292_msr.dbo.SP_LOGIN '"+usercode+"','"+ userpass+"'";
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            z = "Login successful";
                            isSuccess = true;
                        } else {
                            z = "Invalid Credentials!";
                            isSuccess = false;
                        }
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = ex.getMessage();
                }
            }
            return z;
        }

        @Override
        protected void onPostExecute(String r) {
            if (isSuccess) {
                Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();
                Intent gotomain = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(gotomain);

            }
        }

    }

}
