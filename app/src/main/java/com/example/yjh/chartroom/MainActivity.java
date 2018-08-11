package com.example.yjh.chartroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button register;
    private Button login;
    private EditText accountText;
    private EditText passwordText;
    private UserDaoImpl userDao = new UserDaoImpl();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        setContentView(R.layout.activity_main);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        accountText = findViewById(R.id.account_text);
        passwordText = findViewById(R.id.password_text);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        checkBox = findViewById(R.id.remember_password);
        boolean isRemember = sharedPreferences.getBoolean("remember_password", false);
        if (isRemember) {
            String account = sharedPreferences.getString("account", "");
            String password = sharedPreferences.getString("password", "");
            accountText.setText(account);
            passwordText.setText(password);
            checkBox.setChecked(true);
        }
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
//                if (userDao.islogin(accountText.getText().toString(), passwordText.getText().toString())) {
                if (accountText.getText().toString().equals("admin") &&
                        passwordText.getText().toString().equals("admin")) {
                    Toast.makeText(MainActivity.this, "登录成功!",
                            Toast.LENGTH_SHORT).show();
                    editor = sharedPreferences.edit();
                    if (checkBox.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", accountText.getText().toString());
                        editor.putString("password", passwordText.getText().toString());
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, Client.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "登录失败!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register:
                User user = new User(accountText.getText().toString(), passwordText.getText().toString());
                if (userDao.regist(user)) {
                    Toast.makeText(MainActivity.this, "注册成功!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "注册失败!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
