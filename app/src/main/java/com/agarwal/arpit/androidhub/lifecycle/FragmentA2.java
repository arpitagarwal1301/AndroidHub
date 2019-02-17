package com.agarwal.arpit.androidhub.lifecycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.agarwal.arpit.androidhub.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.agarwal.arpit.androidhub.Utils.StringConstants.RECEIVED_STRING;

public class FragmentA2 extends Fragment {


    @BindView(R.id.edit_text)
    EditText editText;

    private Bundle args;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a2,container,false);
        ButterKnife.bind(this,view);
        //NOTE :: attachToRoot should be false since the fragment is getting attached via FragmentManager not by us .

        setUpView();

        return view;
    }

    private void setUpView() {
        args = getArguments();
        if (null != args){
            editText.setText(args.getString(RECEIVED_STRING));
        }
    }
}
