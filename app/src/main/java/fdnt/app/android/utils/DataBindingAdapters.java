package fdnt.app.android.utils;

import android.net.Uri;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class DataBindingAdapters {
	
	@BindingAdapter ("app:src")
	public static void setImageUri(ImageView view, String imageName) {
		view.setImageResource (view.getContext().getResources().getIdentifier(imageName,
				"mipmap", view.getContext().getPackageName()));
	}
	
}
