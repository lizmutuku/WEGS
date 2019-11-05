package com.example.wegs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.wegs.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    //Declare the widgets
    EditText et_fullname, et_email,et_password,et_confirmpassword;
    Button btn_register;
    TextView tv_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize the widgets
        et_fullname=findViewById(R.id.editTextFullname);
        et_email = findViewById(R.id.editTextEmail);
        et_password = findViewById(R.id.editTextPassword);
        et_confirmpassword = findViewById(R.id.edittextConfirmPassword);
        btn_register = findViewById(R.id.buttonRegister);
        tv_login = findViewById(R.id.textviewLogin);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_password.getText().toString()
                        .matches(et_confirmpassword.getText().toString())){
                    Register(et_email.getText().toString()
                            ,et_password.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Passwords don't match",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(RegisterActivity.this,
                                LoginActivity.class);

                startActivity(intent);
            }
        });
    }

    public void Register(final String email, final String password){
        //Declare and Initialize our request tag
        String tag_string_request = "register_request";

        String urlAddress = "http://192.168.0.49/androidappapi/register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAddress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //A listener for the response
                        Toast.makeText(getApplicationContext(),
                                response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");

                            if(error){
                                String errormessage = jsonObject.getString("error_msg");
                                Toast.makeText(getApplicationContext(),errormessage,
                                        Toast.LENGTH_LONG);
                            }else{
                               String message = jsonObject.getString("message");

                               Toast.makeText(getApplicationContext()
                                       ,message, Toast.LENGTH_LONG).show();

                                Intent intent = new
                                        Intent(RegisterActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //An error listener e.g Timed out or No internet connection
                        if(volleyError instanceof TimeoutError){
                            Toast.makeText(getApplicationContext(),
                                    "Timed out", Toast.LENGTH_LONG).show();
                        }

                        Toast.makeText(getApplicationContext(),volleyError.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            //Pass our parameters
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params =
                        new HashMap<String, String>();
                params.put("email",email);
                params.put("password",password);

                return params;
            }
        };

        AppController.getInstance().
                addToRequestQueue(stringRequest,tag_string_request);
    }
}
