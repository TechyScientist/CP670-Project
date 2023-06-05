package net.johnnyconsole.cp670.project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import net.johnnyconsole.cp670.project.objects.Course;

public class CourseInformationFragment extends Fragment {

    private final String termCode;
    private final int crn;
    private final CourseListFragment callingFragment;

    public CourseInformationFragment(String termCode, int crn, CourseListFragment callingFragment) {
        this.termCode = termCode;
        this.crn = crn;
        this.callingFragment = callingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_course_information, container, false);
        view.findViewById(R.id.btBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.fvFragment, callingFragment)
                        .commitNow());
        Snackbar.make(container, "Selected Term: " + termCode + "\nSelected CRN: " + crn, Snackbar.LENGTH_LONG).show();
        return view;
    }
}