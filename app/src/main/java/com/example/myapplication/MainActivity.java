package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    RecyclerView Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("SortSetting", MODE_PRIVATE);
        String mSorting = sharedPreferences.getString("Sort", "newest");

        if (mSorting.equals("newest")){
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
        }
        else if (mSorting.equals("oldest")){
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(false);
            linearLayoutManager.setStackFromEnd(false);
        }

        Data = findViewById(R.id.data_view);
        Data.setLayoutManager(linearLayoutManager);


        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Data");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Data_view();
    }

    private void Data_view() {
        mRef = firebaseDatabase.getReference("Data");

        FirebaseRecyclerAdapter<Model, ViewHolder>firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.data_item,
                        ViewHolder.class,
                        mRef
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.SetDetails(getApplicationContext(), model.getName(), model.getLast_name(), model.getYear(),
                                model.getEmail(), model.getPhone());
                    }
                };
        Data.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_sort){
            //display alert dialog to chose sorting
            showShortDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showShortDialog() {
        //option to display in dialog
        String[] sortOption = {"Newest", "Oldest"};
        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Sort by")
                .setItems(sortOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the which argument contains the index position of the selected item
                        if (which==0){
                            //sort by newest
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "newest");
                            editor.apply();
                            recreate();
                        }
                        else if (which==1){
                            //sort by oldest
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "oldest");
                            editor.apply();
                            recreate();
                        }
                    }
                });
        builder.show();
    }

    private void firebaseSearch(String search){
        Query firebaseSearchQuery = mRef.orderByChild("name").startAt(search).endAt(search + "\uf8ff");

        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                      Model.class,
                      R.layout.data_item,
                      ViewHolder.class,
                      firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.SetDetails(getApplicationContext(), model.getName(), model.getLast_name(), model.getYear(),
                                model.getEmail(), model.getPhone());
                    }
                };
        Data.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });

        return true;
    }

    public void New(MenuItem item) {
        Intent intent = new Intent(this, New.class);
        startActivity(intent);
    }
}





class ViewHolder extends RecyclerView.ViewHolder{
    View view;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    @SuppressLint("SetTextI18n")
    public void SetDetails(Context context, String name, String last_name, Integer year, String email, Long phone){
        TextView textView1 = view.findViewById(R.id.name);
        TextView textView2 = view.findViewById(R.id.last_name);
        TextView textView3 = view.findViewById(R.id.year);
        TextView textView4 = view.findViewById(R.id.email);
        TextView textView5 = view.findViewById(R.id.phone);

        textView1.setText(name);
        textView2.setText(last_name);
        textView3.setText(Integer.toString(year));
        textView4.setText(email);
        textView5.setText(Long.toString(phone));
    }
}





class Model{
    private String name, last_name, email;
    Integer year;
    Long phone;

    public Model(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}
