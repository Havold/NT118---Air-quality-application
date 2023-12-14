package com.example.natural;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class FragmentGraph extends Fragment {

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    AutoCompleteTextView selectEndingTxt;
    String[] items = {"Temperature","Humidity","Amount of rain"};
    String[] itemsTime = {"Day","Month","Year"};
    AutoCompleteTextView autoCompleteTxt;
    AutoCompleteTextView autoCompleteTxtTime;

    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItemsTime;
    Button tmp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        autoCompleteTxt = view.findViewById(R.id.auto_complete_txt);
        autoCompleteTxtTime = view.findViewById(R.id.auto_complete_txt_TimeFrame);
        selectEndingTxt = view.findViewById(R.id.selectEndingTxt);

        adapterItems = new ArrayAdapter<String>(requireContext(), R.layout.list_item,items);
        adapterItemsTime = new ArrayAdapter<String>(requireContext(), R.layout.list_item,itemsTime);



        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxtTime.setAdapter(adapterItemsTime);

        // Khởi tạo DatePickerDialog khi nhấp vào selectEndingTxt
        selectEndingTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Khởi tạo DatePickerDialog.OnDateSetListener
// Khởi tạo DatePickerDialog
        mDateSetListener = (view1, year, month, dayOfMonth) -> {
            // Cập nhật ngày vào AutoCompleteTextView
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            selectEndingTxt.setText(selectedDate);
        };
        
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

    private void showDatePickerDialog() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);

        // Đặt màu nền của DatePickerDialog
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

}