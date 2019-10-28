package appmoviles.com.preclase13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import appmoviles.com.preclase13.model.entity.User;
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

    FirebaseAuth auth;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        signin_email = findViewById(R.id.signin_email);
        signin_name = findViewById(R.id.signin_name);
        signin_username = findViewById(R.id.signin_username);
        signin_mobile = findViewById(R.id.signin_mobile);
        signin_age = findViewById(R.id.signin_age);
        signin_password = findViewById(R.id.signin_password);
        signin_repassword = findViewById(R.id.signin_repassword);
        login_signin = findViewById(R.id.login_signin);


        login_signin.setOnClickListener(
                view -> {
                    auth.createUserWithEmailAndPassword(
                            signin_email.getText().toString().trim(),
                            signin_password.getText().toString().trim()
                    ).addOnCompleteListener(task -> {
                        try {
                            if (task.isSuccessful()) {

                                String strDate = signin_age.getYear()
                                        + "-" + (signin_age.getMonth() + 1)
                                        + "-" + signin_age.getDayOfMonth();
                                //2019-10-24
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date dateBirth = sdf.parse(strDate);
                                User user = new User(
                                        auth.getCurrentUser().getUid(),
                                        signin_name.getText().toString(),
                                        signin_email.getText().toString(),
                                        signin_username.getText().toString(),
                                        signin_mobile.getText().toString(),
                                        strDate,
                                        dateBirth.getTime(),
                                        signin_password.getText().toString()
                                );
                                db.getReference().child("usuarios")
                                        .child(user.getUid()).setValue(user);

                                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();



                            } else {
                                Toast.makeText(SignupActivity.this,
                                        "" + task.getException(),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    });

                    //auth.signInWithEmailAndPassword();

                    //auth.signOut();
                }
        );

    }
}
