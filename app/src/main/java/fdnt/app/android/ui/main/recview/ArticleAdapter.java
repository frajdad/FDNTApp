package fdnt.app.android.ui.main.recview;

import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fdnt.app.android.BR;
import fdnt.app.android.R;
import fdnt.app.android.databinding.ArticleCardRowBinding;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    List<Article> articlesList;
    public ArticleAdapter(List<Article> articlesList) {
        this.articlesList = articlesList;
    }

    @NonNull
    @Override
    //Creates new views
     public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =  LayoutInflater.from(parent.getContext());
        ArticleCardRowBinding itemBinding = ArticleCardRowBinding.inflate(layoutInflater, parent, false);
        return new ArticleViewHolder(itemBinding);
    }

    @Override
    // Replaces the contents of a view
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articlesList.get(position);
        holder.bind(article);

    }

    @Override
    // Returns the size of your dataset
    public int getItemCount() {
        return articlesList != null ? articlesList.size() : 0;
    }
}
