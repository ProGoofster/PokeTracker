package com.example.poketracker;

import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PokedexListActivity extends AppCompatActivity {

    private ListView listView;
    private SimpleCursorAdapter adapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex_list);

        listView = findViewById(R.id.pokedex_listview);

        // Load data from database
        loadPokedexData();

        // Register for context menu (long press)
        registerForContextMenu(listView);
    }

    private void loadPokedexData() {
        // Query all data from the ContentProvider
        cursor = getContentResolver().query(
                PokedexContentProvider.CONTENT_URI,
                null,  // all columns
                null,  // no selection
                null,  // no selection args
                null   // no sort order
        );

        if (cursor != null && cursor.getCount() > 0) {
            // Columns from database to display
            String[] fromColumns = {
                    PokedexContentProvider.COL_NATNUM,
                    PokedexContentProvider.COL_NAME,
                    PokedexContentProvider.COL_SPECIES,
                    PokedexContentProvider.COL_LEVEL
            };

            // View IDs in the custom row layout
            int[] toViews = {
                    R.id.tv_row_natnum,
                    R.id.tv_row_name,
                    R.id.tv_row_species,
                    R.id.tv_row_level
            };

            // Create SimpleCursorAdapter
            adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.pokedex_row,  // custom row layout
                    cursor,
                    fromColumns,
                    toViews,
                    0
            );

            listView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No Pokémon entries found in database", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.pokedex_listview) {
            menu.setHeaderTitle("Options");
            menu.add(0, 1, 0, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {  // Delete option
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            // Get the _id of the selected item
            cursor.moveToPosition(info.position);
            int idColumnIndex = cursor.getColumnIndex("_id");
            long id = cursor.getLong(idColumnIndex);

            // Delete from database
            int rowsDeleted = getContentResolver().delete(
                    PokedexContentProvider.CONTENT_URI,
                    "_id = ?",
                    new String[]{String.valueOf(id)}
            );

            if (rowsDeleted > 0) {
                Toast.makeText(this, "Pokémon deleted successfully", Toast.LENGTH_SHORT).show();

                // Refresh the list
                refreshList();
            } else {
                Toast.makeText(this, "Failed to delete Pokémon", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void refreshList() {
        // Close old cursor
        if (cursor != null) {
            cursor.close();
        }

        // Reload data
        loadPokedexData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }
}