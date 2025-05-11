package com.example.android.ui.menusearch;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.data.MatchesCoursesFake;
import com.example.android.ui.model.MatchCourse;

import java.util.List;

/**
 * Created on October.
 * year 2021 .
 */
class MyQrListRepository {

    private static final String TAG = "MyQrListRepository";
    private final MediatorLiveData<MatchCourse> getInfo = new MediatorLiveData<>();


    MutableLiveData<Boolean> isLoadingGetList = new MutableLiveData<>();

    MutableLiveData<Boolean> isLoadingRepo = new MutableLiveData<>();

    MutableLiveData<List<MatchCourse>> liveData;

    public void getData(MutableLiveData<List<MatchCourse>> liveData) {
        isLoadingGetList.setValue(true);
        try {
            isLoadingGetList.setValue(false);
            List<MatchCourse> myQrItem = MatchesCoursesFake.getInstance().matchedCourses();
            liveData.postValue( myQrItem);
        }catch (Exception ex){
            isLoadingGetList.setValue(false);
        }


    }



}
