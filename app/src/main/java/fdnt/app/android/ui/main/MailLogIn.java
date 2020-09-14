package fdnt.app.android.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import fdnt.app.android.R;
import fdnt.app.android.utils.GlobalUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MailLogIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MailLogIn extends Fragment {
	
	public MailLogIn () {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment MailLogIn.
	 */
	public static MailLogIn newInstance () {
		return new MailLogIn ();
	}
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated (savedInstanceState);
		((EditText) getActivity ().findViewById (R.id.mailEmail)).setText ("Poczta");
		TextWatcher textWatcher = new TextWatcher () {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				View button = getActivity ().findViewById (R.id.mail_sign_in_button);
				String password =
						((EditText) getActivity ().findViewById (R.id.mail_password)).getText ().toString ();
				if (password != null && password != "") {
					button.setBackgroundColor (ContextCompat.getColor (getContext (),
							R.color.activeButton));
					button.setClickable (true);
				}else {
					button.setBackgroundColor (ContextCompat.getColor (getContext (),
							R.color.inactiveButton));
					button.setClickable (false);
				}
				
			}
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		};
		((EditText) getActivity ().findViewById (R.id.mail_password)).addTextChangedListener (textWatcher);
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		return inflater.inflate (R.layout.fragment_mail_log_in, container, false);
	}
	
}