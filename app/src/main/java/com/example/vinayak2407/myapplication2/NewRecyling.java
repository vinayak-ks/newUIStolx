package com.example.vinayak2407.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewRecyling extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recyling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b = getIntent().getExtras();
        String ds  = b.getString("table");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(ds);

        recyclerView = (RecyclerView) findViewById(R.id.new_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        //prodialog.setMessage("Loading");
        //prodialog.show();

        FirebaseRecyclerAdapter<Book,BookViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, BookViewHolder>(
                Book.class,
                R.layout.new_recycler_row,
                BookViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(BookViewHolder viewHolder, Book model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setName(model.getName());
                viewHolder.setPrice("₹ "+model.getPrice());

                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle args = new Bundle();
                        //args.putString("fragment","CropsP");
                        args.putString("key",post_key);
                        args.putString("dtable","Book");
                        Intent i = new Intent(NewRecyling.this,DetailedActivity.class);
                        i.putExtras(args);
                        startActivity(i);
                    }
                });

            }

          /*  @Override
            public void onDataChanged() {
                if (prodialog != null && prodialog.isShowing()) {
                    prodialog.dismiss();
                }
            }*/
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public  static class BookViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public BookViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setName(String name) {
            TextView namee = (TextView) mView.findViewById(R.id.itemName);
            namee.setText(name);
        }

        public void setPrice(String price) {
            TextView textView2 = (TextView) mView.findViewById(R.id.itemPrice);
            textView2.setText(price);
        }

        public void setImage(Context ctx, String image) {
            ImageView imageView = (ImageView) mView.findViewById(R.id.itemImage);
            Glide.with(ctx).load(image).into(imageView);

        }
    }

}
