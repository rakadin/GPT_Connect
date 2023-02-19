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
        // mở lại trang chu
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
            Toast.makeText(this, "Nhập nội dung.", Toast.LENGTH_SHORT).show();
            return;
        }
        sendMessageByMe(message);
    }

    private void sendMessageByMe(String msg) {
        //Khi người dùng nhấn nút send
        //ta khởi tạo MessageData
        //và gán TypeTalking.HUMAN
        MessageData userMessage = new MessageData();
        userMessage.setUserName("Rakadin");
        userMessage.setTypeTalking(TypeTalking.HUMAN);

        userMessage.setCreated(System.currentTimeMillis());
        userMessage.setText(msg);

        messageList.add(userMessage);
        //Cập nhật lại giao diện
        refreshMessageList();
        edtChatBox.setText("");
        //gửi thông tin này cho OpenAI
        getMessageByOpenAI(msg);
    }

    private void getMessageByOpenAI(String msg) {
        txtOpenAIStatus.setVisibility(View.VISIBLE);
        //dùng AsyncTask để tạo tiểu trình
        AsyncTask<OpenAIInput,Void, OpenAIOutput> task=new AsyncTask<OpenAIInput, Void, OpenAIOutput>() {
            @Override
            protected OpenAIOutput doInBackground(OpenAIInput... openAIInputs) {
                try {
                    //gọi HttpURLConnection
                    //và coding như bên dưới đây:
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
                    //lấy dữ liệu trả về là OpenAIOutput
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
                        //nếu có dữ liệu, ta lấy phần tử đầu tiên trong mảng choices
                        MessageData openAIMessage=openAIOutput.getChoices().get(0);
                        //cập nhật lại tên
                        openAIMessage.setUserName("OpenAI");
                        //cập nhật lại loại OpenAI để show box cho đúng
                        openAIMessage.setTypeTalking(TypeTalking.OPENAI);
                        //cập nhật thời gian để show
                        openAIMessage.setCreated(openAIOutput.getCreated());
                        messageList.add(openAIMessage);
                        //cập nhật giao diện
                        refreshMessageList();

                        txtOpenAIStatus.setVisibility(View.INVISIBLE);
                    }
                }
            }
        };
        //Kích hoạt tiểu trình:
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
