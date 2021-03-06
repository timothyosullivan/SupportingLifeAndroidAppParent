package ie.ucc.bis.supportinglife.activity;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.ccm.model.CcmAssessmentResultsModel;
import ie.ucc.bis.supportinglife.assessment.ccm.ui.CcmAssessmentTreatmentsFragment;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.rule.engine.ClassificationRuleEngine;
import ie.ucc.bis.supportinglife.rule.engine.TreatmentRecommendation;
import ie.ucc.bis.supportinglife.rule.engine.TreatmentRuleEngine;
import ie.ucc.bis.supportinglife.rule.engine.Diagnostic;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.BaseAdapter;

/**
 * Class: CcmAssessmentResultsActivity
 * 
 * Responsible for displaying CCM assessment results.
 * 
 * The results shown comprise of the following:
 * 
 * 1. Assessment Review Items Tab
 * 2. Classifications Tab
 * 3. Recommended Treatments Tab
 * 4. Vaccine Tab
 * 5. Follow-up Tab
 * 
 * @author TOSullivan
 *
 */
public class CcmAssessmentResultsActivity extends AssessmentResultsActivity {

	/* 
	 * Method: onCreate() 
	 * 
	 * Perform initialisation of all fragments and loaders.
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setAssessmentResultsModel(new CcmAssessmentResultsModel(this));
        
        setContentView(R.layout.activity_assessment_results);
        setTitleFromActivityLabel(R.id.action_bar_title_text);
        setViewPager((ViewPager) findViewById(R.id.assessment_results_pager));
        
        // extract the assessment page data sent by the assessment bread-crumb wizard
		Intent intent = getIntent();
        setReviewItems((ArrayList<ReviewItem>) intent.getSerializableExtra(CcmAssessmentActivity.ASSESSMENT_REVIEW_ITEMS));
        
        // capture the patient assessment data
        // entered by the user and construct
        // the patient instance
        contructPatientInstance();
        
        // resolve CCM classifications based on assessed symptoms        
        setClassificationRuleEngine(new ClassificationRuleEngine());
        getClassificationRuleEngine().readCcmClassificationRules((SupportingLifeBaseActivity) this);
        getClassificationRuleEngine().determinePatientClassifications(this, getReviewItems(), getPatientAssessment(), getClassificationRuleEngine().getSystemCcmClassifications());
        
        // identify CCM treatments
        setTreatmentRuleEngine(new TreatmentRuleEngine());
        getTreatmentRuleEngine().readCcmTreatmentRules((SupportingLifeBaseActivity) this);
        getTreatmentRuleEngine().determineCcmTreatments(this, getReviewItems(), getPatientAssessment());
 
        // only record the patient visit in the DB if dealing with a non-guest user type 
        if (!isGuestUser()) {
        	recordPatientVisit();
        }
                
        // create a new Action bar and set title to strings.xml
        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        // attach the Tabs to the fragment classes and set the tab title.
        setTabsAdapter(new TabsAdapter(this, getViewPager()));
        
        // add assessment review items tab
        getTabsAdapter().addTab(bar.newTab().setText(R.string.ccm_assessment_results_review_tab_title),
        		getAssessmentResultsModel().getAnalyticsPages().get(0));
               
        // add classifications tab
        getTabsAdapter().addTab(bar.newTab().setText(R.string.ccm_assessment_results_classifications_tab_title),
        		getAssessmentResultsModel().getAnalyticsPages().get(1));

        // add treatments tab
        getTabsAdapter().addTab(bar.newTab().setText(R.string.ccm_assessment_results_treatments_tab_title),
        		getAssessmentResultsModel().getAnalyticsPages().get(2));
 
        // open on classifications tab by default
        getTabsAdapter().setDefaultTab();

       if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
	}

	/**
	 * Display the treatments tab and scroll to the
	 * relevant classification if applicable, otherwise flash header 
	 * 
	 * @param classificationName 
	 */
	public void displayTreatmentTab(String classificationTitle) {
		getTabsAdapter().displayTreatmentTab();
		CcmAssessmentTreatmentsFragment treatmentsFragment = (CcmAssessmentTreatmentsFragment) 
				getSupportFragmentManager().getFragments().get(TabsAdapter.TREATMENT_TAB_INDEX);

		if (treatmentsFragment != null) {
			// refresh adapter data set - gets view redrawn
			((BaseAdapter) treatmentsFragment.getCcmTreatmentAdapter()).notifyDataSetChanged();
		}
	}
	
	/**
	 * Responsible for determining if all drug related treatments
	 * have been administered
	 * 
	 * @return boolean - true if all drug treatments administered
	 */
	public boolean checkAllDrugTreatmentsAdministered() {
		boolean allDrugTreatmentsAdministered = true;
		
		for (Diagnostic diagnostic : getPatientAssessment().getDiagnostics()) {
			for (TreatmentRecommendation treatmentRec : diagnostic.getTreatmentRecommendations()){
				if (treatmentRec.isDrugAdministered()) {
					if (!treatmentRec.isTreatmentAdministered()) {
						allDrugTreatmentsAdministered = false;
						return allDrugTreatmentsAdministered;
					}
				}
			}
		}
		return allDrugTreatmentsAdministered;
	}	
	
	/**
	 * Responsible for determining if all treatments (both drug and non-drug related)
	 * have been administered
	 * 
	 * @return boolean - true if all treatments administered
	 */
	public boolean checkAllTreatmentsAdministered() {
		boolean allTreatmentsAdministered = true;
		
		for (Diagnostic diagnostic : getPatientAssessment().getDiagnostics()) {
			for (TreatmentRecommendation treatmentRec : diagnostic.getTreatmentRecommendations()){
				if (!treatmentRec.isTreatmentAdministered()) {
					allTreatmentsAdministered = false;
					return allTreatmentsAdministered;
				}
			}
		}
		return allTreatmentsAdministered;
	}	
}

