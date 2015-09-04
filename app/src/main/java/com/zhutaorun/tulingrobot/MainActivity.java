package com.zhutaorun.tulingrobot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.font.TextAttribute;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity{


    private ChatListMgr articleListMgr;
    private ListView listView1;
    private TextView textView;
    private EditText editText;
    private String sendStr;
    private ArrayList<ChatEntity> list = new ArrayList<ChatEntity>();
    private ChatAdapter adapter;

    /*
    * 消息处理机制
    * */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case  1:
                    editText.setText("");
                    listView1.setAdapter(adapter);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);//挑战页面
            }
        });
        View view = View.inflate(this,R.layout.item_tuling,null);
        TextView mTulingName = (TextView) view.findViewById(R.id.TulingName);
        mTulingName.setText("sss");

        initView();
        articleListMgr = new ChatListMgr(MainActivity.this,handler,list);//向uri get数据
        adapter = new ChatAdapter(list,MainActivity.this);//将经过解析的数据显示出来
    }

    private void initView() {
        listView1 = (ListView) findViewById(R.id.listView1);
        textView = (TextView) findViewById(R.id.send);
        editText = (EditText) findViewById(R.id.edit);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStr = editText.getText().toString();// 要发送的内容
                if(!"".equals(sendStr)){
                    //先将要发送的数据存到ChatEntity,再存到list
                    list.add(new ChatEntity(ChatEntity.Type.OUTCOMING,new Date(),sendStr));
                    articleListMgr.httpGet(sendStr);//向url get 数据
                }
            }
        });
    }

    class ChatAdapter extends BaseAdapter{


        private ArrayList<ChatEntity> chat_list;
        private LayoutInflater inflater;
        private Context context;
        private ViewHodel viewHodel;
        private ViewHolder2 holder2;

        public ChatAdapter(ArrayList<ChatEntity> chat_list,Context context){
            super();
            this.chat_list = chat_list;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return chat_list.size();
        }

        @Override
        public Object getItem(int position) {
            return chat_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            ChatEntity chatEntity = chat_list.get(position);
            if(chatEntity.getType() == ChatEntity.Type.OUTCOMING){
                return 0;
            }
            return 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                if(getItemViewType(position) == 0){ //自己
                    viewHodel = new ViewHodel();
                    convertView = inflater.inflate(R.layout.item_wo,parent,false);
                    viewHodel.mDate = (TextView) convertView.findViewById(R.id.me_title_time);
                    viewHodel.mMsg = (TextView) convertView.findViewById(R.id.me_msg);
                    convertView.setTag(viewHodel);
                }else {                             //图灵
                    holder2 = new ViewHolder2();
                    convertView = inflater.inflate(R.layout.item_tuling,parent,false);
                    TextView textname = (TextView) convertView.findViewById(R.id.TulingName);
                    SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                    String name = pref.getString("name",""); //取出自定义的名称
                    textname.setText(name);
                    Log.d("MainActivity", "name is " + name);

                    holder2.mDate = (TextView) convertView.findViewById(R.id.me_title_time);
                    holder2.mMsg = (TextView) convertView.findViewById(R.id.me_msg);
                    convertView.setTag(holder2);
                }
            }else {
                if(getItemViewType(position) ==0 ){
                    viewHodel = (ViewHodel) convertView.getTag();
                }else if(getItemViewType(position) == 1){
                    holder2 = (ViewHolder2) convertView.getTag();
                }
            }

            ChatEntity chatEntity = list.get(position);
            if(getItemViewType(position) == 0){
                viewHodel.mDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chatEntity.getDate()));
                viewHodel.mMsg.setText(chatEntity.getMsg());
            }else if(getItemViewType(position) == 1){
                holder2.mDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chatEntity.getDate()));
                holder2.mMsg.setText(chatEntity.getMsg());
            }
            return convertView;
        }
    }

    class ViewHodel{
        TextView mDate;
        TextView mMsg;
    }

    class ViewHolder2{
        TextView mDate;
        TextView mMsg;
    }


}
