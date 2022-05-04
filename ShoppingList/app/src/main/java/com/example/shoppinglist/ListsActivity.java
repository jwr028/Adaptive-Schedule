package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.shoppinglist.Adapter.ListToDoAdapter;
import com.example.shoppinglist.Model.ParentToDoModel;
import com.example.shoppinglist.TouchHelper.RecyclerItemTouchHelperLists;
import com.example.shoppinglist.Utils.DataBaseHelper;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import java.util.Collections;
import java.util.List;

public class ListsActivity extends AppCompatActivity implements DialogCloseListener, ListToDoAdapter.OnListListener {

    private int listID; // used for passing to inspectList
    private DataBaseHelper db;

    private RecyclerView listsRecyclerView;
    private ListToDoAdapter listsAdapter;

    private Button newListButton;
    private Button debugButton;
    //private Button specifyButton;
    TextView title;
    private List<ParentToDoModel> listOfLists;

    Button logout;
    private FirebaseAuth fAuth;
    private static final String TAG = "ListsActivity";
    GoogleSignInClient signInClient;


    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        listOfLists = db.getAllLists();
        listsAdapter.setLists(listOfLists);
        listsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        db = new DataBaseHelper(this);
        
        listsRecyclerView = findViewById(R.id.listsRecyclerView);
        listsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        logout = findViewById(R.id.user_logout);

        listsAdapter = new ListToDoAdapter(db,ListsActivity.this, this);
        listsRecyclerView.setAdapter(listsAdapter);

        // DECLARATIONS FOR SWIPING
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelperLists(listsAdapter));
        itemTouchHelper.attachToRecyclerView(listsRecyclerView);

        newListButton = findViewById(R.id.newListButton);
        debugButton = findViewById(R.id.debugButton);
        //specifyButton = findViewById(R.id.specifyButton);

        // fetches items and displays them
        listOfLists = db.getAllLists();
        Collections.reverse(listOfLists); // newest on top
        listsAdapter.setLists(listOfLists);

        // NEW LIST BUTTON FUNCTION
        newListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateListActivity();
            }
        });

        // DEBUG BUTTON FUNCTION
        debugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listsAdapter.deleteAllLists();
                //listsRecyclerView.notifyDataSetChanged(); // need to look into this function
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        /*
        specifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebScrape();
                //listsRecyclerView.notifyDataSetChanged(); // need to look into this function
            }
        });

         */

    }

    private void signOut() {
        // Firebase signout
        fAuth.getInstance().signOut();

        //Google client sign out
        signInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ListsActivity.this, "You have been signed out!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                } else {
                    Log.w(TAG, "Error signing out!");
                }
            }
        });
    }

    // button function to move to InspectList screen
    public void openInspectListActivity() {
        Intent intent = new Intent(this, InspectListActivity.class);
        startActivity(intent);
    }

    // button function to move to CreateList screen
    public void openCreateListActivity() {
        Intent intent = new Intent(this, CreateListActivity.class);
        startActivity(intent);
        //startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    }

    public void openWebScrape() {
        Intent intent = new Intent(this, WebScrape.class);
        startActivity(intent);
        //startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    }

    // clicking on lists
    @Override
    public void onListClick(int position){
        //Log.d(TAG, "onListClick: clicked");
        //listOfLists.get(position); // will be used to load proper info in list inspection
        // get parentID to pass to InspectActivity
        listID = listOfLists.get(position).getId();
        Intent intent = new Intent(this, InspectListActivity.class);
        intent.putExtra("listID",listID);
        startActivity(intent);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        listOfLists = db.getAllLists(); // refreshing Lists from database
        Collections.reverse(listOfLists);
        listsAdapter.setLists(listOfLists);
        listsAdapter.notifyDataSetChanged();
    }
}