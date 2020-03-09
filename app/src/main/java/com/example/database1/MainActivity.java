package com.example.database1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText song,singer;
    Button addSinger;
    DatabaseReference databaseReference;
    ListView songList;
    List<String> list;
    ArrayAdapter<String> adapter;
    Button deleteArtist;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("artists");
        setContentView(R.layout.activity_main);
        song = findViewById(R.id.song);
        singer = findViewById(R.id.singer);
        addSinger = findViewById(R.id.addSinger);
        songList = findViewById(R.id.songList);
        deleteArtist = findViewById(R.id.deleteArtist);
        list = new ArrayList<>();
        flag=0;


        addSinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtist();
            }
        });
        deleteArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG","clicked");
                flag=0;
                final String originalSong = song.getText().toString().trim();
                final String originalSinger = singer.getText().toString().trim();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot artistSnapshot : dataSnapshot.getChildren()){
                            String key = artistSnapshot.getKey();
                            String song11 = artistSnapshot.child("songName").getValue().toString().trim();
                            String singer11 = artistSnapshot.child("singerName").getValue().toString().trim();

                            if(originalSong.equals(song11) && originalSinger.equals(singer11)){
                                flag=1;
                                showToast("Item successfully deleted");
                                deleteArtist(key);
                            }
                        }
                        if(flag==0){
                            showToast("No item matched");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void deleteArtist(String artistId) {
        DatabaseReference dReference = FirebaseDatabase.getInstance().getReference().child("artists").child(artistId);
        dReference.removeValue();
    }

    public void addArtist(){
        String song1 = song.getText().toString().trim();
        String singer1 = singer.getText().toString().trim();

        if(TextUtils.isEmpty(song1)){
            showToast("song can't be empty");
            return;
        }
        if(TextUtils.isEmpty(singer1)){
            showToast("singer can't be empty");
            return;
        }
        String key = databaseReference.push().getKey();
        Artist artist = new Artist(key,song1,singer1);
        databaseReference.child(key).setValue(artist);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot artistSnapshot : dataSnapshot.getChildren()){
                    String songName = artistSnapshot.child("songName").getValue().toString();
                    list.add(songName);
                }
                adapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,list);
                songList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
