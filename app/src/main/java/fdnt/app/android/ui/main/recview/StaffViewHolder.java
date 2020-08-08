package fdnt.app.android.ui.main.recview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.databinding.ViewDataBinding;

import fdnt.app.android.R;

/*
*
 * Class inherits after UniversalViewHolder and implements View.OnClickListener, so it supports
 * specialized behaviour, required in this case.
 */
public class StaffViewHolder extends UniversalViewHolder implements View.OnClickListener {
		
		public StaffViewHolder(ViewDataBinding binding){
			super(binding);
		}
	
		@Override
		public void onClick(View view) {
			Person person = (Person) variable;
			Context context = binding.getRoot ().getContext ();
			Shared.sendMail(person.email, context);
			if (view.getId() == R.id.MailIcon) {
				Shared.sendMail(person.email, context);
			}
			else if (view.getId() == R.id.TelIcon) {
				String mail = "tel:" + person.phone;
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(mail));
				context.startActivity(intent);
			}
		}
	
}
