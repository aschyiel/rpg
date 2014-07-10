package org.aschyiel.rpg.activities;

import org.aschyiel.rpg.R;
import org.aschyiel.rpg.activities.sandbox.*;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

/**
* The developer sandbox provides quick access to use-cases (levels).
*
* Organized (Shamelessly) JUST-LIKE the AndEngine ExampleLauncher.
* @see http://developer.android.com/reference/android/app/ExpandableListActivity.html
*/
public class Sandbox extends ExpandableListActivity
{ 
  private SandboxListAdapter adapter;
  
  @Override
  public void onCreate( final Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_sandbox );
    adapter = new SandboxListAdapter( this );
    setListAdapter( adapter );
  } 

  @Override
  public boolean onChildClick( final ExpandableListView parent,
                               final View view,
                               final int groupPosition,
                               final int childPosition,
                               final long id ) {
    startActivity( new Intent( this, adapter.getChild( groupPosition, childPosition ).klass ) );
    return super.onChildClick( parent, view, groupPosition, childPosition, id );
  }

  private class SandboxListAdapter extends BaseExpandableListAdapter
  { 
    private final SandboxGroup[] groups;
    private final Context ctx;
    public SandboxListAdapter( final Context ctx )
    {
      this.ctx = ctx;
      groups = new SandboxGroup[]
          {
            SandboxGroup.MOVEMENT,
            SandboxGroup.COMBAT
          };
    }

    @Override
    public SandboxItem getChild( final int groupPosition, final int childPosition )
    {
      return groups[ groupPosition ].children[ childPosition ];
    }

    @Override
    public long getChildId( final int _, final int childPosition )
    {
      return childPosition;
    }

    @Override
    public int getChildrenCount( final int groupPosition )
    {
      return groups[ groupPosition ].children.length;
    }

    @Override
    public SandboxGroup getGroup( final int groupPosition )
    {
      return groups[ groupPosition ];
    }

    @Override
    public int getGroupCount()
    {
      return groups.length;
    }

    @Override
    public long getGroupId( final int groupPosition )
    {
      return groupPosition;
    }

    @Override
    public boolean isChildSelectable( final int _, final int __ )
    {
      return true;
    }

    @Override
    public boolean hasStableIds()
    {
      return true;
    }

    @Override
    public View getChildView( final int       groupPosition,
                              final int       childPosition,
                              final boolean   _,
                              final View      convertView,
                              final ViewGroup __ )
    {
      final View v = ( null != convertView )?
          convertView : LayoutInflater.from( ctx ).inflate( R.layout.sandbox_item, null );
      ((TextView) v.findViewById( R.id.sandbox_item ))
          .setText( getChild( groupPosition, childPosition ).name );
      return v;
    }

    @Override
    public View getGroupView( final int       groupPosition,
                              final boolean   _,
                              final View      convertView,
                              final ViewGroup __ )
    {
      final View v = ( null != convertView )?
          convertView : LayoutInflater.from( ctx ).inflate( R.layout.sandbox_group, null );
      ((TextView) v.findViewById( R.id.sandbox_group ))
          .setText( getGroup( groupPosition ).name );
      return v;
    }
  }

  private enum SandboxItem
  {
    BASIC_MOVEMENT(         BasicMovement.class,       R.string.sandbox_basic_movement ),
    MOVEMENT_VS_LAND_TYPES( MovementVsLandTypes.class, R.string.sandbox_movement_vs_land_types ),
    BASIC_COMBAT(           BasicCombat.class,         R.string.sandbox_basic_combat );

    public final Class<? extends Terrain> klass;
    public final int name;

    private SandboxItem( final Class<? extends Terrain> klass, final int name )
    {
      this.klass = klass;
      this.name  = name;
    }
  }
  
  private enum SandboxGroup
  {
    MOVEMENT( R.string.sandbox_group_movement, SandboxItem.BASIC_MOVEMENT, SandboxItem.MOVEMENT_VS_LAND_TYPES ),
    COMBAT(   R.string.sandbox_group_combat,   SandboxItem.BASIC_COMBAT );

    public final SandboxItem[] children;
    public final int name;

    private SandboxGroup( final int name, final SandboxItem ... children )
    {
      this.name     = name;
      this.children = children;
    }
  }

}
