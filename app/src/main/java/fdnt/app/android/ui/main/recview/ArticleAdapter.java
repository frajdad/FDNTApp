package fdnt.app.android.ui.main.recview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fdnt.app.android.BR;
import fdnt.app.android.R;
import fdnt.app.android.databinding.ArticleCardRowBinding;

public class ArticleAdapter extends RecyclerView.Adapter<UniversalViewHolder> {
    List<Article> articlesList;
    public ArticleAdapter(List<Article> articlesList) {
        this.articlesList = articlesList;
    }

    @NonNull
    @Override
    //Create new views h
     public UniversalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =  LayoutInflater.from(parent.getContext());
        ArticleCardRowBinding itemBinding = ArticleCardRowBinding.inflate(layoutInflater, parent, false);
        return new UniversalViewHolder(itemBinding);
    }

    @Override
    // Replace the contents of a view
    public void onBindViewHolder(@NonNull UniversalViewHolder holder, int position) {
        holder.bind(BR.article, (Object) articlesList.get(position));
    }

    @Override
    // Return the size of your dataset
    public int getItemCount() {
        return articlesList != null ? articlesList.size() : 0;
    }
}
