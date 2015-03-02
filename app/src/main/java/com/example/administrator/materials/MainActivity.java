package com.example.administrator.materials;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconCompat;
import com.dd.processbutton.iml.SubmitProcessButton;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.makeramen.RoundedImageView;
import com.readystatesoftware.viewbadger.BadgeView;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends ActionBarActivity implements MaterialTabListener {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.tabHost)
    MaterialTabHost mTabHost;
    @InjectView(R.id.pager)
    ViewPager mPager;
    @InjectView(R.id.avatar_lg)
    RoundedImageView mAvatarLg;
    @InjectView(R.id.message_like_button)
    ImageView mMessageLikeButton;
    @InjectView(R.id.love_button)
    ImageView mLoveButton;
    @InjectView(R.id.menu_message_answer_count)
    BadgeView mMenuMessageAnswerCount;
    @InjectView(R.id.menu_user)
    RelativeLayout mMenuUser;
    @InjectView(R.id.menu_todo_total)
    TextView mMenuTodoTotal;
    @InjectView(R.id.menu_todo_count)
    BadgeView mMenuTodoCount;
    @InjectView(R.id.menu_todo)
    LinearLayout mMenuTodo;
    @InjectView(R.id.menu_timeline_count)
    BadgeView mMenuTimelineCount;
    @InjectView(R.id.menu_timeline)
    LinearLayout mMenuTimeline;
    @InjectView(R.id.menu_relationship)
    LinearLayout mMenuRelationship;
    @InjectView(R.id.menu_settings_new)
    BadgeView mMenuSettingsNew;
    @InjectView(R.id.menu_settings)
    LinearLayout mMenuSettings;
    @InjectView(R.id.menu_exit)
    SubmitProcessButton mMenuExit;
    @InjectView(R.id.main_random)
    AddFloatingActionButton mMainRandom;
    @InjectView(R.id.main_ask)
    FloatingActionButton mMainAsk;
    @InjectView(R.id.main_user)
    FloatingActionButton mMainUser;
    private ViewPagerAdapter pagerAdapter;
    private MaterialMenuIconCompat materialMenu;
    private boolean isDrawerOpened;
    private MenuDrawer mDrawer;
    private Resources res;
    private CountDownTimer mExitCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = this.getResources();
        mDrawer = MenuDrawer.attach(this, Position.LEFT);
        mDrawer.setContentView(R.layout.activity_main);
        mDrawer.setMenuView(R.layout.activity_left);
        this.mDrawer.setSlideDrawable(R.drawable.ic_navigation_drawer);
        this.mDrawer.setDrawerIndicatorEnabled(true);
        ButterKnife.inject(this);



        initfloatbutton();

        mToolbar.setTitle("收件箱");
        mToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolbar);
        this.materialMenu = new MaterialMenuIconCompat(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        mTabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        mPager = (ViewPager) this.findViewById(R.id.pager);

        // init view pager
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                mTabHost.setSelectedNavigationItem(position);
            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            mTabHost.addTab(
                    mTabHost.newTab()
                            .setIcon(geticons(i))
                            .setTabListener(this)
            );
        }
        this.mDrawer.setOnDrawerStateChangeListener(new MenuDrawer.OnDrawerStateChangeListener() {


            @Override
            public void onDrawerStateChange(int i, int i2) {

            }

            @Override
            public void onDrawerSlide(float paramAnonymousFloat, int i) {
                MaterialMenuIconCompat localMaterialMenuIconCompat = MainActivity.this.materialMenu;
                MaterialMenuDrawable.AnimationState localAnimationState = MaterialMenuDrawable.AnimationState.BURGER_ARROW;
                if (MainActivity.this.isDrawerOpened)
                    paramAnonymousFloat = 2.0F - paramAnonymousFloat;
                localMaterialMenuIconCompat.setTransformationOffset(localAnimationState, paramAnonymousFloat);

            }
        });


    }

    private void initfloatbutton() {

        mMainAsk.setVisibility(View.INVISIBLE);

        mMainUser.setVisibility(View.INVISIBLE);
    }


    @OnClick({R.id.menu_exit, R.id.menu_settings,R.id.main_random})
    void onMaybeMissingClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_exit:
                SubmitProcessButton localSubmitProcessButton = (SubmitProcessButton) view;
                localSubmitProcessButton.setProgress(100);
                if (this.mExitCountDownTimer != null) {
                    this.mExitCountDownTimer.cancel();
                    localSubmitProcessButton.setText("(ToT)つ~~~");
                    finish();
                    return;
                }
                this.mExitCountDownTimer = new CountDownTimer(1000, 10) {


                    public void onFinish() {
                        mExitCountDownTimer = null;
                        mMenuExit.setProgress(0);
                        mMenuExit.setText("退出");
                    }

                    public void onTick(long paramAnonymousLong) {
                        int i = (int) paramAnonymousLong / 10;
                        mMenuExit.setProgress(i * i / 100);
                    }
                };
                localSubmitProcessButton.setText("再点一下");
                this.mExitCountDownTimer.start();
                break;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                Toast.makeText(this, "ss", Toast.LENGTH_LONG).show();
                break;
            case R.id.main_random:
            ////触发添加消息


                break;
        }

    }

    private Drawable geticons(int i) {

        return res.getDrawable(R.drawable.ic_group_white_24dp);

    }

    @Override
    public void onBackPressed() {
// 这里处理逻辑代码，大家注意：该方法仅适用于2.0或更新版的sdk

        this.mDrawer.toggleMenu();

    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        // when the tab is clicked the pager swipe content to the tab position
        mPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }
}
