package com.agarwal.arpit.androidhub.lifecycle

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agarwal.arpit.androidhub.R
import com.agarwal.arpit.common.logging.HubLog
import kotlinx.android.synthetic.main.fragment_a1.*

private const val TAG = "FragmentA1"

class FragmentA1 : Fragment(){


    private var mFragmentCommunication : FragmentCommunication? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentCommunication = context as FragmentCommunication;// interfaces are initialised in onAttach of fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HubLog.v("Fragment :onCreate")
        retainInstance = true

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_a1, container, false)
        //NOTE :: attachToRoot should be false since the fragment is getting attached via FragmentManager not by us .
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        mFragmentCommunication?.onActionDone(edit_text.getText().toString())
    }
    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        HubLog.v("Fragment :onDestroy")
        super.onDestroy()

    }

    override fun onDetach() {
        HubLog.v("Fragment :onDetach")
        super.onDetach()
    }

}