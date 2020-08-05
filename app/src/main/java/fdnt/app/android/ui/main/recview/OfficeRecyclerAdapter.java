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

import fdnt.app.android.R;

public class OfficeRecyclerAdapter extends RecyclerView.Adapter<OfficeRecyclerAdapter.ViewHolder>{
    protected Context context;

    private final String[] personRoles = {
            "Koordynator ds. formacji stypendystów",
            "Asystentka Zarządu",
            "Menadżer ds. stypendialnych",
            "Menedżer ds. Dnia Papieskiego",
            "Menedżer ds. kontaktu z Darczyńcami",
            "Menedżer ds. kampanii społecznych",
            "Kierownik Biura Prasowego"
    };
    private final String[] personMails = {
            "lukasz.nycz@dzielo.pl",
            "sekretariat@dzielo.pl",
            "stypendia@dzielo.pl",
            "dzienpapieski@dzielo.pl",
            "darczyncy@dzielo.pl",
            "paulina.worozbit@dzielo.pl",
            "biuroprasowe@dzielo.pl"
    };
    private final String[] personTels = {
            "503504407",
            "668286129",
            "734445490",
            "602830082",
            "668285959",
            "881678857",
            "662506859"
    };
    private final int[] personImages = {
            R.mipmap.ksnycz_foreground,
            R.mipmap.annamarszalek_foreground,
            R.mipmap.monikagawracz_foreground,
            R.mipmap.marzenasawula_foreground,
            R.mipmap.malgorzatakucharska_foreground,
            R.mipmap.paulinaworozbit_foreground,
            R.mipmap.hubertszczypek_foreground
    };

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
        holder.PersonRole.setText(personRoles[position]);
        holder.PersonImage.setImageResource(personImages[position]);
    }

    @Override
    public int getItemCount() {
        return personMails.length;
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
                Shared.sendMail(personMails[getAdapterPosition()], context);
            }
            else if (view.getId() == R.id.TelIcon) {
                String mail = "tel:" + personTels[getAdapterPosition()];
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mail));
                context.startActivity(intent);
            }
        }
    }
}