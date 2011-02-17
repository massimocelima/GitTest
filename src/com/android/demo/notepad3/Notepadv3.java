/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.demo.notepad3;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView;

public class Notepadv3 extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private NotesDbAdapter mDbHelper;

    private String _postUrl = "http://10.0.2.2:1245/Service1/SampleItem";
    private String _getUrl = "http://10.0.2.2:1245/Service1/SampleItem";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);
        mDbHelper = new NotesDbAdapter(this);
        
        ListView listView = this.getListView();
        listView.addHeaderView(buildHeader());		
        listView.addFooterView(buildFooter(), null, false);

        mDbHelper.open();
        fillData();
        registerForContextMenu(listView);
        

        PostDataTest();
        GetDataTest();
    }
   
    private void GetDataTest()
    {
    
		try {

			// Send GET request to <service>/GetPlates         
	    	HttpGet request = new HttpGet(_getUrl);         
	    	request.setHeader("Accept", "application/json");         
	    	request.setHeader("Content-type", "application/json");          
	    	DefaultHttpClient httpClient = new DefaultHttpClient();         
	    	HttpResponse response;
			response = httpClient.execute(request);
	    	HttpEntity responseEntity = response.getEntity();                   
	    	// Read response data into buffer         
	    	char[] buffer = new char[(int)responseEntity.getContentLength()];         
	    	InputStream stream = responseEntity.getContent();         
	    	InputStreamReader reader = new InputStreamReader(stream);         
	    	reader.read(buffer);         
	    	stream.close();          
	    	
	        GsonBuilder gBuilder = new GsonBuilder();
	        gBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
	        Gson gson = gBuilder.create();
	        List<SampleItem> items = gson.fromJson(new String(buffer), new TypeToken<List<SampleItem>>(){}.getType());
	    	
	        int count = items.size();
	        SampleItem x = items.get(0);
	        count += 1;
	    	//JSONArray plates = new JSONArray(new String(buffer));          
	    	// Reset plate spinner         
	    	//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);         
	    	//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);         
	    	//for (int i = 0; i < plates.length(); ++i) {             
	    	//	adapter.add(plates.getString(i));         
	        //}         
	        //plateSpinner.setAdapter(adapter); 

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}          

    }
    
    private void PostDataTest()
    {
		try {
	        
	        HttpPost request = new HttpPost(_postUrl); 
	        request.setHeader("Accept", "application/json");             
	        request.setHeader("Content-type", "application/json"); 
	        
	        GsonBuilder gBuilder = new GsonBuilder();
	        gBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
	        Gson gson = gBuilder.create();
	        String jsonString = gson.toJson(GetTestData());
	        
	        //JSONStringer vehicle;
			//vehicle = new JSONStringer()                 
			//	.object()                     
			//		.key("vehicle")                        
			//		.object()                             
			//			.key("plate").value("1")                             
			//			.key("make").value("2")                             
			//			.key("model").value("3")                             
			//			.key("year").value("4")                         
			//		.endObject()                     
			//	.endObject();
			
		    //   String test = vehicle.toString();
		    
	        
	        
		       JSONObject jobject = new JSONObject(jsonString); 
		       
		       StringEntity entity = new StringEntity(jobject.toString()); 
		       request.setEntity(entity); 
		       
	            // Send request to WCF service             
		       DefaultHttpClient httpClient = new DefaultHttpClient();
		       HttpResponse response = httpClient.execute(request);
		       
		       HttpEntity responseEntity =  response.getEntity();
		       
		       char[] buffer = new char[(int)responseEntity.getContentLength()];         
		       InputStream stream = responseEntity.getContent();         
		       InputStreamReader reader = new InputStreamReader(stream);         
		       reader.read(buffer);         
		       stream.close();          
		       		       
		       String jsonResponse = new String(buffer);
		       SampleItem responseItem = gson.fromJson(jsonResponse, SampleItem.class );
		       
		       Log.d("WebInvoke", "response:  " + jsonResponse);
		       
		       
		       
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private SampleItem GetTestData()
    {
    	SampleItem result = new SampleItem();
    	result.Id = 55;
    	result.StringValue ="From CLient";
    	result.Created = new Date(99, 9, 9);
    	result.Child = new SampleItemChild();
    	result.Child.Id = 66;
    	result.Child.StringValue = "Child From Client";
    	
    	return result;
    }
    

	private View buildHeader() {
        LayoutInflater inflater = getLayoutInflater(); 
        View header = inflater.inflate( R.layout.notes_list_header, null, false);
        return header;
	}
	
	private View buildFooter() {
		
	    LayoutInflater inflater = getLayoutInflater(); 
	    View footer = inflater.inflate( R.layout.notes_list_footer, null, false);
	    return footer;
	 
	/*	
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(     
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);   
		
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x55000000);
		ll.setLayoutParams(layoutParams);

		TextView tv = new TextView(this);
		tv.setText("Footer!");
		tv.setGravity(android.view.Gravity.CENTER);
		//tv.setLayoutParams(layoutParams);
		
		ll.addView(tv);
		
		
		return ll;
		*/
	}

    private void fillData() {

        // Get all of the rows from the database and create the item list
        Cursor  notesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(notesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{NotesDbAdapter.KEY_TITLE, NotesDbAdapter.KEY_BODY};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1, R.id.note_description};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter notes = 
            new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to);
        setListAdapter(notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Uses the menu xml file to load the options menu
        MenuInflater inflater = getMenuInflater(); 
        inflater.inflate(R.menu.notepad_menu, menu); 
        return true; 
        
        //menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        //return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case R.id.notepad_menu_add:
                createNote();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Note Actions");
        
        MenuInflater inflater = getMenuInflater(); 
        inflater.inflate(R.menu.notepad_context_menu, menu); 
        //menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.notepad_context_menu_delete:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteNote(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if( id >= 0)
        {
	        PostDataTest();
	        GetDataTest();
	        //Intent i = new Intent(this, NoteEdit.class);
	        //i.putExtra(NotesDbAdapter.KEY_ROWID, id);
	        //startActivityForResult(i, ACTIVITY_EDIT);
        } else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}
