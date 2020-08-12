package fdnt.app.android.ui.main.recview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fdnt.app.android.BR;
import fdnt.app.android.databinding.ManagementRowBinding;

/**
 * Recycler Adapter for ManagementRecyclerView. Uses StaffViewHolder
 * */
public class ManagementRecyclerAdapter extends RecyclerView.Adapter<StaffViewHolder> {
    
    /**ArrayList of People, whose assignment is Management.*/
    protected ArrayList<Person> managementStaff;
    
    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ManagementRowBinding itemBinding = ManagementRowBinding.inflate(layoutInflater, parent, false);
        return new StaffViewHolder (itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        holder.bind (BR.person, (Object) managementStaff.get(position));
    }

    @Override
    public int getItemCount() {
        if(managementStaff == null)
            managementStaff = RecViewUtil.getStaffWithGivenAssignment(Assignment.Management);
        return managementStaff.size ();
    }

    
}