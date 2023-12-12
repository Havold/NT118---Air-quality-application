package com.example.natural;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class FragmentMap extends Fragment {

    String[] items = {"Temperature","Humidity","Amount of rain"};
    String[] itemsTime = {"Day","Month","Year"};
    AutoCompleteTextView autoCompleteTxt;
    AutoCompleteTextView autoCompleteTxtTime;

    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItemsTime;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        autoCompleteTxt = view.findViewById(R.id.auto_complete_txt);
        autoCompleteTxtTime = view.findViewById(R.id.auto_complete_txt_TimeFrame);
        adapterItems = new ArrayAdapter<String>(requireContext(),R.layout.list_item,items);
        adapterItemsTime = new ArrayAdapter<String>(requireContext(),R.layout.list_item,itemsTime);

        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxtTime.setAdapter(adapterItemsTime);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext().getApplicationContext(), "Attribute: "+item,Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTxtTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext().getApplicationContext(), "Time Frame: "+item,Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}