/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gideon.bluetooth;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * @author gideon
 */
public class Screen_Home extends Fragment
{
    Boolean allowsBackPress = true;
    
    public Screen_Home(){}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        //Verander die Title van die Fragment/Activity
        getActivity().setTitle(getResources().getStringArray(R.array.menuitems)[0]);
        
        //Inflate die fragment met sy layout.
        View rootView = inflater.inflate(R.layout.screen_home, container, false);
        
        //Doen veranderinge aan layout hier... bv Textviews ens...
        return rootView;
    }
}