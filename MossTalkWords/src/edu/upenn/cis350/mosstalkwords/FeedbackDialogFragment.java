package edu.upenn.cis350.mosstalkwords;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class FeedbackDialogFragment extends DialogFragment {

	public FeedbackDialogFragment() {
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

		b.setCancelable(false);
		LayoutInflater li = getActivity().getLayoutInflater();
		
		b.setView(li.inflate(R.layout.dialog, null));
       
        // Create the AlertDialog object and return it
        return b.create();
    }
	
}
