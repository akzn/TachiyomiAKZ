package eu.kanade.mangafeed.ui.manga.chapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.kanade.mangafeed.R;
import eu.kanade.mangafeed.data.database.models.Chapter;
import eu.kanade.mangafeed.ui.base.adapter.FlexibleViewHolder;
import eu.kanade.mangafeed.ui.base.fragment.BaseFragment;

public class ChaptersAdapter extends FlexibleAdapter<ChaptersHolder, Chapter> {

    private BaseFragment fragment;
    public FlexibleViewHolder.OnListItemClickListener clickListener;

    public ChaptersAdapter(BaseFragment fragment) {
        this.fragment = fragment;
        mItems = new ArrayList<>();
        clickListener = (FlexibleViewHolder.OnListItemClickListener) fragment;
        setHasStableIds(true);
    }

    @Override
    public void updateDataSet(String param) {}

    @Override
    public ChaptersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_chapter, parent, false);
        return new ChaptersHolder(v, this, clickListener);
    }

    @Override
    public void onBindViewHolder(ChaptersHolder holder, int position) {
        final Chapter chapter = getItem(position);
        holder.onSetValues(fragment.getActivity(), chapter);

        //When user scrolls this bind the correct selection status
        holder.itemView.setActivated(isSelected(position));
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).id;
    }

    public void setItems(List<Chapter> chapters) {
        mItems = chapters;
        notifyDataSetChanged();
    }

    public ChaptersFragment getChaptersFragment() {
        return (ChaptersFragment) fragment;
    }
}
