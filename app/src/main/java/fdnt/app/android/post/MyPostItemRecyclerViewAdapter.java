package fdnt.app.android.post;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;

import java.util.List;

import fdnt.app.android.R;
import fdnt.app.android.post.AsyncMailLoad.MailItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MailItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPostItemRecyclerViewAdapter extends RecyclerView.Adapter<MyPostItemRecyclerViewAdapter.ViewHolder> {

    private final List<MailItem> mValues;
    protected Context context;

    public MyPostItemRecyclerViewAdapter(List<MailItem> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.content.setText(cutString(Jsoup.parse(mValues.get(position).content).text()));
        holder.date.setText(mValues.get(position).date);
        holder.sender.setText(mValues.get(position).sender);
        holder.subject.setText(cutString(mValues.get(position).subject));
        if (mValues.get(position).sender != null) {
            holder.circle.setText(mValues.get(position).sender.substring(0, 1).toUpperCase());
        }
    }

    static String cutString(String s) {
        if (s.length() > 40) {
            return s.substring(0, 37) + "...";
        }
        else {
            return s;
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView sender;
        public final TextView subject;
        public final TextView content;
        public final TextView date;
        public final TextView circle;
        public MailItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            sender = view.findViewById(R.id.sender);
            content = view.findViewById(R.id.email_content);
            subject = view.findViewById(R.id.subject);
            date = view.findViewById(R.id.email_date);
            circle = view.findViewById(R.id.name_circle);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            Intent intent = new Intent(context, MailViewer.class);
            intent.putExtra("subject", mValues.get(getAdapterPosition()).subject);
            intent.putExtra("sender", mValues.get(getAdapterPosition()).sender);
            intent.putExtra("content", mValues.get(getAdapterPosition()).content);
            intent.putExtra("date", mValues.get(getAdapterPosition()).date);
            context.startActivity(intent);
        }
    }
}