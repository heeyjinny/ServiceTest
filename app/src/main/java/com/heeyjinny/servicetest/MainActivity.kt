package com.heeyjinny.servicetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

//1
//스타티드 서비스 만들기
//app - java디렉터리 밑 패키지명 우클릭 - New - Service - Service
//MyService - Finish

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }//onCreate

    //2
    //서비스 호출하는 함수 serviceStart()생성
    //새 메서드 생성 시 파라미터에 (view: View)사용하면
    //클릭 리스너 연결 없이도 레이아웃 파일에서 메서드에 직접 접근 가능함
    fun serviceStart(view: View){

        //2-1
        //안드로이드에 전달할 인텐트를 만들고
        //MyService에 임의로 정의해둔 명령을 action에 담아서 같이 전달

        //액티비티에서 동일한 인텐트를 하나 더 생성하고 startServic()를 해도
        //서비스는 더 이상 생성되지 않고 onStartCommand()만 호출됨
        //일방적인 명령어 전달 구조에 사용 용이함
        //만약 새 서비스를 생성하려면 인텐트의 변수명을 다르게 해야함
        val intent = Intent(this, MyService::class.java)
        intent.action = MyService.ACTION_START

        //2-2
        //startService() 메서드로 스타티드 서비스 호출
        startService(intent)

    }

    //3
    //서비스 중단을 위한 메서드 생성
    fun serviceStop(view: View){

        //3-1
        //서비스 중단을 위해 stopService()로 인텐트 전달
        val intent = Intent(this, MyService::class.java)
        stopService(intent)

    }

    //4
    //서비스 중지상태 확인을 위해 MyService.kt에 코드작성

}//MainActivity달