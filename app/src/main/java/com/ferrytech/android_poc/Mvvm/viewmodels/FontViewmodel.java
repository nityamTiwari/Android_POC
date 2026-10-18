package com.ferrytech.android_poc.Mvvm.viewmodels;

import android.graphics.Typeface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.model.FontModel;
public class FontViewmodel extends ViewModel {

    private MutableLiveData<FontModel> _fontLiveData = new MutableLiveData<>();
    public  LiveData<FontModel>  getFont(){
        return  _fontLiveData;
    }
    public void onFontSelected(int viewId){
        FontModel state =  null;

        if (viewId == R.id.btn_font1) {
            state = new FontModel(R.font.font1, Typeface.BOLD);
        } else if (viewId == R.id.btn_font2) {
            state = new FontModel(R.font.font2, Typeface.ITALIC);
        } else if (viewId == R.id.btn_font3) {
            state = new FontModel(R.font.font3, Typeface.BOLD_ITALIC);
        } else if (viewId == R.id.btn_font4) {
            state = new FontModel(R.font.font4, Typeface.NORMAL);
        } else if (viewId == R.id.btn_font5) {
            state = new FontModel(R.font.font5, Typeface.NORMAL);
        } else if (viewId == R.id.btn_font6) {
            state = new FontModel(R.font.font6, Typeface.ITALIC);
        }

        _fontLiveData.setValue(state);

    }
}
