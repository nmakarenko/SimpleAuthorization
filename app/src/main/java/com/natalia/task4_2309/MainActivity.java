package com.natalia.task4_2309;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{

    EditText etLogin;
    EditText etPassword;
    EditText etPhone;
    EditText etEmail;

    Button bEnter;

    String login;
    String password;
    String phone;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);

        bEnter = (Button) findViewById(R.id.bEnter);

        bEnter.setEnabled(false);
        bEnter.setOnClickListener(this);

        etLogin.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        etPhone.addTextChangedListener(this);
        etEmail.addTextChangedListener(this);

 /*       etEmail.setText("d@f.f");
        etLogin.setText("fdsjkfhdsk");
        etPassword.setText("fgfgG3%");
        etPhone.setText("4464664");

*/

    }


    @Override
    public void onClick(View v) {
        String warning = "";
        login = etLogin.getText().toString();
        password = etPassword.getText().toString();
        phone = etPhone.getText().toString();
        email = etEmail.getText().toString();


        boolean hasUpper = false,
                hasLower = false,
                hasNum = false,
                hasSymbol = false,
                hasAt = false;

        String symbols = "№;%@#$%&";
        String substrEmail;

        if (login.length() < 4) warning += "Логин должен быть не менее 4-х символов. \n";
        if (password.length() < 6) warning += "Пароль должен быть не менее 6-ти символов. \n";

        //check login
        for (int i = 0; i < login.length(); i++) {
            if (login.charAt(i) >= 'a' && login.charAt(i) <= 'z'
                    || login.charAt(i) >= 'A' && login.charAt(i) <= 'Z'
                    || login.charAt(i) >= '0' && login.charAt(i) <= '9') continue;
            else {
                warning += "В логине разрешено использовать только латинские буквы и цифры. \n";
                break;
            }
        }

        //check password
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) >= 'a' && password.charAt(i) <= 'z'
                    || password.charAt(i) >= 'A' && password.charAt(i) <= 'Z'
                    || password.charAt(i) >= '0' && password.charAt(i) <= '9'
                    || symbols.contains(Character.toString(password.charAt(i)))) continue;
            else {
                warning += "В пароле разрешено использовать только латинские буквы. \n";
                break;
            }
        }

        for (int i = 0; i < password.length(); i++) {
            if (!hasUpper && password.charAt(i) >= 'A' && password.charAt(i) <= 'Z') {
                hasUpper = true;
                continue;
            }
            if (!hasLower && password.charAt(i) >= 'a' && password.charAt(i) <= 'z') {
                hasLower = true;
                continue;
            }
            if (!hasNum && password.charAt(i) >= '0' && password.charAt(i) <= '9') {
                hasNum = true;
                continue;
            }
            if (!hasSymbol && symbols.contains(Character.toString(password.charAt(i)))) {
                hasSymbol = true;
                continue;
            }
            if (hasSymbol && symbols.contains(Character.toString(password.charAt(i)))) {
                warning += "Пароль должен содержать только один служебный символ №;%@#$%&.\n";
                break;
            }
        }

        if (password.length() >= 6) {
            if (!hasUpper) warning += "Пароль должен содержать хотя бы одну заглавную букву.\n";
            if (!hasLower) warning += "Пароль должен содержать хотя бы одну прописную букву.\n";
            if (!hasNum) warning += "Пароль должен содержать хотя бы одну цифру.\n";
            if (!hasSymbol) warning += "Пароль должен содержать один служебный символ №;%@#$%&.\n";
        }

        //check phone
        for (int i = 0; i < phone.length(); i++) {
            if (phone.charAt(i) < '0' || phone.charAt(i) > '9') {
                warning += "Телефон может содержать только цифры.\n";
            }
        }

        //check email
        for (int i = 0; i < email.length(); i++) {
            if (!hasAt && symbols.contains(Character.toString(email.charAt(i))) && i > 0) {
                hasAt = true;
                substrEmail = email.substring(i + 1);
                if (substrEmail.indexOf('.') == -1) {
                    warning += "Email должен содержать точку после @.\n";
                    break;
                }
                else if (substrEmail.indexOf('.') == 0) {
                    warning += "В email между @ и точкой должна быть текстовая область.\n";
                    break;
                }
                else if (substrEmail.indexOf('.') == substrEmail.length() - 1) {
                    warning += "В email точка не может быть последним символом.\n";
                    break;
                }
                else if (substrEmail.charAt(substrEmail.length() - 1) == '.') {
                    warning += "В email точка не может быть последним символом.\n";
                    break;
                }
                else continue;
            }
            if (i == 0 && symbols.contains(Character.toString(email.charAt(i)))) {
                warning += "Email должен содержать текстовую область перед @.\n";
                break;
            }
            if (hasAt && symbols.contains(Character.toString(email.charAt(i)))) {
                warning += "Email должен содержать только один символ @.\n";
                break;
            }
        }

        if (!hasAt) warning += "Email должен содержать символ @.\n";

        if (!warning.equals("")) Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
        else {
            final LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
            final ProgressBar pbar = new ProgressBar(this); // Final so we can access it from the other thread
            pbar.setVisibility(View.VISIBLE);
            ll.removeAllViews();
            ll.addView(pbar);

            setContentView(ll);

// Create a Handler instance on the main thread
            final Handler handler = new Handler();

// Create and start a new Thread
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try{
                        Thread.sleep(5000);
                    }
                    catch (Exception e) { } // Just catch the InterruptedException

                    // Now we use the Handler to post back to the main thread
                    handler.post(new Runnable() {
                        public void run() {
                            // Set the View's visibility back on the main UI Thread

                            pbar.setVisibility(View.INVISIBLE);
                            Toast toast = new Toast(ll.getContext());
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.makeText(ll.getContext(), "Login: " + login
                                    + "\nPassword: " + password
                                    + "\nPhone: " + phone
                                    + "\nEmail: " + email
                                    + "\n\nПоздравляем, вход выполнен!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            thread.start();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        setButtonVisibility();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setButtonVisibility();

    }

    void setButtonVisibility() {
        if (etPhone.getText().length() == 0 ||
                etPassword.getText().length() == 0 ||
                etLogin.getText().length() == 0 ||
                etEmail.getText().length() == 0) bEnter.setEnabled(false);
        else bEnter.setEnabled(true);
    }
}
