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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fdnt.app.android.R;

public class ManagementRecyclerAdapter extends RecyclerView.Adapter<ManagementRecyclerAdapter.ViewHolder> {

    protected Context context;
    List<String> personRoles;
    List<String> personMails;
    private final int[] personImages = { R.drawable.ksdarek, R.drawable.pawelwalkiewicz, R.drawable.marekzdrojewski };

    public ManagementRecyclerAdapter(List<String> personRoles, List<String> personMails) {
        this.personRoles = personRoles;
        this.personMails = personMails;
    }

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
        holder.PersonRole.setText(personRoles.get(position));
        holder.PersonImage.setImageResource(personImages[position]);
    }

    @Override
    public int getItemCount() {
        return personMails.size();
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
            String mail = "mailto:" + personMails.get(getAdapterPosition());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mail));
            context.startActivity(intent);
        }
    }
}