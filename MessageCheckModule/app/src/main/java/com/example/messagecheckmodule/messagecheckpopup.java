package com.example.messagecheckmodule;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.SEND_SMS;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class messagecheckpopup extends Activity {
    Button sendTextBtn1;
    Button sendTextBtn2;
    Button sendTextBtn3;
    Button sendTextBtn4;
    Button sendTextBtn5;

    int PERMISSION_REQUEST_CODE = 201;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//타이틀 바 제거
        setContentView(R.layout.messagecheckpopup);

        if(!checkPermission()) {
            Log.v("test", "no per");
            requestPermission();
        }
        else
        {
            Log.v("test", "yes per");
        }
        Intent sending = backgroundMain();

        sendTextBtn1 = (Button)findViewById(R.id.btn_msg_to_1);
        sendTextBtn2 = (Button)findViewById(R.id.btn_msg_to_2);
        sendTextBtn3 = (Button)findViewById(R.id.btn_msg_to_3);
        sendTextBtn4 = (Button)findViewById(R.id.btn_msg_to_4);
        sendTextBtn5 = (Button)findViewById(R.id.btn_msg_to_5);

        sendTextBtn1.setOnClickListener(view -> mOnPopupClick(1,sending));
        sendTextBtn2.setOnClickListener(view -> mOnPopupClick(2,sending));
        sendTextBtn3.setOnClickListener(view -> mOnPopupClick(3,sending));
        sendTextBtn4.setOnClickListener(view -> mOnPopupClick(4,sending));
        sendTextBtn5.setOnClickListener(view -> mOnPopupClick(5,sending));

    }

    private boolean checkPermission() {
        Log.v("test","checkPermission()");
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {
        Log.v("test","requestPermission");
        ActivityCompat.requestPermissions(this, new String[]{READ_SMS, SEND_SMS}, PERMISSION_REQUEST_CODE);
    }

    private Intent backgroundMain() {
        Log.v("test", "backgroundMain()");
        messageScrapper mS = new messageScrapper();
        Uri allMessage = Uri.parse("content://sms");

        Cursor managingCursor = getContentResolver().query(allMessage, new String[] {" _id", "address", "person" ,"date", "body"}, null, null, "date DESC");
        Intent sendMessage = new Intent(this, messageSendPopup.class);
        int count=1;
        while(managingCursor.moveToNext())
        {
            String instantPhNum = mS.defineAndDistribute(count, managingCursor);
            String currentMessage = "Message"+count;
            sendMessage.putExtra(currentMessage, instantPhNum);
            if (count==5)
                break;
            count = count+1;
        }
        return sendMessage;
    }

    public void mOnPopupClick(int count, Intent sending) {// 누르면 팝업 하도록 하는 함수. messagecheckpopup.xml의 버튼에서 직접 선언

        Log.v("test", "mOnPopupClick()");
        String buttonNo = Integer.toString(count);
        String buttonName = "from_btn";
        sending.putExtra(buttonName, buttonNo);


        startActivity(sending);//팝업 생성

    }

    //확인 버튼. 팝업 제거.
    public void mOnClosed(View v) {
        Log.v("test", "mOnClose()");
        moveTaskToBack(true);
        finishAndRemoveTask();
    }
}

class DateModule {
    String DateSimplifier(Date callDate) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd E", Locale.KOREA);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm a", Locale.KOREA);
        String simpleDate = ""+dateFormat1.format(callDate)+"요일  "+dateFormat2.format(callDate);
        return simpleDate;
    }
}

class messageScrapper extends Activity {

    TextView message;
    TextView msgbody;

    String defineAndDistribute(int count, Cursor cursor) {

        Log.v("test", "DAD");
        Log.v("test", Integer.toString(count));


        switch (count)
        {
            case 1:
                Log.v("test", "case 1");
                message = (TextView)findViewById(R.id.message_1);
                Log.v("test", "part 1 txt");
                String MessageInfo1 = getMessageLog(cursor);
                message.setText(MessageInfo1);
                String MessagePhNum1 = getmsNumber(cursor);

                msgbody=(TextView)findViewById(R.id.msgbody_1);
                String MessageBody1 = getMessageBody(cursor);
                msgbody.setText(MessageBody1);

                return MessagePhNum1;
            case 2:
                message = (TextView)findViewById(R.id.message_2);
                String MessageInfo2 = getMessageLog(cursor);
                message.setText(MessageInfo2);
                String MessagePhNum2 = getmsNumber(cursor);

                msgbody=(TextView)findViewById(R.id.msgbody_2);
                String MessageBody2 = getMessageBody(cursor);
                msgbody.setText(MessageBody2);

                return MessagePhNum2;
            case 3:
                message = (TextView)findViewById(R.id.message_3);
                String MessageInfo3 = getMessageLog(cursor);
                message.setText(MessageInfo3);
                String MessagePhNum3 = getmsNumber(cursor);

                msgbody=(TextView)findViewById(R.id.msgbody_3);
                String MessageBody3 = getMessageBody(cursor);
                msgbody.setText(MessageBody3);

                return MessagePhNum3;
            case 4:
                message = (TextView)findViewById(R.id.message_4);
                String MessageInfo4 = getMessageLog(cursor);
                message.setText(MessageInfo4);
                String MessagePhNum4 = getmsNumber(cursor);

                msgbody=(TextView)findViewById(R.id.msgbody_4);
                String MessageBody4 = getMessageBody(cursor);
                msgbody.setText(MessageBody4);

                return MessagePhNum4;
            case 5:
                message = (TextView)findViewById(R.id.message_5);
                String MessageInfo5 = getMessageLog(cursor);
                message.setText(MessageInfo5);
                String MessagePhNum5 = getmsNumber(cursor);

                msgbody=(TextView)findViewById(R.id.msgbody_5);
                String MessageBody5 = getMessageBody(cursor);
                msgbody.setText(MessageBody5);

                return MessagePhNum5;
            default:
                return "error";
        }
    }

    private String getMessageLog(Cursor managedCursor) {//가장 최근의 부재중 전화 정보를 전화번호, 날짜 등을 포함한 String 형태로 받아 오는 함수
        Log.v("test", "getCallLog()");

        StringBuilder sb = new StringBuilder();
        DateModule DM = new DateModule();
        String msSender = managedCursor.getString(2);
        long msDate = managedCursor.getLong(3);
        Date msGotDate = new Date(Long.valueOf(msDate));

        sb.append("송신자: ").append(msSender).append("\n날짜: ").append(DM.DateSimplifier(msGotDate));

        return sb.toString();
    }

    private String getmsNumber(Cursor managedCursor) {//가장 최근의 부재중 전화의 전화번호를 받아와 전화를 걸 수 있는 형식의 String으로 돌려 받는 함수
        Log.v("test", "getmsNumber()");

        StringBuilder sb = new StringBuilder();

        String msPhNum = managedCursor.getString(1);
        sb.append(msPhNum);

        return sb.toString();
    }

    private String getMessageBody(Cursor managedCursor) {//가장 최근의 부재중 전화 정보를 전화번호, 날짜 등을 포함한 String 형태로 받아 오는 함수
        Log.v("test", "getCallLog()");

        StringBuilder sb = new StringBuilder();

        String msBody = managedCursor.getString(4);

        sb.append(msBody);

        return sb.toString();
    }
}