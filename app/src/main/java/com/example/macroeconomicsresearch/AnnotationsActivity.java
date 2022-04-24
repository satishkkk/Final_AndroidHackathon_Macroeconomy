package com.example.macroeconomicsresearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class AnnotationsActivity extends AppCompatActivity {
    private String selectedCountry;
    private int selectedUser;
    private int selectedTable;
    List<GDPUSD> GDPList = new ArrayList<>();
    List<GDPUSD> inflowList = new ArrayList<>();
    List<GDPUSD> outflowList = new ArrayList<>();
    ArrayList<String> checkedStrings;
    String startYear="1960";
    String endYear="2020";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotations);
        Button annotations_btn=findViewById(R.id.annotations_btn);
        TextView selectedCountry_tv=findViewById(R.id.country_name);
        Button apply_btn=findViewById(R.id.apply_btn);
        EditText end_year_et=findViewById(R.id.end_year_et);
        EditText start_year_et=findViewById(R.id.start_year_et);
        start_year_et.setText(startYear);
        end_year_et.setText(endYear);
        Intent intent = getIntent();
        selectedCountry=intent.getStringExtra("selectedCountry");
        selectedUser=intent.getIntExtra("selectedUser",0);
        selectedTable=intent.getIntExtra("selectedTable",0);
        checkedStrings= (ArrayList<String>) getIntent().getSerializableExtra("checkedStrings");

        if(selectedUser==0 && selectedTable==0){ // user -> resercher & table -> macroeconomic
            Log.d("Annotation","checkedStrings: " + checkedStrings);
            if(checkedStrings.contains("GDP (USD)")){
                Log.d("Annotation","GDP (USD) ");
                readCsvgdpusd(selectedCountry);
            }
            if(checkedStrings.contains("FDI Inflows(USD)")){
                Log.d("Annotation","FDI Inflows(USD): ");
                readCsvInFlow(selectedCountry);
            }
            if(checkedStrings.contains("FDI Outflows(USD)")){
                Log.d("Annotation","FDI Onflows(USD): ");
                readCsvOutFlow(selectedCountry);
            }

        }else if(selectedUser==0 && selectedTable==1) {// user -> resercher & table -> agriculture
            Log.d("Annotation","checkedStrings: " + checkedStrings);
            if(checkedStrings.contains("Fertilizers Consumption")){
                Log.d("Annotation","Fertilizer Consumption ");
                readCsvFertilizaerConsumption(selectedCountry);
            }
            if(checkedStrings.contains("Fertilizer Production")){
                Log.d("Annotation","Fertilizer Production: ");
                readCsvFertilizaerProduction(selectedCountry);
            }
        }else if(selectedUser==0 && selectedTable==2){// user -> resercher & table -> debt

        }
        // select check box gdp -> read from gdp file -> csv read code

        ImageView macroeconomics_table_image=findViewById(R.id.macroeconomics_table_image);
        ImageView agriculture_table_image=findViewById(R.id.agriculture_table_image);
        ImageView debt_table_image=findViewById(R.id.debt_table_image);
        selectedCountry_tv.setText(selectedCountry);
        if(selectedTable==0){
            annotations_btn.setBackgroundColor(getResources().getColor(R.color.Blue_color));;
            macroeconomics_table_image.setBackgroundColor(getResources().getColor(R.color.Blue_color));
        }
        else if(selectedTable==1){
            annotations_btn.setBackgroundColor(getResources().getColor(R.color.Green_color));;
            agriculture_table_image.setBackgroundColor(getResources().getColor(R.color.Green_color));
        }
        else{
            annotations_btn.setBackgroundColor(getResources().getColor(R.color.Brown_color));;
            debt_table_image.setBackgroundColor(getResources().getColor(R.color.Brown_color));
        }

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start_year_et.getText().toString().isEmpty() && !end_year_et.getText().toString().isEmpty()){
                    start_year_et.setText(startYear);
                    start_year_et.setText(endYear);
                    Log.e("StartYearEndYear",start_year_et.getText().toString()+ " "+end_year_et.getText().toString());
                }
            }
        });

        annotations_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNotesActivity();
            }
        });
    }
    private  void openNotesActivity(){
        Intent intent = new Intent(this,NotesAnnotations.class);
        intent.putExtra("selectedCountry",selectedCountry);
        intent.putExtra("selectedUser",selectedUser);
        intent.putExtra("selectedTable",selectedTable);
        intent.putExtra("checkedStrings",checkedStrings.toString());
        startActivity(intent);
        finish();
    }

    public void readCsvgdpusd(String selectedCountry) {
        InputStream is = getResources().openRawResource(R.raw.gdpusd2);
        int countryOrder = 1;
        if(selectedCountry.equals("India")){
            countryOrder=1;
        }else if(selectedCountry.equals("China")){
            countryOrder=2;
        }else{
            countryOrder=3;
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line="";
        Log.d("Annotation","Column" + countryOrder);
            try {
                while ( (line = reader.readLine()) != null){
                    String[] tokens = line.split(",");
                    GDPUSD gdpusd = new GDPUSD();
                    gdpusd.setYear(tokens[0]);
                    double val = new BigDecimal(tokens[countryOrder]).doubleValue();
                    gdpusd.setCountry(val);
                    GDPList.add(gdpusd);
                    Log.d("Annotation","Reading: " + gdpusd);
                }
            } catch (IOException e) {
                Log.wtf("Annotation","error reading csv file" + line ,e);
                e.printStackTrace();
            }

        GraphView graph = (GraphView) findViewById(R.id.gdp);
        graph.setVisibility(View.VISIBLE);
//        graph.setLayoutParams(new LinearLayout.LayoutParams(1000,500));

        List<DataPoint> list = new ArrayList<>();
        Log.d("Annotation","Year" + GDPList.get(0).getYear());

        double d = 1960;
        for(GDPUSD gdpusd : GDPList){
            list.add(new DataPoint( d++,gdpusd.getCountry()));
        }

        DataPoint[] myArray = new DataPoint[list.size()];
        list.toArray(myArray);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(myArray);

        graph.addSeries(series);

    }

    public void readCsvInFlow(String selectedCountry) {
        InputStream is = getResources().openRawResource(R.raw.inflow);
        int countryOrder = 2;
        if(selectedCountry.equals("India")){
            countryOrder=2;
        }else if(selectedCountry.equals("China")){
            countryOrder=1;
        }else{
            countryOrder=3;
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line="";
        Log.d("Annotation","Column" + countryOrder);

        try {
            while ( (line = reader.readLine()) != null ){
                String[] tokens = line.split(",");
                GDPUSD gdpusd = new GDPUSD();
                gdpusd.setYear(tokens[0]);
                double val = new BigDecimal(tokens[countryOrder]).doubleValue();
                gdpusd.setCountry(val);
                inflowList.add(gdpusd);
                Log.d("Annotation","Reading: " + gdpusd);
            }
        } catch (IOException e) {
            Log.wtf("Annotation","error reading csv file" + line ,e);
            e.printStackTrace();
        }

        GraphView graph = (GraphView) findViewById(R.id.inflow);
        graph.setVisibility(View.VISIBLE);
//        graph.setLayoutParams(new LinearLayout.LayoutParams(1000,500));
        List<DataPoint> list = new ArrayList<>();
        Log.d("Annotation","Year" + inflowList.get(0).getYear());

        double d = 1978;
        for(GDPUSD gdpusd : inflowList){
            list.add(new DataPoint( d++,gdpusd.getCountry()));
        }

        DataPoint[] myArray = new DataPoint[list.size()];
        list.toArray(myArray);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(myArray);

        graph.addSeries(series);

    }

    public void readCsvOutFlow(String selectedCountry) {
        InputStream is = getResources().openRawResource(R.raw.outflow);
        int countryOrder = 1;
        if(selectedCountry.equals("India")){
            countryOrder=2;
        }else if(selectedCountry.equals("China")){
            countryOrder=1;
        }else{
            countryOrder=3;
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line="";
        Log.d("Annotation","Column" + countryOrder);
        try {
            while ( (line = reader.readLine()) != null){
                String[] tokens = line.split(",");
                GDPUSD gdpusd = new GDPUSD();
                gdpusd.setYear(tokens[0]);
                double val = new BigDecimal(tokens[countryOrder]).doubleValue();
                gdpusd.setCountry(val);
                outflowList.add(gdpusd);
                Log.d("Annotation","Reading: " + gdpusd);
            }
        } catch (IOException e) {
            Log.wtf("Annotation","error reading csv file" + line ,e);
            e.printStackTrace();
        }

        GraphView graph = (GraphView) findViewById(R.id.outflow);
        graph.setVisibility(View.VISIBLE);
//        graph.setLayoutParams(new LinearLayout.LayoutParams(1000,500));
        List<DataPoint> list = new ArrayList<>();
        Log.d("Annotation","Year" + outflowList.get(0).getYear());

        double d = 1981;
        for(GDPUSD gdpusd : outflowList){
            list.add(new DataPoint( d++,gdpusd.getCountry()));
        }
        DataPoint[] myArray = new DataPoint[list.size()];
        list.toArray(myArray);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(myArray);
        graph.addSeries(series);
    }

    public void readCsvFertilizaerConsumption(String selectedCountry) {
        InputStream is = getResources().openRawResource(R.raw.fertilizerconsumption);
        int countryOrder = 1;
        if(selectedCountry.equals("India")){
            countryOrder=2;
        }else if(selectedCountry.equals("China")){
            countryOrder=1;
        }else{
            countryOrder=3;
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line="";
        Log.d("Annotation","Column" + countryOrder);
        try {
            while ( (line = reader.readLine()) != null){
                String[] tokens = line.split(",");
                GDPUSD gdpusd = new GDPUSD();
                gdpusd.setYear(tokens[0]);
                double val = new BigDecimal(tokens[countryOrder]).doubleValue();
                gdpusd.setCountry(val);
                outflowList.add(gdpusd);
                Log.d("Annotation","Reading: " + gdpusd);
            }
        } catch (IOException e) {
            Log.wtf("Annotation","error reading csv file" + line ,e);
            e.printStackTrace();
        }

        GraphView graph = (GraphView) findViewById(R.id.inflow);
        graph.setVisibility(View.VISIBLE);
//        graph.setLayoutParams(new LinearLayout.LayoutParams(1000,500));
        List<DataPoint> list = new ArrayList<>();
        Log.d("Annotation","Year" + outflowList.get(0).getYear());

        double d = 1961;
        for(GDPUSD gdpusd : outflowList){
            list.add(new DataPoint( d++,gdpusd.getCountry()));
        }
        DataPoint[] myArray = new DataPoint[list.size()];
        list.toArray(myArray);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(myArray);
        graph.addSeries(series);
    }

    public void readCsvFertilizaerProduction(String selectedCountry) {
        InputStream is = getResources().openRawResource(R.raw.fertilizersproduction);
        int countryOrder = 1;
        if(selectedCountry.equals("India")){
            countryOrder=2;
        }else if(selectedCountry.equals("China")){
            countryOrder=1;
        }else{
            countryOrder=3;
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line="";
        Log.d("Annotation","Column" + countryOrder);
        try {
            while ( (line = reader.readLine()) != null){
                String[] tokens = line.split(",");
                GDPUSD gdpusd = new GDPUSD();
                gdpusd.setYear(tokens[0]);
                double val = new BigDecimal(tokens[countryOrder]).doubleValue();
                gdpusd.setCountry(val);
                outflowList.add(gdpusd);
                Log.d("Annotation","Reading: " + gdpusd);
            }
        } catch (IOException e) {
            Log.wtf("Annotation","error reading csv file" + line ,e);
            e.printStackTrace();
        }

        GraphView graph = (GraphView) findViewById(R.id.outflow);
        graph.setVisibility(View.VISIBLE);
//        graph.setLayoutParams(new LinearLayout.LayoutParams(1000,500));
        List<DataPoint> list = new ArrayList<>();
        Log.d("Annotation","Year" + outflowList.get(0).getYear());

        double d = 1960;
        for(GDPUSD gdpusd : outflowList){
            list.add(new DataPoint( d++,gdpusd.getCountry()));
        }
        DataPoint[] myArray = new DataPoint[list.size()];
        list.toArray(myArray);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(myArray);
        graph.addSeries(series);
    }



}