package com.badjatya.ledger.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.badjatya.ledger.R;
import com.badjatya.ledger.models.GirviFilter;
import com.badjatya.ledger.models.GirviTransaction;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tanmay on 11/14/2016.
 */
public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.filter_activity_name_ans)EditText nameEditText;
    @BindView(R.id.filter_activity_fathers_name_ans) EditText fatherNameEditText;
    @BindView(R.id.filter_activity_village_ans)TextView villageTextView;
    @BindView(R.id.filter_activity_caste_ans)TextView castTextView;
    @BindView(R.id.filter_activity_clear_frame)FrameLayout clearFrame;
    @BindView(R.id.filter_activity_clear_frame_text)TextView clearFrameTextView;
    @BindView(R.id.filter_activity_okay_button)Button okayButton;
    private static final int VILLAGES=001;
    private static final int CASTE=002;
    GirviFilter girviFilter;
    Intent data =new Intent();
    boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        girviFilter=(GirviFilter)getIntent().getSerializableExtra("filter");
        updateResult();

        if(filterableDataCount()>0)
        {
            flag=true;
            nameEditText.setText(girviFilter.getName());
            flag=false;
            fatherNameEditText.setText(girviFilter.getFatherName());
            villageTextView.setText(girviFilter.getVillage());
            castTextView.setText(girviFilter.getCaste());
            updateFrameText();
        }

        villageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchAndAdd.class);
                intent.putExtra("LOGIC", VILLAGES);
                startActivityForResult(intent, VILLAGES);
            }
        });

        castTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchAndAdd.class);
                intent.putExtra("LOGIC", CASTE);
                startActivityForResult(intent, CASTE);
            }
        });

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (flag == true) {
                    flag = false;
                    return;
                }
                girviFilter.setName(s.toString());
                updateResult();
                updateFrameText();
            }
        });
        fatherNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(flag==true)
                {
                    flag=false;
                    return;
                }
                girviFilter.setFatherName(s.toString());
                updateResult();
                updateFrameText();
            }
        });

        clearFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterableDataCount() > 0) {
                    nameEditText.setText("");
                    fatherNameEditText.setText("");
                    villageTextView.setText("");
                    girviFilter.setVillage("");
                    villageTextView.setText("");
                    updateResult();
                    girviFilter.setCaste("");
                    castTextView.setText("");
                    updateResult();
                    updateFrameText();
                }
            }
        });

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == VILLAGES && data != null) {
            villageTextView.setText(data.getStringExtra("NAME"));
            girviFilter.setVillage(data.getStringExtra("NAME"));
            updateResult();
            updateFrameText();
        }
        if (requestCode == CASTE && data != null) {
            castTextView.setText(data.getStringExtra("NAME"));
            girviFilter.setCaste(data.getStringExtra("NAME"));
            updateResult();
            updateFrameText();
        }
    }
    private void updateResult()
    {
        data.putExtra("filter", girviFilter);
        setResult(RESULT_OK,data);
    }

    private void updateFrameText()
    {
        int count=filterableDataCount();
        if(count>0)
        {
           clearFrameTextView.setText("Touch Here To Clear!");
        }
        else
        {
            clearFrameTextView.setText("Enter atleast one!");
        }
    }

    private int filterableDataCount()
    {
        int count=girviFilter.getName()!=null&&girviFilter.getName().length()>0?1:0
                +girviFilter.getFatherName()!=null&&girviFilter.getFatherName().length()>0?1:0
                +girviFilter.getVillage()!=null&&girviFilter.getVillage().length()>0?1:0
                +girviFilter.getCaste()!=null&&girviFilter.getCaste().length()>0?1:0;
        return count;
    }


}
