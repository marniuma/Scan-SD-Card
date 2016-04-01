package com.marni.android.scanfiles;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScanIntentService extends IntentService {



    public static final String TOP10 = "topFileSize10";
    public static final String AVERAGE = "averageSize";
    public static final String FREQUENTEXT = "extFrequent5";
    public static final String NOTIFICATION = "com.marni.android.scanfiles";

    private final String state = Environment.getExternalStorageState();

    Map<String,Integer> files = new HashMap<String, Integer>();
    Map<String, Double> nameSize = new HashMap<String, Double>();
    Map<String, Double> sorted_nameSize = new HashMap<String, Double>( );
    Map<String, Integer> mostFrequent = new HashMap<String, Integer>( );

    private int fileCount = 0;
    private String top10;
    private String average;
    private String frequent5;

    public ScanIntentService() {
        super("ScanIntentService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

        }

        if ( Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) ) {  // we can read the External Storage...
            scanSDCard(Environment.getExternalStorageDirectory());
        }


        top10 = top10();
        average = averageFileSize();
        frequent5 = mostFrequentFileExtension();

        Intent intent1 = new Intent(NOTIFICATION);
        intent1.putExtra(TOP10,top10);
        intent1.putExtra(AVERAGE,average);
        intent1.putExtra(FREQUENTEXT,frequent5);
        sendBroadcast(intent1);
    }




    public void scanSDCard(File dir) {
        //String pdfPattern = ".pdf";


        String extension = "";

        File[] listFile = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    scanSDCard(listFile[i]);
                } else {

                    fileCount++;
                    int in = listFile[i].getName().lastIndexOf('.');
                    String name = listFile[i].getName();
                    double size = listFile[i].length()/(1024);
                    nameSize.put(name,size);
                    if (in >= 0 && name.length() > in+1) {
                        try {
                            extension = name.substring(i + 1); //get extension type
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println(name);
                        }

                        if(!files.containsKey(extension)) {
                            files.put(extension,1);
                        } else {
                            Integer te = files.get(extension);
                            files.put(extension, te+1);
                        }

                    }
                }
            }
        }
    }

    public String top10() {

        String temp = "";
        sorted_nameSize = sort((HashMap<String, Double>) nameSize);
        int size = sorted_nameSize.size();
        System.out.println("---------------------------------------"+size);
        int ten = 0;
        Iterator it = sorted_nameSize.entrySet().iterator();
        while (it.hasNext()  && ten<=10) {
            System.out.println("----------**************------------------"+ten);
            ten++;
            System.out.println("****************************************"+ten);

                System.out.println("--------------^^^^^^^^^^^^^------------"+size);
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"+ten);
                Map.Entry pair = (Map.Entry) it.next();
                temp = temp.concat(pair.getKey() + " - " + pair.getValue() + " koilobytes\n");
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove();

        }

        return temp;

    }


    public String averageFileSize() {
        String temp1 = "";
        double average = 0;
        Iterator it = nameSize.entrySet().iterator();
        for (int i = 1; it.hasNext(); i++) {
            Map.Entry pair = (Map.Entry)it.next();
            average = (i-1)*average/i + ((double) pair.getValue()/i);
            it.remove(); // avoids a ConcurrentModificationException
        }
        temp1 = String.valueOf(average) + " bytes";
        return temp1;
    }

    public String mostFrequentFileExtension() {

        String temp3 = "";
        mostFrequent = sort1((HashMap<String, Integer>) files);
        int size = mostFrequent.size();
        int five = 1;
        Iterator it = mostFrequent.entrySet().iterator();
        while (it.hasNext() && five <= 5) {

            five++;
                Map.Entry pair = (Map.Entry) it.next();
                temp3 = temp3.concat("Extension (" + pair.getKey() + ") " + " ----- " +"Repeated " + pair.getValue() + " times\n");
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove();

            // avoids a ConcurrentModificationException
        }

        return temp3;
    }


    public HashMap<String, Double> sort(HashMap<String, Double> hmap) {

        Set<Map.Entry<String, Double>> entries = hmap.entrySet();
        Comparator<Map.Entry<String, Double>> valueComparator = new Comparator<Map.Entry<String,Double>>() {

            @Override
            public int compare(Map.Entry<String, Double> e1, Map.Entry<String, Double> e2) {
                Double v1 = e1.getValue();
                Double v2 = e2.getValue();
                return v2.compareTo(v1);
            }
        };

        // Sort method needs a List, so let's first convert Set to List in Java
        List<Map.Entry<String, Double>> listOfEntries = new ArrayList<Map.Entry<String, Double>>(entries);

        // sorting HashMap by values using comparator
        Collections.sort(listOfEntries, valueComparator);

        LinkedHashMap<String, Double> sortedByValue = new LinkedHashMap<String, Double>(listOfEntries.size());

        // copying entries from List to Map
        for(Map.Entry<String, Double> entry : listOfEntries){
            sortedByValue.put(entry.getKey(), entry.getValue());
        }

        System.out.println("HashMap after sorting entries by values ");
        Set<Map.Entry<String, Double>> entrySetSortedByValue = sortedByValue.entrySet();

        for(Map.Entry<String, Double> mapping : entrySetSortedByValue){
            System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
        }

        return sortedByValue;
    }



    public HashMap<String, Integer> sort1(HashMap<String, Integer> hmap) {

        Set<Map.Entry<String, Integer>> entries = hmap.entrySet();
        Comparator<Map.Entry<String, Integer>> valueComparator = new Comparator<Map.Entry<String,Integer>>() {

            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                Integer v1 = e1.getValue();
                Integer v2 = e2.getValue();
                return v2.compareTo(v1);
            }
        };

        // Sort method needs a List, so let's first convert Set to List in Java
        List<Map.Entry<String, Integer>> listOfEntries = new ArrayList<Map.Entry<String, Integer>>(entries);

        // sorting HashMap by values using comparator
        Collections.sort(listOfEntries, valueComparator);

        LinkedHashMap<String, Integer> sortedByValue = new LinkedHashMap<String, Integer>(listOfEntries.size());

        // copying entries from List to Map
        for(Map.Entry<String, Integer> entry : listOfEntries){
            sortedByValue.put(entry.getKey(), entry.getValue());
        }

        System.out.println("HashMap after sorting entries by values ");
        Set<Map.Entry<String, Integer>> entrySetSortedByValue = sortedByValue.entrySet();

        for(Map.Entry<String, Integer> mapping : entrySetSortedByValue){
            System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
        }
        return sortedByValue;
    }
}
