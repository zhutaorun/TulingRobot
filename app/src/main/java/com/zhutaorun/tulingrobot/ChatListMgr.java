package com.zhutaorun.tulingrobot;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zhutaorun on 15/9/2.
 */
public class ChatListMgr {

    private Context context;
    private Handler handler;
    private ArrayList<ChatEntity> list;

    public ChatListMgr(Context context,Handler handler,ArrayList<ChatEntity> list){
        this.context = context;
        this.handler = handler;
        this.list = list;
    }

    public void httpGet(final String info){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = HttpUtil.getData(info);
                try{
                    JSONObject jsonObject = new JSONObject(result);
                    System.out.println(jsonObject.toString());
                    int code = (int) jsonObject.get("code");
                    if(code == HttpUtil.TXT){
                        ChatEntity chatEntity = new ChatEntity();
                        chatEntity.setMsg(jsonObject.getString("text")+"");
                        chatEntity.setDate(new Date());
                        chatEntity.setType(ChatEntity.Type.INCOMING);
                        list.add(chatEntity);
                        handler.sendEmptyMessage(1);
                    }
                    else if(code == HttpUtil.TRAIN){ //列车
                        ChatEntity chatEntity = new ChatEntity();
                        JSONArray List = jsonObject.getJSONArray("list");
                        JSONObject info = List.getJSONObject(0);

                        String Trainnum = info.getString("trainnum");
                        String Start = info.getString("start");
                        String Terminal = info.getString("terminal");
                        String Starttime = info.getString("starttime");
                        String Endtime = info.getString("endtime");

                        String trainnum = "车次："  + Trainnum;
                        String start = "起始站：" + Start;
                        String terminal = "到达站"+ Terminal;
                        String starttime = "开车时间"+Starttime;
                        String endtime = "到达时间"+Endtime;
                        Log.d("TAG",Start);
                        chatEntity.setMsg(trainnum + ", " + start + "," + terminal + "," + starttime + "," + endtime);
                        chatEntity.setDate(new Date());
                        chatEntity.setType(ChatEntity.Type.INCOMING);
                        list.add(chatEntity);//将数据保存到list
                        handler.sendEmptyMessage(1);
                    }
                    else if(code == HttpUtil.NEWS){ //新闻
                        ChatEntity chatEntity = new ChatEntity();
                        chatEntity.setMsg(jsonObject.getString("text")+"");
                        chatEntity.setDate(new Date());
                        chatEntity.setType(ChatEntity.Type.INCOMING);
                        list.add(chatEntity);
                        handler.sendEmptyMessage(1);
                    }else
                        {
                            Toast.makeText(context,"输入信息不合法",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
