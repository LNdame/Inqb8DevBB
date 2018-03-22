package cite.ansteph.beerly.view.beerlylover.affiliate;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.adapter.LeaderboardRecyclerAdapter;
import cite.ansteph.beerly.model.Leaderboard;

public class LeaderboardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    RecyclerView recyclerView;
    RecyclerView.Adapter mLeaderboardAdapter;

    ArrayList<Leaderboard> mLeaderboardList;
    ArrayList<Leaderboard> mFilteredList;

    public SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        mLeaderboardList = setupList();//retrieveList();
        mFilteredList =mLeaderboardList;

        mLeaderboardAdapter = new LeaderboardRecyclerAdapter(mLeaderboardList, this);
        recyclerView.setAdapter(mLeaderboardAdapter);

        /// search view
        searchView = (SearchView)findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);


    }


    ArrayList<cite.ansteph.beerly.model.Leaderboard> setupList()
    {
        ArrayList<cite.ansteph.beerly.model.Leaderboard>  items = new ArrayList<>();

        items.add(new Leaderboard("1","TobiasT","1500"));
        items.add(new Leaderboard("2","BrowserBee","800"));

        items.add(new Leaderboard("3","HomerS","600"));

        items.add(new Leaderboard("4","Cthulthu","300"));


        // String duration, String task_date, String start, String end, String project, String description, String realduration, String task_break) {
        return  items;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        query = query.toLowerCase();
        mFilteredList = new ArrayList<>();

        for(int i=0; i<mLeaderboardList.size(); i++)
        {
            final String text = mLeaderboardList.get(i).getUsername().toLowerCase();
            if(text.contains(query)){
                mFilteredList.add(mLeaderboardList.get(i));
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(LeaderboardActivity.this));
        mLeaderboardAdapter = new LeaderboardRecyclerAdapter(mFilteredList,LeaderboardActivity.this);
        recyclerView.setAdapter(mLeaderboardAdapter);
        mLeaderboardAdapter.notifyDataSetChanged(); //data set changed
        return true;
    }
}
