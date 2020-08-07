package fdnt.app.android.ui.main.recview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fdnt.app.android.R;

public class ManagementRecyclerAdapter extends RecyclerView.Adapter<ManagementRecyclerAdapter.ViewHolder> {

    protected Context context;
    protected ArrayList<Person> managementStaff;
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.management_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagementRecyclerAdapter.ViewHolder holder, int position) {
        holder.PersonRole.setText(managementStaff.get (position).role);
        holder.PersonImage.setImageResource(managementStaff.get (position).imageID);
        
    }

    @Override
    public int getItemCount() {
        if(managementStaff == null)
            managementStaff = Shared.getStaffWithGivenAssignment (Assignment.Management);
        return managementStaff.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView PersonRole;
        ImageView PersonImage, MailIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PersonRole = itemView.findViewById(R.id.PersonRole);
            PersonImage = itemView.findViewById(R.id.PersonImage);
            MailIcon = itemView.findViewById(R.id.MailIcon);
            MailIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Shared.sendMail(managementStaff.get (getAdapterPosition()).email, context);
        }
    }
}