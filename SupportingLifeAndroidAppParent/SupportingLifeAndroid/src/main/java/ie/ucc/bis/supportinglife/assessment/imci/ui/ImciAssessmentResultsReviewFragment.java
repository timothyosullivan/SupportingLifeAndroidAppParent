package ie.ucc.bis.supportinglife.assessment.imci.ui;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.activity.AssessmentResultsActivity;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.FragmentLifecycle;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewAssessmentAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Class: ImciAssessmentResultsReviewFragment
 * 
 * Responsible for displaying the assessment
 * review items list  
 * 
 * @author TOSullivan
 *
 */
public class ImciAssessmentResultsReviewFragment extends ReviewListFragment implements FragmentLifecycle {
    
	private String pageKey;
    private ReviewAssessmentAdapter reviewAssessmentAdapter;
	
	public static ImciAssessmentResultsReviewFragment create(String pageKey) {
		Bundle args = new Bundle();
		args.putString(ARG_PAGE_KEY, pageKey);

		ImciAssessmentResultsReviewFragment fragment = new ImciAssessmentResultsReviewFragment();
		fragment.setArguments(args);
		return fragment;
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		Bundle args = getArguments();
		setPageKey(args.getString(ARG_PAGE_KEY));
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        setReviewAssessmentAdapter(new ReviewAssessmentAdapter(this));
        // capture all the review items associated with each assessment page
        setCurrentReviewItems(((AssessmentResultsActivity) getActivity()).getReviewItems());
        setListAdapter(getReviewAssessmentAdapter());
    }    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	  View myFragmentView = inflater.inflate(R.layout.fragment_assessment_results_review_tab, container, false);
          
          ListView listView = (ListView) myFragmentView.findViewById(android.R.id.list);        
          listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	     	 
    	  return myFragmentView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {}

    @Override
    public void onPauseFragment(AbstractModel assessmentModel) {}

    @Override
    public void onResumeFragment(AbstractModel assessmentModel) {}

    
	public String getPageKey() {
		return pageKey;
	}

	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

	/**
	 * Getter Method: getReviewAssessmentAdapter()
	 */	
	private ReviewAssessmentAdapter getReviewAssessmentAdapter() {
		return reviewAssessmentAdapter;
	}

	/**
	 * Setter Method: setReviewAssessmentAdapter()
	 */
	private void setReviewAssessmentAdapter(ReviewAssessmentAdapter reviewAssessmentAdapter) {
		this.reviewAssessmentAdapter = reviewAssessmentAdapter;
	}
}
