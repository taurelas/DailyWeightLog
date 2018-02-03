package com.leadinsource.dailyweightlog;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.leadinsource.dailyweightlog.db.DataContract;
import com.leadinsource.dailyweightlog.db.DbHelper;

public class MainActivity extends AppCompatActivity {

    EditText etWeight, etFatPc;

    long lastInsertedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        etWeight = findViewById(R.id.et_today_weight);
        etFatPc = findViewById(R.id.et_fat_pc);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return true;
    }


    public void addWeight(View view) {

        float weight, fatPc;


        if(etWeight.getText().length()==0) {
            return;
        }

        try {
            weight = Float.parseFloat(etWeight.getText().toString());
        } catch(NumberFormatException nfe) {
            nfe.printStackTrace();
            return;
        }

        SQLiteDatabase db = new DbHelper(this).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DataContract.WeightEntry.COLUMN_WEIGHT_IN_KG, weight);
        if(etFatPc.getText().length()>0) {

            try {
                fatPc = Float.parseFloat(etFatPc.getText().toString());
                values.put(DataContract.WeightEntry.COLUMN_FAT_PC, fatPc);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }

        lastInsertedId = db.insert(DataContract.WeightEntry.TABLE_NAME, null, values);
        etWeight.setText("");
        etFatPc.setText("");

    }

    void deleteWeight(long id) {
        SQLiteDatabase db = new DbHelper(this).getWritableDatabase();

        db.delete(DataContract.WeightEntry.TABLE_NAME, DataContract.WeightEntry._ID + "=" + id, null);
    }
}
