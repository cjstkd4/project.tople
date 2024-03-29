package com.example.meetingactivity.Activity;

import android.app.Application;

import com.example.meetingactivity.adapter.KakaoSDKAdapter;
import com.kakao.auth.KakaoSDK;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;
    public static GlobalApplication getGlobalApplicationContext(){
        if (instance==null){
            throw  new IllegalStateException("이 어플리케이션은 com.kakao.GlobalApplication을 상속받지 않았습니다.");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance=null;
    }
}