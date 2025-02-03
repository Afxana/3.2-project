package com.example.project32;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project32.Adapter.ToDoAdapter;
import com.example.project32.Model.TodoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListingActivity extends AppCompatActivity implements OnDialogCloseListener {
    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        list.clear();
        showData();
        adapter.notifyDataSetChanged();
    }

    private ImageButton backBtn;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private FirebaseFirestore firestore;

    private ToDoAdapter adapter;

    private List<TodoModel> list;

    private Query query;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        backBtn = findViewById(R.id.listing_back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListingActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.recycler);
        fab = findViewById(R.id.fab);

        firestore = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        list = new ArrayList<>();
        adapter = new ToDoAdapter(ListingActivity.this, list);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        showData();
    }

    private void showData(){
        query = firestore.collection("tasks").orderBy("time" , Query.Direction.DESCENDING);
                listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if(documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        TodoModel todoModel = documentChange.getDocument().toObject(TodoModel.class).withId(id);
                        list.add(todoModel);
                        adapter.notifyDataSetChanged();
                    }

                }
               // Collections.reverse(list);
                listenerRegistration.remove();
            }
        });
    }



}
