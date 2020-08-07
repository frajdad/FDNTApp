package fdnt.app.android.ui.main.recview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.adapters.LinearLayoutBindingAdapter;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import fdnt.app.android.R;

public class OfficeRecyclerAdapter extends RecyclerView.Adapter<OfficeRecyclerAdapter.ViewHolder>{
    protected Context context;
    
    protected ArrayList<Person> officeStaff;
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.office_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.PersonRole.setText(officeStaff.get (position).role);
        holder.PersonImage.setImageResource(officeStaff.get (position).imageID);
    }

    @Override
    public int getItemCount() {
        if(officeStaff == null)
            officeStaff = Shared.getStaffWithGivenAssignment (Assignment.Office);
        return officeStaff.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView PersonRole;
        ImageView PersonImage, MailIcon, TelIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PersonRole = itemView.findViewById(R.id.PersonRole);
            PersonImage = itemView.findViewById(R.id.PersonImage);
            MailIcon = itemView.findViewById(R.id.MailIcon);
            TelIcon = itemView.findViewById(R.id.TelIcon);

            MailIcon.setOnClickListener(this);
            TelIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.MailIcon) {
                Shared.sendMail(officeStaff.get (getAdapterPosition()).email, context);
            }
            else if (view.getId() == R.id.TelIcon) {
                String mail = "tel:" + officeStaff.get (getAdapterPosition()).phone;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mail));
                context.startActivity(intent);
            }
        }
    }
}