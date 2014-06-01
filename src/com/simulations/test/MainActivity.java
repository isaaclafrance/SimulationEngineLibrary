package com.simulations.test;

import android.app.*;
import android.os.*;

public class MainActivity extends Activity
{
    private GLSView glSView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		
	    glSView = new testGLSView(this);
        setContentView(glSView);
    }
	
    @Override
    protected void onPause()
	{
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        glSView.onPause();
    }

    @Override
    protected void onResume()
	{
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        glSView.onResume();
    }
}
