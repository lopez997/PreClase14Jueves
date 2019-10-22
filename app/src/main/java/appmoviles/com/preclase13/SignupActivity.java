package appmoviles.com.preclase13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import appmoviles.com.preclase13.view.custom.ScrolledDatePicker;

public class SignupActivity extends AppCompatActivity {

    private EditText signin_email;
    private EditText signin_name;
    private EditText signin_username;
    private EditText signin_mobile;
    private ScrolledDatePicker signin_age;
    private EditText signin_password;
    private EditText signin_repassword;
    private Button login_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signin_email = findViewById(R.id.signin_email);
        signin_name = findViewById(R.id.signin_name);
        signin_username = findViewById(R.id.signin_username);
        signin_mobile = findViewById(R.id.signin_mobile);
        signin_age = findViewById(R.id.signin_age);
        signin_password = findViewById(R.id.signin_password);
        signin_repassword = findViewById(R.id.signin_repassword);
        login_signin = findViewById(R.id.login_signin);
        
    }
}
