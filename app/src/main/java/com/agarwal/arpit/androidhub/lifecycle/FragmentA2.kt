package com.agarwal.arpit.androidhub.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agarwal.arpit.androidhub.R
import com.agarwal.arpit.common.utils.BUNDLE_RECEIVED_STRING
import kotlinx.android.synthetic.main.fragment_a2.*

class FragmentA2 : Fragment() {

    private var args: Bundle? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_a2, container, false)
        return view
    }

    private fun setUpView() {
        args = arguments
        args?.let { edit_text.setText(it.getString(BUNDLE_RECEIVED_STRING)) }
    }


}