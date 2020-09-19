package fdnt.app.android.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import fdnt.app.android.R;
import fdnt.app.android.databinding.HowtohelpFragmentBinding;
import fdnt.app.android.ui.main.recview.ArticleAdapter;
import fdnt.app.android.ui.main.recview.RecViewUtil;

public class HowToHelpTab extends Fragment {
    public static HowToHelpTab newInstance() {
        return new HowToHelpTab();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        HowtohelpFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.howtohelp_fragment, container,false);
        ArticleAdapter articleAdapter = new ArticleAdapter(
                RecViewUtil.deserializeArticleJSON(binding.getRoot().getContext(), R.raw.how_to_help)
        );
        binding.howToHelpRecyclerView.setAdapter(articleAdapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}