package com.mendhak.gpsvisualizer.views;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mendhak.gpsvisualizer.R;
import com.mendhak.gpsvisualizer.common.GpsTrack;
import com.mendhak.gpsvisualizer.common.IDataImportListener;
import com.mendhak.gpsvisualizer.common.ProcessedData;
import com.mendhak.gpsvisualizer.parsers.Gpx10Parser;

import java.io.File;


public  class MainImportFragment extends Fragment implements View.OnClickListener, IDataImportListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final Integer ACTION_FILE_PICKER=41792;


    public static MainImportFragment newInstance(int sectionNumber) {
        MainImportFragment fragment = new MainImportFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    public MainImportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button btnImport = (Button)rootView.findViewById(R.id.btnImportData);
        btnImport.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnImportData:
                openFolder();
                //ProcessUserGpsFile();
                //importListener.OnDataImported(flatTrack);
                break;
        }
    }

    public void openFolder()
    {
        Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
        mediaIntent.setType("file/*"); //set mime type as per requirement
        startActivityForResult(mediaIntent, ACTION_FILE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_FILE_PICKER
                && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.d("GPSVisualizer", "File URI= " + uri);
            ProcessUserGpsFile(uri);
        }
    }

    private void ProcessUserGpsFile(Uri uri) {

        File gpsFile = new File(uri.getPath());

        Gpx10Parser parser = new Gpx10Parser();
        parser.Parse(gpsFile.getPath(), this);

    }


    /**
     * Replace the current application-wide track
     * @param track
     */
    @Override
    public void OnDataImported(GpsTrack track) {
        Log.i("GPSLogger", "Data imported");
        ProcessedData.SetTrack(track);
    }


}
