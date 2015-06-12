package com.rohanprasad.calllog.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;
import com.rohanprasad.calllog.HelperFunction;
import com.rohanprasad.calllog.R;
import com.rohanprasad.calllog.adapter.RecyclerViewAdapter;
import com.rohanprasad.calllog.holder.CallLogItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ClickListener, RecyclerViewAdapter.LongClickListener{

    String TAG = "MainActivity";

    RecyclerView mCallList;
    RecyclerView.Adapter mCallListAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<CallLogItem> mDataList;
    FilterMenuLayout menuLayout;
    FilterMenu filterMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuLayout = (FilterMenuLayout) findViewById(R.id.filter_menu);
        filterMenu = new FilterMenu.Builder(this)
                .addItem(R.drawable.ic_user)
                .addItem(R.drawable.ic_time)
                .addItem(R.drawable.ic_duration)
                .attach(menuLayout)
                .withListener(new FilterMenu.OnMenuChangeListener() {
                    @Override
                    public void onMenuItemClick(View view, int pos) {
                        switch (pos) {
                            case 0:
                                mDataList = HelperFunction.sortUsingUser(mDataList);
                                break;
                            case 1:
                                mDataList = HelperFunction.sortUsingDate(mDataList);
                                break;
                            case 2:
                                mDataList = HelperFunction.sortUsingDuration(mDataList);
                                break;
                            default:
                                break;
                        }

                        mCallListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onMenuCollapse() {

                    }

                    @Override
                    public void onMenuExpand() {

                    }
                })
                .build();

        mCallList = (RecyclerView) findViewById(R.id.rv_main_call_list);
        mCallList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mCallList.setLayoutManager(mLayoutManager);

        mDataList = getCallDetails();
        mCallListAdapter = new RecyclerViewAdapter(mDataList, this, this);
        mCallList.setAdapter(mCallListAdapter);

    }

    public ArrayList<CallLogItem> getCallDetails() {

        ArrayList<CallLogItem> mItemList = new ArrayList<>();
        Cursor managedCursor = this.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        //get all keys
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int userid = managedCursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int photoId = managedCursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_ID);

        while (managedCursor.moveToNext()) {

            CallLogItem mItem = new CallLogItem();

            Log.d(TAG, "Getting item:");
            Log.d(TAG, "\tName: " + managedCursor.getString(name));
            if(managedCursor.getString(name) == null){
                mItem.setName(managedCursor.getString(number));
            }else{
                mItem.setName(HelperFunction.getTitleCase(managedCursor.getString(name)));
            }
            Log.d(TAG, "\tNumber: " + managedCursor.getString(number));
            mItem.setPhoneNumber(managedCursor.getString(number));
            Log.d(TAG, "\tType: " + managedCursor.getString(type));
            mItem.setCallStatus(Integer.parseInt(managedCursor.getString(type)));
            Log.d(TAG, "\tTime: " + HelperFunction.getTimeFormat(managedCursor.getString(date)));
            mItem.setTime(HelperFunction.getTimeFormat(managedCursor.getString(date)));
            mItem.setDate(new Date(Long.valueOf(managedCursor.getString(date))));
            Log.d(TAG, "\tDuration: " + HelperFunction.getDurationFormat(managedCursor.getString(duration)));
            mItem.setDuration(Long.parseLong(managedCursor.getString(duration)));
            mItem.setDurationString(HelperFunction.getDurationFormat(managedCursor.getString(duration)));
            mItem.setUserId(HelperFunction.getContactID(MainActivity.this,managedCursor.getString(number)));
            if(managedCursor.getString(name) == null || mItem.getUserId() == null){
                mItem.setUserImageId(null);
            }else{
                mItem.setUserImageId(HelperFunction.getPhotoUri(MainActivity.this, mItem.getUserId()));
            }

            mItemList.add(mItem);
        }

        Collections.reverse(mItemList);
        return mItemList;
    }

    @Override
    public void itemClicked(View view, int pos) {

    }

    @Override
    public void itemLongClicked(View view, int pos) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mDataList.get(pos).getPhoneNumber()));
        MainActivity.this.startActivity(intent);
    }
}