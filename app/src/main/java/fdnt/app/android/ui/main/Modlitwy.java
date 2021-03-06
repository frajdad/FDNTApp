package fdnt.app.android.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import fdnt.app.android.R;
import fdnt.app.android.databinding.ModlitwyFragmentBinding;
import fdnt.app.android.ui.main.recview.Article;
import fdnt.app.android.ui.main.recview.ArticleAdapter;
import fdnt.app.android.ui.main.recview.RecViewUtil;

public class Modlitwy extends Fragment {
    public static Modlitwy newInstance() {
        return new Modlitwy();
    }

    List<Article> articlesList;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ModlitwyFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.modlitwy_fragment, container, false);

        ArticleAdapter articleAdapter = new ArticleAdapter(
                RecViewUtil.deserializeArticleJSON(binding.getRoot().getContext(), R.raw.prayers)
        );
        binding.prayersRecyclerView.setAdapter(articleAdapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}