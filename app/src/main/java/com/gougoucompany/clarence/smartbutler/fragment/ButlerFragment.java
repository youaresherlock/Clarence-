package com.gougoucompany.clarence.smartbutler.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gougoucompany.clarence.smartbutler.R;
import com.gougoucompany.clarence.smartbutler.adapter.ChatListAdapter;
import com.gougoucompany.clarence.smartbutler.entity.ChatListData;
import com.gougoucompany.clarence.smartbutler.utils.L;
import com.gougoucompany.clarence.smartbutler.utils.ShareUtils;
import com.gougoucompany.clarence.smartbutler.utils.StaticClass;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   SmartButler
 * 包名:     com.gougoucompany.clarence.smartbutler.fragment
 * 文件名:   ButlerFragment
 * 创建时间: 2018/4/27 10:55
 * 英文名:   Clarence
 * 中文名:   习伟博
 * 描述:     管家服务
 */

public class ButlerFragment extends Fragment implements View.OnClickListener {

    private ListView mChatListView;
    private ChatListAdapter adapter;

    private List<ChatListData> mList = new ArrayList<>();

    //输入框
    private EditText et_text;
    //发送按钮
    private Button btn_send;


    //TTS语音合成对象
    private SpeechSynthesizer mTts;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认发音人
    private String voicer = "xiaoyan";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*inflater对象的inflate()方法
        * View inflate(int resource, ViewGroup root)
        * Inflate a new view hierarchy from the specified xml resource.Throws
        * InflateException if there is an error.*/
        View view = inflater.inflate(R.layout.fragment_butler, null);
        findView(view);
        return view;
    }

    //初始化view
    private void findView(View view){

        initTTS();

//        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
//        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
//        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
//        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
//        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
//        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
//        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
//        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
//        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
//        //如果不需要保存合成音频，注释该行代码
//        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

        mChatListView = (ListView) view.findViewById(R.id.mChatListView);
        et_text = (EditText) view.findViewById(R.id.et_text);
        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        //设置适配器
        adapter = new ChatListAdapter(getActivity(), mList);
        mChatListView.setAdapter(adapter);

        addLeftItem("你好，我是你的聊天小助手");
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_send:
                /**
                 * 逻辑
                 * 1.获取输入框的内容
                 * 2.判断是否为空
                 * 3.用户输入字符串过长，会使机器人学用户说话，所以要限制用户发送的长度
                 * 4.清空当前的输入框
                 * 5.添加你输入的内容到right_item
                 * 6.发送给机器人请求返回内容
                 * 7.拿到机器人的返回值之后添加在left_item
                 */
                //1.获取输入框的内容
                String text = et_text.getText().toString();
                //2.判断用户输入是否为空
                if(!TextUtils.isEmpty(text)) {
                    //3.判断长度不能大于30
                    if(text.length() > 30){
                        Toast.makeText(getActivity(), "输入长度超出限制", Toast.LENGTH_SHORT).show();
                    } else {
                        //4.清空当前的输入框
                        et_text.setText("");
                        //5.添加你输入的内容到right item
                        addRightItem(text);
                        //6.发送给机器人请求
                        String url = "http://www.tuling123.com/openapi/api?key=" + StaticClass.CHAT_LIST_KEY + "&info=" + text;
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                              // Toast.makeText(getActivity(), "Json:" + t, Toast.LENGTH_SHORT).show();
                                L.i("Json:" + t);
                                parsingJson(t);
                            }
                        });
                    }

                } else {
                    Toast.makeText(getActivity(), "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //解析json
    private void parsingJson(String t){
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject(t);
            //通过key获取value
            String text = jsonObject.getString("text");
            addLeftItem(text);
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    //添加左边文本
    private void addLeftItem(String text){

//        //如果语音播报按钮是开启的，那么才执行语音播报的方法
        boolean isSpeak = ShareUtils.getBoolean(getActivity(), "isSpeak", false);

        if(isSpeak){
            startSpeak(text);
        }

        ChatListData data = new ChatListData();
        data.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        data.setText(text);
        mList.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }

    //添加右边文本
    private void addRightItem(String text){
        ChatListData data = new ChatListData();
        data.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        data.setText(text);
        mList.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }

    //开始说话
    private void startSpeak(String text){
        //3.开始合成
        mTts.startSpeaking(text, mTtsListener);
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onCompleted(SpeechError error) {
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            L.i("InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
               L.e("初始化失敗，错误码: " + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    //初始化语音合成对象
    private void initTTS(){
        //1.初始化
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), mTtsInitListener);
        //2.设置参数
        setParam();
    }

    /**
     * 参数设置
     * @return
     */
    private void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }


}



























































