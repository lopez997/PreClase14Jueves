package appmoviles.com.preclase13;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import appmoviles.com.preclase13.model.data.CRUDAlbum;
import appmoviles.com.preclase13.model.data.CRUDPhoto;
import appmoviles.com.preclase13.model.entity.Album;
import appmoviles.com.preclase13.model.entity.Photo;
import appmoviles.com.preclase13.model.entity.User;
import appmoviles.com.preclase13.util.HTTPSWebUtilDomi;
import appmoviles.com.preclase13.model.data.CRUDAlbum;
import appmoviles.com.preclase13.model.entity.Album;
import appmoviles.com.preclase13.util.HTTPSWebUtilDomi;

public class MainActivity extends AppCompatActivity {

    private ListView LVAlbum;
    private ArrayAdapter<Album> adapter;
    private ArrayList<Album> list;
    private Button addAlbumBtn;
    private Button signOutBtn;
    private Button friendsBtn;
    private Button syncBtn;
    private RelativeLayout controlPanel;
    FirebaseAuth auth;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();


        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        db.getReference().child("usuarios")
                .child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Toast.makeText(MainActivity.this,
                        "Hola " + user.getName(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //0...
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, 11);
        //0...


        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        LVAlbum = findViewById(R.id.LVAlbum);
        LVAlbum.setAdapter(adapter);
        addAlbumBtn = findViewById(R.id.addAlbumBtn);
        signOutBtn = findViewById(R.id.signOutBtn);
        friendsBtn = findViewById(R.id.friendsBtn);
        controlPanel = findViewById(R.id.controlPanel);
        syncBtn = findViewById(R.id.syncBtn);

        LVAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent i = new Intent(MainActivity.this, PhotoListActivity.class);
                i.putExtra("album", list.get(pos));
                startActivity(i);
            }
        });

        LVAlbum.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, final long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Eliminar")
                        .setMessage("¿Desea eliminar la lista de tareas?")
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CRUDAlbum.deteleAlbum(list.get(pos));
                                refreshTaskList();
                                dialogInterface.dismiss();
                            }
                        });
                builder.show();
                return true;
            }
        });

        addAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NewAlbumActivity.class);
                startActivity(i);
            }
        });


        signOutBtn.setOnClickListener(
                (view) -> {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(this)
                                    .setTitle("Salir de la sesión")
                                    .setMessage("¿Desea salir de la sesión?")
                                    .setNegativeButton("Cancelar", (dialog, i) -> {
                                        dialog.cancel();
                                    })
                                    .setPositiveButton("Aceptar", (dialog, i) -> {
                                        auth.signOut();
                                        Intent intent = new Intent(this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    });
                    builder.show();

                }
        );

        friendsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, FriendListActivity.class);
            startActivity(intent);
        });

        syncBtn.setOnClickListener(
                (v) -> {
                    HashMap<String, Album> completeAlbums = CRUDAlbum.getCompleteAlbums();
                    db.getReference().child("albumEmbedding")
                            .child(auth.getCurrentUser().getUid())
                            .setValue(completeAlbums);

                    HashMap<String, Album> albums = CRUDAlbum.getAllAlbums();
                    for (String keyAlbums : albums.keySet()) {
                        Album nAlbum = albums.get(keyAlbums);
                        nAlbum.setUserID(auth.getCurrentUser().getUid());
                        db.getReference().child("albums")
                                .child(auth.getCurrentUser().getUid())
                                .child(nAlbum.getId())
                                .setValue(nAlbum);

                        HashMap<String, Photo> photos = CRUDPhoto.getAllPhotosOfAlbum(nAlbum);
                        for (String photoKey : photos.keySet()) {
                            Photo nPhoto = photos.get(photoKey);
                            db.getReference()
                                    .child("fotos")
                                    .child(nAlbum.getId())
                                    .child(nPhoto.getId())
                                    .setValue(nPhoto);


                        }

                    }


                }
        );

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTaskList();
    }

    private void refreshTaskList() {
        HashMap<String, Album> group = CRUDAlbum.getAllAlbums();
        list.clear();
        for (String key : group.keySet()) {
            Album nAlbum = group.get(key);
            list.add(nAlbum);
        }
        adapter.notifyDataSetChanged();
    }
}
