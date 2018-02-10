package com.xrone.julis.compous.HomeFragment.communication.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.xrone.julis.compous.R;
import com.xrone.julis.compous.HomeFragment.communication.Model.Topic;
import com.xrone.julis.compous.HomeFragment.communication.adapter.TopicSimpleListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicSimpleListFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TopicSimpleListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.com_fragment_topic_simple_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TopicSimpleListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }

    public void notifyDataSetChanged(List<Topic> topicSimpleList) {
        adapter.setTopicSimpleListWithNotify(topicSimpleList);
    }

}
