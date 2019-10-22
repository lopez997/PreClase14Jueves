package appmoviles.com.preclase13;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import appmoviles.com.preclase13.model.data.CRUDAlbum;
import appmoviles.com.preclase13.model.entity.Album;


public class NewAlbumActivity extends AppCompatActivity {

    private TextView albumIdTv;
    private EditText albumNameEt;
    private TextView albumDateTv;
    private Button albumCreateBTN;


    //Se generan cuando se inicia la actividad
    private String id;
    private Date date;
    private SimpleDateFormat sdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_album);

        albumIdTv = findViewById(R.id.albumIdTv);
        albumNameEt = findViewById(R.id.albumNameEt);
        albumDateTv = findViewById(R.id.albumDateTv);
        albumCreateBTN = findViewById(R.id.albumCreateBTN);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        id = UUID.randomUUID().toString();
        date = Calendar.getInstance().getTime();

        albumIdTv.setText("ID: "+id);
        albumDateTv.setText("Date: "+sdf.format(date));

        albumCreateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Album tasklist = new Album(id, albumNameEt.getText().toString(),date);
                CRUDAlbum.insertAlbum(tasklist);
                finish();
            }
        });
    }
}
