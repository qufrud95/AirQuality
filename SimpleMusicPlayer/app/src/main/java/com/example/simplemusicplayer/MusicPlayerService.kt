package com.example.simplemusicplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast

class MusicPlayerService : Service(){
    var mMediaPlayer : MediaPlayer? = null

    var mBinder : MusicPlayerBinder = MusicPlayerBinder()

    inner class MusicPlayerBinder : Binder(){
        fun getService():MusicPlayerService{
            return this@MusicPlayerService //현재서비스를 반환
        }
        /*
        *  binder 클래스를 상속받아 binder class 가 inbider interface를 구현해줌
        * */
    }
    override fun onCreate() {

        super.onCreate() // 처음에 딱 한번만 실행됨
        startForegroundService() // 앱이 실행되고 있다는 알림을 생성
    }

    override fun onBind(Intent: Intent?): IBinder? {
       return mBinder
    } //bindService()함수 호출시 실행 -> 서비스와 구성요소를 이어주는 IBinder반환

    override fun onStartCommand(intent: Intent?, flags:Int, startId:Int):Int{
        return START_STICKY // 시스템이 서비스를 중단하면 서비스를 다시 실행하고 onstartCommand 호출
    }

    fun startForegroundService() { // 알림 채널 생성

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val mChannel = NotificationChannel(
                "CHANNEL_ID",
                "CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(mChannel)
        }
        //알림생성
        val notification:Notification = Notification.Builder(this,"CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_play)
            .setContentTitle("뮤직 플레이어 앱")
            .setContentText("앱이 실행 중입니다.")
            .build()
        startForeground(1,notification)
    }
    override fun onDestroy() {
        super.onDestroy()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            stopForeground(true)
        }
    }
    //재생확인
    fun isPlaying() :Boolean{
        return (mMediaPlayer != null && mMediaPlayer?.isPlaying ?: false)
    }
    fun play(){
        if(mMediaPlayer == null){
            mMediaPlayer = MediaPlayer.create(this,R.raw.chocolate)
            mMediaPlayer?.setVolume(1.0f,1.0f)
            mMediaPlayer?.isLooping = true
            mMediaPlayer?.start()
        }else{

            if(mMediaPlayer!!.isPlaying){
                Toast.makeText(this,"이미 실행중입니다.",Toast.LENGTH_SHORT).show()
            }else{
                mMediaPlayer?.start()
            }
        }
    }

    fun pause(){
        mMediaPlayer?.let {
            if(it.isPlaying){
                it.pause()
            }
        }
    }
    fun stop(){
        mMediaPlayer?.let {
            if(it.isPlaying){
                it.stop()
                it.release() // 자원 할당 해제
                mMediaPlayer=null
            }
        }
    }
}