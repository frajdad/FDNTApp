package fdnt.app.android.ui.main.recview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fdnt.app.android.R;

public class ManagementRecyclerAdapter extends RecyclerView.Adapter<ManagementRecyclerAdapter.ViewHolder> {

    protected Context context;
    private final String[] personRoles = {
            "Przewodniczący",
            "Wiceprzewodniczący",
            "Wiceprzewodniczący"
    };
    private final String[] personMails = {
            "dariusz.kowalczyk@dzielo.pl",
            "pawel.walkiewicz@dzielo.pl",
            "marek.zdrojewski@dzielo.pl"
    };
    private final int[] personImages = {
            R.mipmap.ksdarek_foreground,
            R.mipmap.pawelwalkiewicz_foreground,
            R.mipmap.marekzdrojewski_foreground
    };

    public ManagementRecyclerAdapter() {

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
        holder.PersonRole.setText(personRoles[position]);
        holder.PersonImage.setImageResource(personImages[position]);
    }

    @Override
    public int getItemCount() {
        return personMails.length;
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
            Shared.sendMail(personMails[getAdapterPosition()], context);
        }
    }
}