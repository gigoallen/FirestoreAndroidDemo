package com.gigoallen.firestore181010;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference noteRef = db.document("Notebook/My First Note");

    private CollectionReference notebookRef = db.collection("Notebook");

    private TextView textViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find id
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);

        textViewData = findViewById(R.id.text_view_data);


    }



    public void addNote(View view) {

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
//
//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_TITLE, title);
//        note.put(KEY_DESCRIPTION, description);

        Note note = new Note(title, description);

        notebookRef.add(note);

        //save to firestore
//        noteRef.set(note)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, e.toString());
//            }
//        });
    }

    public void loadNotes(View view) {

       notebookRef.get()
               .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                      String data = "";

                       for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                           final Note note = documentSnapshot.toObject(Note.class);


                           String title = note.getTitle();
                           String description = note.getDescription();

                           note.setDocumentId(documentSnapshot.getId());
                           String documentId = note.getDocumentId();

                           data += "ID:" + documentId + "\nTitle: " + title + "\nDescription: " + description + "\n\n" ;

                       }

                       textViewData.setText(data);

                   }
               });
    }

    @Override
    protected void onStart() {
        super.onStart();

        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(e != null){
                    return;
                }

                String data = "";

                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                    Note note = documentSnapshot.toObject(Note.class);

                    String title = note.getTitle();
                    String description = note.getDescription();

                    note.setDocumentId(documentSnapshot.getId());
                    String documentId = note.getDocumentId();

                    data += "ID:" + documentId + "\nTitle: " + title + "\nDescription: " + description + "\n";
                }

                textViewData.setText(data);
            }
        });

    }

}
