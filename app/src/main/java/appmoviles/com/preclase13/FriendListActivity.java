package appmoviles.com.preclase13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import appmoviles.com.preclase13.control.adapters.PhotoAdapter;
import appmoviles.com.preclase13.model.entity.Album;
import appmoviles.com.preclase13.model.entity.Friend;
import appmoviles.com.preclase13.model.entity.Photo;

public class FriendListActivity extends AppCompatActivity {

    private ListView friendList;
    private ArrayAdapter<Friend> friendArrayAdapter;
    private ArrayList<Friend> friends;

    private ListView albumList;
    private ArrayAdapter<Album> albumArrayAdapter;
    private ArrayList<Album> albums;

    private ListView photoList;
    private PhotoAdapter photoAdapter;
    private Button backButton;

    private Friend friend;

    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        db = FirebaseDatabase.getInstance();

        friends = new ArrayList<>();
        friendArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, friends);

        albums = new ArrayList<>();
        albumArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, albums);

        photoAdapter = new PhotoAdapter();


        friendList = findViewById(R.id.friendList);
        friendList.setAdapter(friendArrayAdapter);

        albumList = findViewById(R.id.albumList);
        albumList.setAdapter(albumArrayAdapter);

        photoList = findViewById(R.id.photoList);
        photoList.setAdapter(photoAdapter);
        backButton = findViewById(R.id.back_button);


        db.getReference().child("amigos").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        friends.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Friend friend = child.getValue(Friend.class);
                            friends.add(friend);
                        }
                        friendArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        friendList.setOnItemClickListener(
                (adapterView, view, i, l) -> {
                    friend = friends.get(i);
                    friendList.setVisibility(View.GONE);
                    albumList.setVisibility(View.VISIBLE);
                    db.getReference().child("albums")
                            .child(friend.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    albums.clear();
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        Album album = child.getValue(Album.class);
                                        albums.add(album);
                                    }
                                    albumArrayAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
        );

        albumList.setOnItemClickListener(
                (adapterView, view, i, l) -> {
                    Album album = albums.get(i);
                    albumList.setVisibility(View.GONE);
                    photoList.setVisibility(View.VISIBLE);

                    db.getReference()
                            .child("fotos")         //<---ESTO ERA LO
                            .child(album.getId())   //<---QUE FALTABA!
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            photoAdapter.clear();
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                Photo photo = child.getValue(Photo.class);
                                photoAdapter.addPhoto(photo);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
        );

        backButton.setOnClickListener((v)->{
            if( photoList.getVisibility() == View.VISIBLE ){
                photoList.setVisibility(View.GONE);
                albumList.setVisibility(View.VISIBLE);
            }else if( albumList.getVisibility() == View.VISIBLE ){
                albumList.setVisibility(View.GONE);
                friendList.setVisibility(View.VISIBLE);
            }else if( friendList.getVisibility() == View.VISIBLE ){
                finish();
            }
        });
    }

    public Friend getFriend() {
        return friend;
    }
}
