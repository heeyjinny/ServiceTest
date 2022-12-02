package com.heeyjinny.servicetest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast

//1
//서비스 클래스 생성 : 스타티드, 바운드서비스 만들기
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

    //5
    //바운드서비스와 연결할 수 있는
    //서비스 커넥션 onServiceConnection{}을 생성하고
    //멤버들 임플리먼트 하여 메서드 2개 오버라이드
    //onServiceConnected(), onServiceDisconnected()

    //생성한 서비스 커넥션을 bindService()메서드를 통해
    //시스템 전달 시 서비스와 연결하고
    //unbindService()메서드로 서비스 연결을 끊을 수 있음

    //5-1
    //MyService를 상속받은 변수 생성
    var myService:MyService? = null

    //5-2
    //onServiceConnected(): 서비스 연결되면 자동 호출
    //onServiceDisconnected(): 비정상적으로 서비스가 종료되었을 때만 호출
    //서비스가 정상적으로 연결해제 되면 호출되지 않음...
    //그래서 현재 서비스가 연결되어 있는지를 확인하는 변수 isService생성
    var isService = false

    //5-3
    //변수 connection을 생성하여 서비스 커넥션 ServiceConnection 생성
    //멤버들 임플리먼트 하여 메서드 2개 오버라이드
    //onServiceConnected(), onServiceDisconnected()
    val connection = object: ServiceConnection{

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

            //5-5
            //p1의 형변환 MyService.MyBinder로...
            val binder = p1 as MyService.MyBinder
            myService = binder.getService()
            //5-6
            //현재 서비스가 연결되었으므로 true로 변경
            isService = true
            //5-7
            //현재 서비스가 연결되었는지 확인
            Log.d("BoundService", "연결되었습니다.")

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            //5-8
            //현재 서비스가 연결해제 되었으므로 false로 변경
            isService = false
        }

    }//val connection

    //6
    //바운드 서비스 호출 함수 생성
    //레이아웃에 직접 접근하여(view: View)
    //호출 메서드 bindService() 실행
    //버튼 클릭 시 서비스 호출하면서 커넥션도 같이 넘기는 코드 작성
    fun serviceBind(view: View){

        //6-1
        //인텐트 생성
        val intent = Intent(this, MyService::class.java)
        //6-2
        //바운드서비스 호출 메서드 bindService()호출
        //파라미터네 인텐트, 커넥션 넘겨주고
        //세 번째 파라미터: 서비스가 생성되어 있지 않으면 생성 후 바인딩
        //이미 생성되어 있으면 바로 바인딩 설정(Context.BIND_AUTO_CREATE)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

    }

    //7
    //바운드 서비스 연결해제 함수 생성
    //연결해제 메서드 unbindService()실행
    fun  serviceUnbind(view: View){
        //7-1
        //현재 서비스가 실행중인 것 먼저 체크
        if (isService){

            //7-2
            //실행중이라면 연결해제 메서드 unbindService()실행하여 커넥션 해제
            unbindService(connection)
            //7-3
            //연결해제 된 것을 알리기 위해 false
            isService = false

        }
    }

    //8
    //activity_main.xml에서 바운드서비스 버튼 추가출

    //9
    //바운드 서비스의 메서드 직접 호출을 위해
    //메서드 생성
    fun callServiceFunction(view: View){

        //9-1
        //만약 현재 서비스가 연결중이라면
        //바운드 서비스의 메서드 serviceMessage()에서 반환된 문자열 출력
        if (isService){
            val message = myService?.serviceMessage()
            Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "바운드 서비스가 연결되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }

    }

    //10
    //서비스함수 callServiceFunction 호출 버튼 추가
    //activity_main.xml

}//MainActivity