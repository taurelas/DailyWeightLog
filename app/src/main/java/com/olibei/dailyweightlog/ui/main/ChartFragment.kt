package com.olibei.dailyweightlog.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.olibei.dailyweightlog.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the [ChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
//private OnFragmentInteractionListener mListener;

class ChartFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }


        /*binding.chart.setData(lineData);
        binding.chart.setDrawGridBackground(false);
        binding.chart.setDescription(null);
        YAxis yAxis = binding.chart.getAxisRight();
        yAxis.setEnabled(false);
        XAxis xAxis = binding.chart.getXAxis();
        xAxis.setEnabled(false);
        binding.chart.invalidate();*/
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    //override fun onAttach(context: Context?) {
   //     super.onAttach(context)
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
   // }

    override fun onDetach() {
        super.onDetach()
        /*mListener = null;*/
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChartFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ChartFragment {
            val fragment = ChartFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     * public interface OnFragmentInteractionListener {
     * // TODO: Update argument type and name
     * void onFragmentInteraction(Uri uri);
     * } */
}// Required empty public constructor
