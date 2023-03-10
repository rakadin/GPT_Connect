package com.example.gpt_connect.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gpt_connect.MainActivity;
import com.example.gpt_connect.R;
import com.example.gpt_connect.adapter.MessageDataAdapter;
import com.example.gpt_connect.api.OpenAI;
import com.example.gpt_connect.model.MessageData;
import com.example.gpt_connect.model.OpenAIInput;
import com.example.gpt_connect.model.OpenAIOutput;
import com.example.gpt_connect.model.TypeTalking;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LiveChat_Activity extends AppCompatActivity {
    private static final String TAG = LiveChat_Activity.class.getSimpleName();
    RecyclerView rvMessageData;
    EditText edtChatBox;
    TextView txtOpenAIStatus;
    ImageView imgChatBoxSend;
    ImageButton backbut;

    private MessageDataAdapter messageDataAdapter;
    private List<MessageData> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat);
        getSupportActionBar().hide();
        addControls();
        addEvents();
    }

    private void addControls() {
        edtChatBox=findViewById(R.id.edtChatBox);
        rvMessageData=findViewById(R.id.rvMessageData);
        txtOpenAIStatus=findViewById(R.id.txtOpenAIStatus);
        imgChatBoxSend=findViewById(R.id.imgChatBoxSend);
        backbut = findViewById(R.id.backbutton);
        messageList = new ArrayList<>();

        messageDataAdapter = new MessageDataAdapter(this, messageList);
        rvMessageData.setLayoutManager(new LinearLayoutManager(this));
        rvMessageData.setAdapter(messageDataAdapter);
    }

    private void addEvents() {
        // kich hoat chat box
        imgChatBoxSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSendMessage();
            }
        });
        // m??? l???i trang chu
        backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void processSendMessage() {
        String message = edtChatBox.getText().toString();
        if (message.isEmpty()) {
            Toast.makeText(this, "Nh???p n???i dung.", Toast.LENGTH_SHORT).show();
            return;
        }
        sendMessageByMe(message);
    }

    private void sendMessageByMe(String msg) {
        //Khi ng?????i d??ng nh???n n??t send
        //ta kh???i t???o MessageData
        //v?? g??n TypeTalking.HUMAN
        MessageData userMessage = new MessageData();
        userMessage.setUserName("Rakadin");
        userMessage.setTypeTalking(TypeTalking.HUMAN);

        userMessage.setCreated(System.currentTimeMillis());
        userMessage.setText(msg);

        messageList.add(userMessage);
        //C???p nh???t l???i giao di???n
        refreshMessageList();
        edtChatBox.setText("");
        //g???i th??ng tin n??y cho OpenAI
        getMessageByOpenAI(msg);
    }

    private void getMessageByOpenAI(String msg) {
        txtOpenAIStatus.setVisibility(View.VISIBLE);
        //d??ng AsyncTask ????? t???o ti???u tr??nh
        AsyncTask<OpenAIInput,Void, OpenAIOutput> task=new AsyncTask<OpenAIInput, Void, OpenAIOutput>() {
            @Override
            protected OpenAIOutput doInBackground(OpenAIInput... openAIInputs) {
                try {
                    //g???i HttpURLConnection
                    //v?? coding nh?? b??n d?????i ????y:
                    URL url = new URL(OpenAI.API);
                    HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Authorization","Bearer "+OpenAI.TOKEN);
                    conn.setRequestProperty("Content-Type",OpenAI.CONTENT_TYPE);
                    conn.setRequestMethod(OpenAI.METHOD);
                    conn.setDoOutput(true);
                    OpenAIInput aiInput=openAIInputs[0];
                    String myData=new Gson().toJson(aiInput);
                    conn.getOutputStream().write(myData.getBytes());
                    InputStream responseBody = conn.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                    //l???y d??? li???u tr??? v??? l?? OpenAIOutput
                    OpenAIOutput data= new Gson().fromJson(responseBodyReader, OpenAIOutput.class);
                    return data;
                }
                catch (Exception ex)
                {
                    Log.e(TAG,ex.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(OpenAIOutput openAIOutput) {
                super.onPostExecute(openAIOutput);
                if(openAIOutput!=null)
                {
                    if(openAIOutput.getChoices()!=null)
                    {
                        //n???u c?? d??? li???u, ta l???y ph???n t??? ?????u ti??n trong m???ng choices
                        MessageData openAIMessage=openAIOutput.getChoices().get(0);
                        //c???p nh???t l???i t??n
                        openAIMessage.setUserName("OpenAI");
                        //c???p nh???t l???i lo???i OpenAI ????? show box cho ????ng
                        openAIMessage.setTypeTalking(TypeTalking.OPENAI);
                        //c???p nh???t th???i gian ????? show
                        openAIMessage.setCreated(openAIOutput.getCreated());
                        messageList.add(openAIMessage);
                        //c???p nh???t giao di???n
                        refreshMessageList();

                        txtOpenAIStatus.setVisibility(View.INVISIBLE);
                    }
                }
            }
        };
        //K??ch ho???t ti???u tr??nh:
        OpenAIInput input=new OpenAIInput(msg);
        task.execute(input);
    }
    private void refreshMessageList() {
        messageDataAdapter.notifyDataSetChanged();
        rvMessageData.scrollToPosition(messageList.size() - 1);
    }
    private void getItemClick()
    {

    }
}
