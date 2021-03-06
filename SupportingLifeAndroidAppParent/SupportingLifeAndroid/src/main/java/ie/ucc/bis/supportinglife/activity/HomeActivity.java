package ie.ucc.bis.supportinglife.activity;

import ie.ucc.bis.supportinglife.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

/**
 * This is a simple activity that demonstrates the Supporting LIFE dashboard user interface.
 *
 * @author timothyosullivan
 */

public class HomeActivity extends SupportingLifeBaseActivity {
	
	/**
	 * OnCreate method is called when the activity is first created.
	 * 
	 * This is where all of the normal static set up should occur
	 * e.g. create views, bind data to lists, etc.
	 * 
	 * The method also provides a Bundle parameter containing the activity's
	 * previously frozen state (if there was one).
	 * 
	 * This method is always followed by onStart().
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		configureButtonFontIcons();
	}
	
	/**
	 * onResume method is called when the activity will start interacting with the user.
	 * 
	 * At this point your activity is at the top of the activity stack, with user input going to it.
	 * 
	 * This method is always followed by onPause().
	 *
	 */
	@Override
	protected void onResume () {
		super.onResume ();
		
		// localisation values needs to be picked up explicilty for HomeActivity
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = settings.getString(LANGUAGE_SELECTION_KEY, "");
        setLocale(lang);
	    setContentView(R.layout.activity_home);
	    
	    // load icon fonts for buttons
		configureButtonFontIcons();
	}
	
	/**
	 * Responsible for configuring the font icons (provided by FlatIcon)
	 * on the dashboard buttons
	 * 
	 */
	private void configureButtonFontIcons() {
		// load the flaticon font for the buttons
		Typeface font = Typeface.createFromAsset(getAssets(), DASHBOARD_ICON_TYPEFACE_ASSET);
		
		((Button) findViewById(R.id.dashboard_imci_assessment_button)).setTypeface(font);
		((Button) findViewById(R.id.dashboard_ccm_assessment_button)).setTypeface(font);
		((Button) findViewById(R.id.dashboard_training_button)).setTypeface(font);
		((Button) findViewById(R.id.dashboard_settings_button)).setTypeface(font);
		((Button) findViewById(R.id.dashboard_opinions_button)).setTypeface(font);
		((Button) findViewById(R.id.dashboard_about_button)).setTypeface(font);
	}
	
	/**
	 * Determine if this activity should display an ActionBar when it is
	 * shown.
	 * 
	 * @return boolean
	 */
	@Override
	protected boolean shouldDisplayActionBar() {
		return true;
	}
	
	/**
	 * Click Handler: Handler the click of a dashboard button
	 * 
	 * @param view View
	 * @return void
	 */
	public void onClickDashboardButton(View view) {
		int id = view.getId();
		switch(id) {
			case R.id.dashboard_about_button :
				startActivity(new Intent(getApplicationContext(), AboutActivity.class));
				break;
			case R.id.dashboard_imci_assessment_button :
				startActivity(new Intent(getApplicationContext(), ImciAssessmentActivity.class));
				break;
			case R.id.dashboard_ccm_assessment_button :
				startActivity(new Intent(getApplicationContext(), CcmAssessmentActivity.class));
				break;
			case R.id.dashboard_training_button :
				startActivity(new Intent(getApplicationContext(), TrainingActivity.class));
				break;
			case R.id.dashboard_settings_button :
				startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
				break;
			case R.id.dashboard_opinions_button :
				startActivity(new Intent(getApplicationContext(), OpinionsActivity.class));
				break;				
			default : 
				break;
		} // end of switch
		
		// configure the activity animation transition effect
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

} // end class
