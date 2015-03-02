package com.example.administrator.materials;


import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


/**
 * Created by neokree on 16/12/14.
 */
public class FragmentText extends Fragment {
    protected EndlessAdapter mAdapter;
    private PullToRefreshLayout mPullToRefreshLayout;
    MaterialListView list;
    Card card;
    List<Card> cards = new ArrayList<Card>();
    List<String> a=new ArrayList<String>();
    private MaterialListView materiallistview;
    private android.widget.ListView listView;
    private List lists = new ArrayList();

    MyAdapters ada=new MyAdapters();
    public FragmentText() {

        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View row = inflater.inflate(R.layout.framgent, container, false);
        this.listView = (ListView) row.findViewById(R.id.listView);

        this.materiallistview = (MaterialListView) row.findViewById(R.id.material_listview);
        materiallistview.setVisibility(View.GONE);

//        TextView text = new TextView(container.getContext());
//        text.setText("Fragment content");
//        text.setGravity(Gravity.CENTER);


        card = new SmallImageCard(container.getContext());

//        card = new BasicListCard(getActivity());
//
//        MaterialListViewAdapter adapters = new MaterialListViewAdapter(getActivity(), cards);
//        adapters.add("Text1");
//        adapters.add("Text2");
//        adapters.add("Text3");
//        ((BasicListCard) card).setAdapter(adapters);
//
        cards.add(card);
        cards.add(card);
//        adapter = new DemoAdapter(cards);
//        adapter.setRunInBackground(false);
        a.add("123");
        a.add("456");
        ada =  new MyAdapters();
        listView.setAdapter(ada);
//        materiallistview.add(card);
//        materiallistview.setAdapter(new com.dexafree.materialList.controller.MaterialListViewAdapter(getActivity()));
//
//        materiallistview.setCardAnimation(MaterialListView.CardAnimation.SWING_BOTTOM_IN);


        return row;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This is the View which is created by ListFragment
        final ViewGroup viewGroup = (ViewGroup) view;

        materiallistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_LONG).show();
            }
        });
        // We need to create a PullToRefreshLayout manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

        // We can now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                .insertLayoutInto(viewGroup) // We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
                .theseChildrenArePullable(listView) // We need to mark the ListView and it's Empty View as pullable This is because they are not direct children of the
                        // ViewGroup
                .options(Options.create()
                        .build())
                .listener(new OnRefreshListener() { // We can now complete the setup as desired
                    @Override
                    public void onRefreshStarted(View view) {

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ada =  new MyAdapters();
                                        a.clear();
                                        listView.setAdapter(ada);
                                        ada.notifyDataSetChanged();
                                        mPullToRefreshLayout.setRefreshComplete();
                                    }
                                });
                            }
                        };
                        timer.schedule(task, 5000);
                    }
                })
                .setup(mPullToRefreshLayout);
    }


    class MyAdapters extends EndlessAdapter {
        //加载时的动画
        private RotateAnimation rotate = null;
//        private View pendingView = null;
        //构造的时候 要super一下
        MyAdapters() {
            //里面这个很熟悉 常用的baseAdapter
            super(new MyAdapter());

            rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(600);
            rotate.setRepeatMode(Animation.RESTART);
            rotate.setRepeatCount(Animation.INFINITE);
        }


        //显示加载时的view
        @Override
        protected View getPendingView(ViewGroup parent) {
            View row = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row, null);
            ProgressWheel localProgressWheel = (ProgressWheel) row.findViewById(R.id.progress);
            return (row);
        }


        //设置加载的时间
        @Override
        protected boolean cacheInBackground() {
            SystemClock.sleep(2000);
            //设置限制总数据大小
            return (getWrappedAdapter().getCount() <30);
        }


        //往list里面添加数据
        @Override
        protected void appendCachedData() {
            if (getWrappedAdapter().getCount() < 30) {


                for (int i = 0; i <10; i++) {
                    a.add(i+"addData");

                }
            }
        }

    }















    private class MyAdapter implements ListAdapter {
        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return a.size();
        }

        @Override
        public Object getItem(int position) {
            return a.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
        class Holder {
            TextView t, t2;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder h;
            if (convertView == null) {
                h=new Holder();
                convertView = View.inflate(getActivity(), R.layout.items, null);

                convertView.setTag(h);
            } else {
                h = (Holder) convertView.getTag();
            }





            return convertView ;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }



}

