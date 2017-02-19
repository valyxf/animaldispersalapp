package com.example.animaldispersal;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.animaldispersal.adapter.AnimalAdapter;
import com.example.animaldispersal.localdb.LocalDBHelper;
import com.example.davaodemo.R;
import com.example.animaldispersal.localdb.AnimalTable;

public class SearchActivity extends AppCompatActivity {
    private ListView listView;
    private TextView noRecordTextView;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedTextView = (TextView)view.findViewById(R.id.animal_id);
                String selectedAnimalId = selectedTextView.getText().toString();

                Bundle dataBundle = new Bundle();
                dataBundle.putString("SELECTED_ANIMAL_ID", selectedAnimalId);

                Intent intent = new Intent(getApplicationContext(),AnimalDetailActivity.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
        handleIntent(getIntent());

    }
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.animal_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_icon:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }
    private void doMySearch(String query){

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { AnimalTable.COLUMN_ANIMAL_ID , AnimalTable.COLUMN_ANIMAL_TYPE, AnimalTable.COLUMN_SUPERVISOR,
                "CARETAKER_NAME", AnimalTable.COLUMN_DATE_DISTRIBUTED};
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.animal_id, R.id.animal_type, R.id.supervisor,
                R.id.caretaker, R.id.date_distributed };

        // localDBHelper is a SQLiteOpenHelper class connecting to SQLite
        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(this);

        Cursor animalCursor = localDBHelper.getSearchResult(query);
        if (animalCursor.getCount()>0)
        {
            AnimalAdapter adapter = new AnimalAdapter(this, R.layout.item_animal, animalCursor, from, to, 0);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);
            noRecordTextView = (TextView)findViewById(R.id.no_animals_text);
            noRecordTextView.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            noRecordTextView = (TextView)findViewById(R.id.no_animals_text);
            noRecordTextView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
    }
}
