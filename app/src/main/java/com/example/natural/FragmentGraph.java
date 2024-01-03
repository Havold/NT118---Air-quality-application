package com.example.natural;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.natural.SQLite.DatabaseHelper;
import com.example.natural.model.DateUtils;
import com.example.natural.model.SharedViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentGraph extends Fragment {
    SharedViewModel sharedViewModel;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    FrameLayout frameLayout;
    private long dateStart,dateEnd;
    boolean flag=false;
    String attribute,timeFrame;
    int dayStart,monthStart,yearStart,dayEnd,monthEnd,yearEnd;
    AutoCompleteTextView selectStartingTxt,selectEndingTxt;

    String[] itemsTime = {"Day"};
    AutoCompleteTextView autoCompleteTxt;
    AutoCompleteTextView autoCompleteTxtTime;
    String[] items = {"Temperature","Humidity","Wind speed","Rainfall"};

    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItemsTime;
    Button showChart;
    boolean flagStart=false,flagEnd=false;
    private Animation.AnimationListener animationListener;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        //         Khởi tạo ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        frameLayout = view.findViewById(R.id.frameLayout);
        autoCompleteTxt = view.findViewById(R.id.auto_complete_txt);
        autoCompleteTxtTime = view.findViewById(R.id.auto_complete_txt_TimeFrame);
        selectStartingTxt = view.findViewById(R.id.selectStartingTxt);
        selectEndingTxt = view.findViewById(R.id.selectEndingTxt);
        showChart = view.findViewById(R.id.showChartBtn);

        //Animation
        handleButtonAnimationXml(showChart,R.anim.anim_zoom_in);


        adapterItems = new ArrayAdapter<String>(requireContext(), R.layout.list_item,items);
        adapterItemsTime = new ArrayAdapter<String>(requireContext(), R.layout.list_item,itemsTime);

        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxtTime.setAdapter(adapterItemsTime);

//        attribute=sharedViewModel.getAttribute();
        timeFrame= sharedViewModel.getTimeFrame();
//        autoCompleteTxt.setText(sharedViewModel.getAttribute());

        autoCompleteTxtTime.setText(sharedViewModel.getTimeFrame());

        // Khởi tạo DatePickerDialog khi nhấp vào selectStartTxt
        selectStartingTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=false;
                showDatePickerDialog();
            }
        });

        selectEndingTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                showDatePickerDialog();
            }
        });

        // Khởi tạo DatePickerDialog.OnDateSetListener
        // Khởi tạo DatePickerDialog
        mDateSetListener = (view1, year, month, dayOfMonth) -> {
            // Cập nhật ngày vào AutoCompleteTextView
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            // Tạo một đối tượng Calendar và đặt ngày, tháng, năm từ selectedDate
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);

            // Lấy timestamp từ Calendar
            long timestamp = calendar.getTimeInMillis();
            if (flag==false) {
                dateStart = timestamp;
                yearStart = year;
                monthStart = month;
                dayStart = dayOfMonth;
                flagStart=true;
                selectStartingTxt.setText(selectedDate);
            }
            else {
                dateEnd = timestamp;
                yearEnd = year;
                monthEnd = month;
                dayEnd = dayOfMonth;
                flagEnd=true;
                selectEndingTxt.setText(selectedDate);
            }
        };
        
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                attribute = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext().getApplicationContext(), "Attribute: "+item,Toast.LENGTH_SHORT).show();
                sharedViewModel.setAttribute(attribute);
            }
        });

        autoCompleteTxtTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                timeFrame = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext().getApplicationContext(), "Time Frame: "+item,Toast.LENGTH_SHORT).show();
                sharedViewModel.setTimeFrame(timeFrame);
            }
        });

        showChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa tất cả các views con khỏi frameLayout (trong trường hợp đã có chart hiện tại)
                frameLayout.removeAllViews();

                //        Khai báo BarPlot
                BarChart mBarChart = new BarChart(requireContext());
                mBarChart.setLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));
                frameLayout.addView(mBarChart);


                DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
                Cursor cursor = dbHelper.getAllWeatherData();
                if (cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") double temperature = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEMPERATURE));
                        @SuppressLint("Range") double wind = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_WIND));
                        @SuppressLint("Range") double humidity = cursor.getFloat((cursor.getColumnIndex(DatabaseHelper.COLUMN_HUMIDITY)));
                        @SuppressLint("Range") double rainfall = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_RAINFALL));
                        @SuppressLint("Range") long time = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME));
//                        int day = DateUtils.getDayFromTimestamp(time);
//                        int month = DateUtils.getMonthFromTimestamp(time);
//                        int year = DateUtils.getYearFromTimestamp(time);

                        // Add data to the BarChart
                        if (attribute=="Temperature") {
                            if (timeFrame=="Day") {
                                if (time>=dateStart && time<=dateEnd) {
                                    mBarChart.addBar(new BarModel(DateUtils.convertTimestampToDateString(time), Float.parseFloat(decimalFormat.format(temperature)) , 0xFFFFA500));
                                    mBarChart.setShowDecimal(true);

                                }
                            }
//                            else if (timeFrame=="Month") {
//                                if (month>=monthStart && day<=monthEnd) {
//                                    mBarChart.addBar(new BarModel(String.valueOf(month), temperature, 0xFF123456));
//                                }
//                            }
//                            else if (timeFrame=="Year") {
//                                if (year>=yearStart && day<=yearEnd) {
//                                    mBarChart.addBar(new BarModel(String.valueOf(month), temperature, 0xFF123456));
//                                }
//                            }
                        }

                        else if (attribute=="Humidity") {
                            if (timeFrame=="Day") {
                                if (time>=dateStart && time<=dateEnd) {
                                    mBarChart.addBar(new BarModel(DateUtils.convertTimestampToDateString(time), Float.parseFloat(decimalFormat.format(humidity)), 0xFFADD8E6));
                                    mBarChart.setShowDecimal(true);
                                }
                            }
//                            else if (timeFrame=="Month") {
//                                if (month>=monthStart && day<=monthEnd) {
//                                    mBarChart.addBar(new BarModel(String.valueOf(month), humidity, 0xFF123456));
//                                }
//                            }
//                            else if (timeFrame=="Year") {
//                                if (year>=yearStart && day<=yearEnd) {
//                                    mBarChart.addBar(new BarModel(String.valueOf(month), humidity, 0xFF123456));
//                                }
//                            }
                        }
                        else if (attribute=="Wind speed") {
                            if (timeFrame=="Day") {
                                if (time>=dateStart && time<=dateEnd) {
                                    mBarChart.addBar(new BarModel(DateUtils.convertTimestampToDateString(time), Float.parseFloat(decimalFormat.format(wind)), 0xFFFFFFFF));
                                    mBarChart.setShowDecimal(true);
                                }
                            }
//                            else if (timeFrame=="Month") {
//                                if (month>=monthStart && day<=monthEnd) {
//                                    mBarChart.addBar(new BarModel(String.valueOf(month), wind, 0xFF123456));
//                                }
//                            }
//                            else if (timeFrame=="Year") {
//                                if (year>=yearStart && day<=yearEnd) {
//                                    mBarChart.addBar(new BarModel(String.valueOf(month), wind, 0xFF123456));
//                                }
//                            }
                        }
                        else if (attribute=="Rainfall") {
                            if (timeFrame=="Day") {
                                if (time>=dateStart && time<=dateEnd) {
                                    mBarChart.addBar(new BarModel(DateUtils.convertTimestampToDateString(time), Float.parseFloat(decimalFormat.format(rainfall)), 0xFF123456));
                                    mBarChart.setShowDecimal(true);
                                }
                            }
//                            else if (timeFrame=="Month") {
//                                if (month>=monthStart && day<=monthEnd) {
//                                    mBarChart.addBar(new BarModel(String.valueOf(month), rainfall, 0xFF123456));
//                                }
//                            }
//                            else if (timeFrame=="Year") {
//                                if (year>=yearStart && day<=yearEnd) {
//                                    mBarChart.addBar(new BarModel(String.valueOf(month), rainfall, 0xFF123456));
//                                }
//                            }
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
                dbHelper.close();
            }
        });
        return view;
    }

    private void showDatePickerDialog() {
        int year,month,day;
        // Lấy ngày hiện tại
        if (flag==false && flagStart==true) {
            year = yearStart;
            month = monthStart;
            day = dayStart;
        }
        else if (flag==true && flagEnd==true) {
            year = yearEnd;
            month = monthEnd;
            day = dayEnd;
        } else {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

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

    private void initVariables() {
        animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };
    }

    private void handleButtonAnimationXml(Button btn, int animId)
    {
        // HandleClickAnimationXML
        // Load the Animation
        final Animation animation = AnimationUtils.loadAnimation(requireContext(),animId);

        // set animation listener
        animation.setAnimationListener(animationListener);

        btn.startAnimation(animation);
    }

    private void handleImageAnimationXml(ImageView img, int animId)
    {
        // HandleClickAnimationXML
        // Load the Animation
        final Animation animation = AnimationUtils.loadAnimation(requireContext(),animId);

        // set animation listener
        animation.setAnimationListener(animationListener);

        img.startAnimation(animation);
    }

    private void handleTextAnimationXml(TextView txt, int animId)
    {
        // HandleClickAnimationXML
        // Load the Animation
        final Animation animation = AnimationUtils.loadAnimation(requireContext(),animId);

        // set animation listener
        animation.setAnimationListener(animationListener);

        txt.startAnimation(animation);
    }
}