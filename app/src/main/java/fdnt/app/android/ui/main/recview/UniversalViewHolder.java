package fdnt.app.android.ui.main.recview;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
* Class represents universal view holder, which implements basic functionality of supporting data
 * binding
* */
public class UniversalViewHolder extends RecyclerView.ViewHolder {

	protected ViewDataBinding binding;
	protected Object variable;
	
	public UniversalViewHolder(ViewDataBinding binding){
		super(binding.getRoot ());
		this.binding = binding;
	}
	/**
	 * Method allows to bind variable.
	 * @param variableID ID of variable gotten from BR.
	 * @param obj Value which is gonna be set for given variable. Be sure, for types to match.
	 * */
	public void bind(int variableID, Object obj){
		binding.setVariable (variableID, obj);
		variable = obj;
		binding.executePendingBindings ();
	}
	
}
