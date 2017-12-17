package corless.matthew.devcalculatorandconverter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class BaseActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		//MenuItem item = findViewById(R.id.nav_about);
		//item.setChecked(true);
	}

	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		}
		else
		{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		boolean handled = false;

		// Handle navigation view item clicks here.
		int id = item.getItemId();
		Fragment fragment;
		Class fragmentClass;
		switch (id)
		{
			case R.id.nav_converter:
				fragmentClass = NumericalConverterFragment.class;
				break;
			case R.id.nav_calculator:
				fragmentClass = NumericalCalculatorFragment.class;
				break;
			case R.id.nav_color:
				fragmentClass = ColorConverterFragment.class;
				break;
			case R.id.nav_report:
				fragmentClass = ReportBugFragment.class;
				break;
			case R.id.nav_rate:
				Intent intent = new Intent(Intent.ACTION_VIEW);
				//Try Google play
				intent.setData(Uri.parse("market://details?id=" + getPackageName()));
				if (!MyStartActivity(intent))
				{
					//Market (Google play) app seems not installed, let's try to open a webbrowser
					intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
					if (!MyStartActivity(intent))
					{
						//Well if this also fails, we have run out of options, inform the user.
						Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
					}
				}
				return true;
			default:
				//wtf do i do?
				fragmentClass = null;
		}

		try
		{
			if (fragmentClass != null)
			{
				fragment = (Fragment) fragmentClass.newInstance();

				// Insert the fragment by replacing any existing fragment
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_main, fragment, "Current")
						.addToBackStack("")
						.commit();
				handled = true;
			}
			else
			{
				//Intent intent = new Intent(this, this.getClass());
				//onCreate(null);
				//onResume();
				//startActivity(intent);
				// we dont have a fragment class, what do we do?
				fragment = getFragmentManager().findFragmentByTag("Current");
				if (fragment != null)
				{// remove current fragment if there is one to get root main element
					getFragmentManager().beginTransaction().remove(fragment).commit();
				}
				else
				{// TODO is this even necessary or can we just do nothing
					onResume();
				}
			}
		}
		catch (Exception e)
		{
			Toast.makeText(this, "Could not navigate to that.", Toast.LENGTH_LONG);
			e.printStackTrace();
		}

		// Highlight the selected item has been done by NavigationView
		item.setChecked(true);
		// Set action bar title
		setTitle(item.getTitle());

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return handled;
	}

	/**
	 * Returns true if and only if an activity was able to start from the given intent. Otherwise
	 * returns false.
	 *
	 * @param intent Intent to be started as an activity.
	 * @return True if activity could start. Otherwise false.
	 */
	private boolean MyStartActivity(Intent intent)
	{
		try
		{
			startActivity(intent);
			return true;
		}
		catch (ActivityNotFoundException e)
		{
			return false;
		}
	}
}
