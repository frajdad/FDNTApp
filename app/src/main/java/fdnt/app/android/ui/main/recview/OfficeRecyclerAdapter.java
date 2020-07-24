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

import java.util.List;

import fdnt.app.android.R;



public class OfficeRecyclerAdapter extends RecyclerView.Adapter<OfficeRecyclerAdapter.ViewHolder>{
    protected Context context;
    List<String> personRoles;
    List<String> personMails;
    List<String> personTels;

    private final int[] personImages = {
            R.mipmap.ksnycz_foreground,
            R.mipmap.annamarszalek_foreground,
            R.mipmap.monikagawracz_foreground,
            R.mipmap.marzenasawula_foreground,
            R.mipmap.malgorzatakucharska_foreground,
            R.mipmap.paulinaworozbit_foreground,
            R.mipmap.hubertszczypek_foreground
    };

    public OfficeRecyclerAdapter(List<String> personRoles, List<String> personMails, List<String> personTels) {
        this.personRoles = personRoles;
        this.personMails = personMails;
        this.personTels = personTels;
    }

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
        holder.PersonRole.setText(personRoles.get(position));
        holder.PersonImage.setImageResource(personImages[position]);
    }

    @Override
    public int getItemCount() {
        return personMails.size();
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
                String mail = "mailto:" + personMails.get(getAdapterPosition());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mail));
                context.startActivity(intent);
            }
            else if (view.getId() == R.id.TelIcon) {
                String mail = "tel:" + personTels.get(getAdapterPosition());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mail));
                context.startActivity(intent);
            }
        }
    }
}