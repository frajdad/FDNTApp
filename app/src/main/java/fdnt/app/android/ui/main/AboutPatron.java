package fdnt.app.android.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import fdnt.app.android.R;
import fdnt.app.android.databinding.AboutPatronBinding;
import fdnt.app.android.ui.main.recview.ArticleAdapter;
import fdnt.app.android.ui.main.recview.RecViewUtil;

public class AboutPatron extends Fragment {
    public static AboutPatron newInstance() {
        return new AboutPatron();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AboutPatronBinding binding = DataBindingUtil.inflate(inflater, R.layout.about_patron, container, false);
        ArticleAdapter articleAdapter = new ArticleAdapter(
                RecViewUtil.deserializeArticleJSON(binding.getRoot().getContext(), R.raw.we_about_patron)
        );
        binding.aboutPatronRecyclerView.setAdapter(articleAdapter);
        return binding.getRoot();
        //comment
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}