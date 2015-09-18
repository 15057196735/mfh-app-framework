package com.mfh.comna.bizz.member.view;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.mfh.comna.R;
import com.mfh.comna.view.BaseComnActivity;

/**
 * Created by Shicy on 14-4-24.
 */
public class MemberEditActivity extends BaseComnActivity {

    private String[] memberTypes = new String[]{"业主", "租户"};
    private String[] xiaoquList = new String[]{"风尚小区", "其它小区"};

    private Spinner typeSpinner;
    private Spinner xiaoquSpinner;

    @Override
    public int getLayoutId() {
        return R.layout.activity_member_edit;
    }

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar_save);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typeSpinner = (Spinner)findViewById(R.id.member_type_spinner);
        xiaoquSpinner = (Spinner)findViewById(R.id.member_xq_spinner);

        typeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, memberTypes));
        xiaoquSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, xiaoquList));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

}
