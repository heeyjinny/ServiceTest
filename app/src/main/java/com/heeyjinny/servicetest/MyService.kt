package com.heeyjinny.servicetest

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

//1
//새로운 서비스 생성 시 AndroidManifest.xml에
//<service/>태그가 자동 등록됨

class MyService : Service() {

    //스타티드 서비스에서 사용하지 않는 메서드
    //8
    //바운드서비스 사용 시 binder변수를 반환하도록 설정
    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    //9
    //MainActivity.kt에서 바운드서비스와 연결할 수 있는
    //서비스 커넥션 생성

    //2
    //서비스 사용을 위해
    //onStartCommand() 메서드 오버라이드
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //2-1
        //호출할 때 onStartCommand로 명령어를 전달할 수 있는 코드 작성
        val action = intent?.action
        Log.d("StartedService", "action = $action")

        return super.onStartCommand(intent, flags, startId)
    }

    //3
    //companion object를 사용해 테스트로 사용할 명령어 임의 생성
    //컴패니언 오브젝트의 블록 안에 변수와 함수를 정의할 시 생성자를 통하지 않고 클래스 멤버들 사용가능함
    //명령어는 일반적으로 "패키지명 + 명령어" 조합으로 만들어짐
    //액티비티에서 서비스를 호출할 때 사용
    companion object{
        val ACTION_START = "com.heeyjinny.servicetest.START"
        val ACTION_RUN = "com.heeyjinny.servicetest.RUN"
        val ACTION_STOP = "com.heeyjinny.servicetest.STOP"
    }

    //4
    //MainActivity.kt에서 서비스 호출코드 작성

    //5
    //서비스 중지상태 확인을 위한 코드작성
    //서비스 종료 시 호출되는 onDestroy() 오버라이드
    override fun onDestroy() {

        //5-1
        //서비스 종료 시 메시지 출력
        Log.d("Service", "서비스가 종료되었습니다.")

        super.onDestroy()
    }

    //6
    //버튼을 누르면 로그캣 창에 원하는 로그가 뜨도록
    //activity_main.xml에 버튼 추가

    //7
    //바운드 서비스 만들기
    //이너클래스로 바인더 클래스 생성
    inner class MyBinder: Binder(){
        //7-1
        //getService()메서드 생성
        //액티비티와 서비스가 연결되면
        //바인더의 getService()메서드를 통해 서비스 접근 가능함
        fun getService(): MyService{
            return this@MyService
        }
    }

    //7-2
    //변수를 만들어 바인더 이너클래스 생성
    val binder = MyBinder()

    //8
    //바운드 서비스는 액티비티에서 서비스의 메서드를 직접 호출 가능
    //문자열 하나를 반환하는 메서드 추가
    fun serviceMessage(): String{
        return "Hello Activity, I am BoundService!"
    }

    //8-1
    //MainActivity.kt에서 문자열반환 메서드 호출

}//Myservice