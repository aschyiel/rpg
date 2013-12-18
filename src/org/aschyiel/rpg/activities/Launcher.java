package org.aschyiel.rpg.activities;

import java.util.Arrays;

import org.aschyiel.rpg.R;

import org.andengine.AndEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.Toast;

/**
 * The launcher for the game - the very first activity the user willl see.. 
 *
 */
public class Launcher extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String PREF_LAST_APP_LAUNCH_VERSIONCODE_ID = "last.app.launch.versioncode";

	private static final int DIALOG_FIRST_APP_LAUNCH = 0;
	private static final int DIALOG_NEW_IN_THIS_VERSION      = 1 + Launcher.DIALOG_FIRST_APP_LAUNCH;
	private static final int DIALOG_DEVICE_NOT_SUPPORTED     = 1 + Launcher.DIALOG_NEW_IN_THIS_VERSION;

	// ===========================================================
	// Fields
	// ===========================================================

	private int previousVersion;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		if( !AndEngine.isDeviceSupported() )
		{
			this.showDialog(Launcher.DIALOG_DEVICE_NOT_SUPPORTED);
		}

		setContentView( R.layout.activity_launcher ); 

		final SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );

		int currentVersion = this.getVersionCode();
		int previousVersion = prefs.getInt( Launcher.PREF_LAST_APP_LAUNCH_VERSIONCODE_ID, -1 );

		if ( isFirstTime( "first.app.launch" ) )
		{
		  //this.showDialog( ExampleLauncher.DIALOG_FIRST_APP_LAUNCH );
		}
		else if ( -1 != previousVersion && ( previousVersion < currentVersion ) )
		{
			//this.showDialog( ExampleLauncher.DIALOG_NEW_IN_THIS_VERSION );
		}

		prefs.edit().putInt( Launcher.PREF_LAST_APP_LAUNCH_VERSIONCODE_ID, currentVersion ).commit();
	
		// TODO
		launchTerrainActivity();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected Dialog onCreateDialog(final int pId) {
		switch(pId) {
			case DIALOG_DEVICE_NOT_SUPPORTED:
				return new AlertDialog.Builder(this)
					.setTitle(R.string.dialog_device_not_supported_title)
					.setMessage(R.string.dialog_device_not_supported_message)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(android.R.string.ok, null)
					.create();
			case DIALOG_FIRST_APP_LAUNCH:
				return new AlertDialog.Builder(this)
					.setTitle(R.string.dialog_first_app_launch_title)
					.setMessage(R.string.dialog_first_app_launch_message)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(android.R.string.ok, null)
					.create();
			case DIALOG_NEW_IN_THIS_VERSION:
				final int[] versionCodes = this.getResources().getIntArray(R.array.new_in_version_versioncode);
				final int versionDescriptionsStartIndex = Math.max(0, Arrays.binarySearch(versionCodes, previousVersion) + 1);

				final String[] versionDescriptions = this.getResources().getStringArray(R.array.new_in_version_changes);

				final StringBuilder sb = new StringBuilder();
				for(int i = versionDescriptions.length - 1; i >= versionDescriptionsStartIndex; i--) {
					sb.append("--------------------------\n");
					sb.append(">>>  Version: " + versionCodes[i] + "\n");
					sb.append("--------------------------\n");
					sb.append(versionDescriptions[i]);

					if(i > versionDescriptionsStartIndex){
						sb.append("\n\n");
					}
				}

				return new AlertDialog.Builder(this)
					.setTitle(R.string.dialog_new_in_this_version_title)
					.setMessage(sb.toString())
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(android.R.string.ok, null)
					.create();
			default:
				return super.onCreateDialog(pId);
		}
	}

	/**
	* Launch the main terrain activity.
	*/
	public void launchTerrainActivity()
	{
		startActivity( new Intent( this, org.aschyiel.rpg.activities.Terrain.class ) ); 
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isFirstTime(final String pKey){
		final SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
		if(prefs.getBoolean(pKey, true)){
			prefs.edit().putBoolean(pKey, false).commit();
			return true;
		}
		return false;
	}

	public int getVersionCode() {
		try {
			final PackageInfo pi = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			return pi.versionCode;
		} catch (final PackageManager.NameNotFoundException e) {
			return -1;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}