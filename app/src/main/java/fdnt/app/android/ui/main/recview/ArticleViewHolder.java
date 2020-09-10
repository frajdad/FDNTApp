package fdnt.app.android.ui.main.recview;

import android.view.View;
import android.widget.TextView;

import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;

import fdnt.app.android.R;

/*
 *
 * Class inherits after UniversalViewHolder and implements View.OnClickListener, so it supports
 * specialized behaviour, required in this case.
 */
public class ArticleViewHolder extends UniversalViewHolder implements View.OnClickListener {
    boolean expanded;
    public ArticleViewHolder(ViewDataBinding binding) {
        super(binding);
        binding.getRoot().findViewById(R.id.card_header).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        binding.getRoot().findViewById(R.id.card_content).setVisibility(expanded ? View.GONE : View.VISIBLE);
        expanded = !expanded;
    }

    public void bind(Article article){
        binding.setVariable (BR.article, article);
        expanded = article.isExpanded();
        binding.executePendingBindings();
    }
}
