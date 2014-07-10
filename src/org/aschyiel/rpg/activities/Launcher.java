package org.aschyiel.rpg.activities;

import java.util.Arrays;

import org.aschyiel.rpg.R;

import org.andengine.AndEngine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * The launcher for the game - dictates the first activity we're going to show the user.
 *
 * A lot of the format is copied-off of the AndEngine style for writing a launcher.
 */
public class Launcher extends Activity
{

  private static final String PREF_LAST_APP_LAUNCH_VERSIONCODE_ID = "last.app.launch.versioncode";

  private static final int DIALOG_FIRST_APP_LAUNCH     = 0;
  private static final int DIALOG_NEW_IN_THIS_VERSION  = 1 + Launcher.DIALOG_FIRST_APP_LAUNCH;
  private static final int DIALOG_DEVICE_NOT_SUPPORTED = 1 + Launcher.DIALOG_NEW_IN_THIS_VERSION; 

  @SuppressWarnings( "deprecation" )
  @Override
  public void onCreate( final Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );

    if( !AndEngine.isDeviceSupported() )
    {
      this.showDialog( Launcher.DIALOG_DEVICE_NOT_SUPPORTED );
    }

    setContentView( R.layout.activity_launcher );

    final SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );

    int currentVersion = this.getVersionCode();
    prefs.edit().putInt( Launcher.PREF_LAST_APP_LAUNCH_VERSIONCODE_ID, currentVersion ).commit();

    launchNextActivity();
  }

  /**
  * Figure out what activity we're going to launch next.
  * ie. the start-menu, vs. resuming?, etc.
  */
  public void launchNextActivity()
  {
    // TODO: For dev purposes, jump directly into the sandbox environment.
    startActivity( new Intent( this, org.aschyiel.rpg.activities.Sandbox.class ) );
  }

  /**
  * Returns the current-version of the code, if any.
  */
  public int getVersionCode()
  {
    try
    {
      final PackageInfo pi = this.getPackageManager().getPackageInfo( this.getPackageName(), 0 );
      return pi.versionCode;
    }
    catch ( final PackageManager.NameNotFoundException e )
    {
      return -1;
    }
  }
}