package com.example.animaldispersal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.animaldispersal.adapter.AnimalAdapter;
import com.example.animaldispersal.localdb.AnimalTable;
import com.example.animaldispersal.localdb.LocalDBHelper;
import com.example.davaodemo.R;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getName();

    SearchView searchView;
    private ListView listView;
    private AnimalAdapter adapter;
    private TextView noRecordTextView;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint(getString(R.string.enter_search));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
              public boolean onQueryTextSubmit(String query) {
                // Called when the user submits the query.
                doMySearch(query);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                return true;

              }
              @Override
              public boolean onQueryTextChange(String newText) {
                  // Called when the query text is changed by the user.
                  return true;
              }
          });

        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedTextView = (TextView)view.findViewById(R.id.animal_id);
                String selectedAnimalId = selectedTextView.getText().toString();

                Bundle dataBundle = new Bundle();
                dataBundle.putString("SELECTED_ANIMAL_ID", selectedAnimalId);
                Log.d(TAG, "SELECTED_ANIMAL_ID" + selectedAnimalId );

                Intent intent = new Intent(getApplicationContext(),AnimalDetailActivity.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });

        //handleIntent(getIntent());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("searchQuery", searchView.getQuery().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        searchView.setQuery(savedInstanceState.getString("searchQuery"), true);
        //doMySearch(savedInstanceState.getString("searchQuery"));
    }

    /*
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
    */
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
            /*
            case R.id.search_icon:
                onSearchRequested();
                break;
                */
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }
    */
    private void doMySearch(String query){

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { AnimalTable.COLUMN_ANIMAL_ID , AnimalTable.COLUMN_ANIMAL_TYPE,
                AnimalTable.COLUMN_SUPERVISOR, "CARETAKER_NAME", AnimalTable.COLUMN_DATE_DISTRIBUTED};
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.animal_id, R.id.animal_type, R.id.supervisor,
                R.id.caretaker, R.id.date_distributed };

        // localDBHelper is a SQLiteOpenHelper class connecting to SQLite
        LocalDBHelper localDBHelper = LocalDBHelper.getInstance(this);

        Cursor animalCursor = localDBHelper.getSearchResult(query);
        if (animalCursor.getCount()>0)
        {
            adapter = new AnimalAdapter(this, R.layout.item_animal, animalCursor, from, to, 0);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);
            noRecordTextView = (TextView)findViewById(R.id.no_animals_text);
            noRecordTextView.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(null);
            noRecordTextView = (TextView)findViewById(R.id.no_animals_text);
            noRecordTextView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
    }
}
