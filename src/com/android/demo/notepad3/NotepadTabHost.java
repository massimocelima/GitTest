package com.android.demo.notepad3;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

public class NotepadTabHost extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TabHost tabHost = getTabHost();
        
        tabHost.addTab(
        		tabHost.newTabSpec("tabNotes")
        			.setIndicator("Notes", getResources().getDrawable(R.drawable.ic_tab_options))
        			.setContent(new Intent(this, Notepadv3.class))); 

       
        tabHost.addTab(
        		tabHost.newTabSpec("tabAdd")
        			.setIndicator("Other Notes", getResources().getDrawable(R.drawable.ic_tab_add))
        			.setContent(new Intent(this, Notepadv3.class))); 

        tabHost.addTab(
        		tabHost.newTabSpec("tabAccount")
        			.setIndicator("Accounts", getResources().getDrawable(R.drawable.ic_tab_copy))
        			.setContent(new Intent(this, Notepadv3.class))); 

    }
	
}
