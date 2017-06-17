package com.example.lenovo.contentprovider;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.type;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    Context context = this;
    private ArrayList<Location> sampleLocs= new ArrayList<>();
    private final String TAG = MainActivity.class.getSimpleName();
    public static final String[] LOCATION_PROJECTION = {
            MapDbContract.MapEntry.COLUMN_LATITUDE,
            MapDbContract.MapEntry.COLUMN_LONGITUDE,
            MapDbContract.MapEntry.COLUMN_NAME,
            MapDbContract.MapEntry.COLUMN_TYPE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fillsampleLocs();
        displayAllInfo();
        findViewById(R.id.bulk_insert).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                long insertCount = insertSampleValues();
                Toast.makeText(context,insertCount + " items inserted.", Toast.LENGTH_SHORT).show();
                displayAllInfo();
            }
        });
        findViewById(R.id.remove_all).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                long itemsRemoved = context.getContentResolver().delete(MapDbContract.MapEntry.CONTENT_URI, null, null);

                Toast.makeText(context,itemsRemoved + " items removed.", Toast.LENGTH_SHORT).show();
                displayAllInfo();
            }
        });
        findViewById(R.id.query_all).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllInfo();

            }
        });
        findViewById(R.id.query_id).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog);
                final       EditText editText = (EditText) dialog.findViewById(R.id.id);
                Button button = (Button) dialog.findViewById(R.id.done);
                dialog.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = editText.getText().toString();
                        Log.d(TAG, "URI: " + MapDbContract.MapEntry.CONTENT_URI.buildUpon().appendPath(id).build().toString());
                        Cursor cursor = context.getContentResolver().query(MapDbContract.MapEntry.CONTENT_URI.buildUpon().appendPath(id).build(),null,null,null,null);
                        cursor.moveToNext();
                        textView = (TextView) findViewById(R.id.textView);
                        textView.setText("");
                        textView.append("Id: " + cursor.getInt(cursor.getColumnIndex(MapDbContract.MapEntry._ID)));
                        textView.append("\nLatitude: " + cursor.getDouble(cursor.getColumnIndex(MapDbContract.MapEntry.COLUMN_LATITUDE)));
                        textView.append("\nLongitude: " + cursor.getDouble(cursor.getColumnIndex(MapDbContract.MapEntry.COLUMN_LONGITUDE)));
                        textView.append("\nName: " + cursor.getString(cursor.getColumnIndex(MapDbContract.MapEntry.COLUMN_NAME)));
                        textView.append("\nType: " + cursor.getString(cursor.getColumnIndex(MapDbContract.MapEntry.COLUMN_TYPE)) + "\n\n");
                        dialog.dismiss();
                    }
                });

            }
        });
        findViewById(R.id.remove_id).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog);
                final       EditText editText = (EditText) dialog.findViewById(R.id.id);
                Button button = (Button) dialog.findViewById(R.id.done);
                dialog.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = editText.getText().toString();
                        Log.d(TAG, "URI: " + MapDbContract.MapEntry.CONTENT_URI.buildUpon().appendPath(id).build().toString());
                        int itemsDeleted = context.getContentResolver().delete(MapDbContract.MapEntry.CONTENT_URI.buildUpon().appendPath(id).build(),null,null);

                        dialog.dismiss();
                        Toast.makeText(context,itemsDeleted + " items removed.", Toast.LENGTH_SHORT).show();
                        displayAllInfo();
                    }
                });

            }
        });
        findViewById(R.id.insert).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog);
                final EditText editText = (EditText) dialog.findViewById(R.id.id);
                final EditText lat = (EditText) dialog.findViewById(R.id.lat);
                final EditText longi = (EditText) dialog.findViewById(R.id.longi);
                final EditText val = (EditText) dialog.findViewById(R.id.name);
                final EditText type = (EditText) dialog.findViewById(R.id.type);
                lat.setVisibility(View.VISIBLE);
                longi.setVisibility(View.VISIBLE);
                type.setVisibility(View.VISIBLE);
                editText.setVisibility(View.GONE);

                val.setVisibility(View.VISIBLE);

                Button button = (Button) dialog.findViewById(R.id.done);
                dialog.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(LOCATION_PROJECTION[0],lat.getText().toString());
                        contentValues.put(LOCATION_PROJECTION[1],longi.getText().toString());
                        contentValues.put(LOCATION_PROJECTION[2],val.getText().toString());
                        contentValues.put(LOCATION_PROJECTION[3],type.getText().toString());

                        context.getContentResolver().insert(MapDbContract.MapEntry.CONTENT_URI,contentValues);
                        dialog.dismiss();
                        displayAllInfo();

                    }
                });

            }
        });
        findViewById(R.id.update).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog);
                final EditText editText = (EditText) dialog.findViewById(R.id.id);
                final EditText lat = (EditText) dialog.findViewById(R.id.lat);
                final EditText longi = (EditText) dialog.findViewById(R.id.longi);
                final EditText val = (EditText) dialog.findViewById(R.id.name);
                final EditText type = (EditText) dialog.findViewById(R.id.type);
                lat.setVisibility(View.VISIBLE);
                longi.setVisibility(View.VISIBLE);
                type.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);

                val.setVisibility(View.VISIBLE);

                Button button = (Button) dialog.findViewById(R.id.done);
                dialog.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(LOCATION_PROJECTION[0],lat.getText().toString());
                        contentValues.put(LOCATION_PROJECTION[1],longi.getText().toString());
                        contentValues.put(LOCATION_PROJECTION[2],val.getText().toString());
                        contentValues.put(LOCATION_PROJECTION[3],type.getText().toString());
                        String id = editText.getText().toString();
                        int itemsUpdated = context.getContentResolver().update(MapDbContract.MapEntry.CONTENT_URI.buildUpon().appendPath(id).build(),contentValues,null,null);
                        Toast.makeText(context,itemsUpdated + " items updated.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        displayAllInfo();

                    }
                });

            }
        });



    }

    public int insertSampleValues(){

        ContentValues sampleValues[] = new ContentValues[sampleLocs.size()];
        for(int i=0;i<sampleLocs.size();i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put(LOCATION_PROJECTION[0],sampleLocs.get(i).latitude);
            contentValues.put(LOCATION_PROJECTION[1],sampleLocs.get(i).longitude);
            contentValues.put(LOCATION_PROJECTION[2],sampleLocs.get(i).name);
            contentValues.put(LOCATION_PROJECTION[3],sampleLocs.get(i).type);
            sampleValues[i] = contentValues;
        }
        int insertCount = this.getContentResolver().bulkInsert(MapDbContract.MapEntry.CONTENT_URI,sampleValues);
        return insertCount;
    }
    public void displayAllInfo(){
        textView = (TextView) findViewById(R.id.textView);
        Cursor cursor = this.getContentResolver().query(MapDbContract.MapEntry.CONTENT_URI,null,null,null,null);
        textView.setText("");
        if(cursor.getCount()==0)
            textView.setText("Database is empty");
        while(cursor.moveToNext()){
            textView.append("Id: " + cursor.getInt(cursor.getColumnIndex(MapDbContract.MapEntry._ID)));
            textView.append("\nLatitude: " + cursor.getDouble(cursor.getColumnIndex(MapDbContract.MapEntry.COLUMN_LATITUDE)));
            textView.append("\nLongitude: " + cursor.getDouble(cursor.getColumnIndex(MapDbContract.MapEntry.COLUMN_LONGITUDE)));
            textView.append("\nName: " + cursor.getString(cursor.getColumnIndex(MapDbContract.MapEntry.COLUMN_NAME)));
            textView.append("\nType: " + cursor.getString(cursor.getColumnIndex(MapDbContract.MapEntry.COLUMN_TYPE)) + "\n\n");
        }


    }

    public void fillsampleLocs(){
        sampleLocs.add(new Location(27.1750, 78.0422, "Taj Mahal", "Monument"));
        sampleLocs.add(new Location(40.4319, 116.5704, "Great Wall of China", "Historical Place"));
        sampleLocs.add(new Location(48.858222, 2.2945, "Eiffel Tower", "Tower"));
        sampleLocs.add(new Location(19.1334, 72.9133, "IIT Bombay", "Institute"));


    }

    public class Location{
        double latitude;
        double longitude;
        String name;
        String type;

        public Location(double latitude, double longitude, String name, String type) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.name = name;
            this.type = type;
        }
    }
}
