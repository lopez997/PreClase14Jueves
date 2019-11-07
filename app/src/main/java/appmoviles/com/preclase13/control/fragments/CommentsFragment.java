package appmoviles.com.preclase13.control.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

import appmoviles.com.preclase13.R;
import appmoviles.com.preclase13.model.entity.Comment;

public class CommentsFragment extends DialogFragment {

    private EditText commentEt;
    private Button commentBtn;

    private ListView commentList;
    private ArrayList<Comment> comentarios;
    private ArrayAdapter<Comment> arrayAdapter;

    FirebaseDatabase db;
    private String photoID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, null);
        commentEt = view.findViewById(R.id.comment_et);
        commentBtn = view.findViewById(R.id.comment_btn);
        commentList = view.findViewById(R.id.comment_list);
        db = FirebaseDatabase.getInstance();

        comentarios = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                comentarios);
        commentList.setAdapter(arrayAdapter);

        commentBtn.setOnClickListener((v)->{

            //Enviar comentario

            hideSoftKeyboard(v);
        });

        db.getReference().child("comentarios").child(photoID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Recibir comentarios
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return view;
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }
}
