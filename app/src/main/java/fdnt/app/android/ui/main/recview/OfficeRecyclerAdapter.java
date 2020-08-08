package fdnt.app.android.ui.main.recview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fdnt.app.android.BR;
import fdnt.app.android.databinding.OfficeRowBinding;

/**
* Recycler Adapter for OfficeRecyclerView. Uses StaffViewHolder
* */
public class OfficeRecyclerAdapter extends RecyclerView.Adapter<StaffViewHolder>{
    
    /**ArrayList of People, whose assignment is Office.*/
    protected ArrayList<Person> officeStaff;
    
    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext ());
        OfficeRowBinding itemBinding = OfficeRowBinding.inflate(layoutInflater, parent, false);
        return new StaffViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        holder.bind (BR.person, (Object) officeStaff.get (position));
    }

    @Override
    public int getItemCount() {
        if(officeStaff == null)
            officeStaff = Shared.getStaffWithGivenAssignment (Assignment.Office);
        return officeStaff.size ();
    }
}