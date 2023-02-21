package com.example.gpt_connect.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpt_connect.R;
import com.example.gpt_connect.activity.LiveChat_Activity;
import com.example.gpt_connect.model.FolderData;
import com.example.gpt_connect.model.MessageData;
import com.example.gpt_connect.model.SpinnerFolder;
import com.example.gpt_connect.model.TypeTalking;
import com.example.gpt_connect.sqlite.FolderDAO;
import com.example.gpt_connect.sqlite.ItemDAO;
import com.example.gpt_connect.utils.AppUtil;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;




public class MessageDataAdapter extends RecyclerView.Adapter  {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private List<SpinnerFolder> spinnerDataList = null;
    private Context context;
    private List<MessageData> messageDataList;

    public MessageDataAdapter(LiveChat_Activity context, List<MessageData> messageList) {
        this.context = context;
        this.messageDataList = messageList;
        spinnerDataList = new ArrayList<>(); // initialize folderDataList
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chatbox_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chatbox_message_received, parent, false);
            ImageView imageView=view.findViewById(R.id.imgAvatar);

            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageData message = messageDataList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }

    }

    @Override
    public int getItemCount() {
        return messageDataList != null ? messageDataList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        MessageData message =  messageDataList.get(position);

        if (message.getTypeTalking()== TypeTalking.HUMAN) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    //Lớp này dùng để hiển thị giao diện và dữ liệu do người dùng hỏi OpenAI
    class SentMessageHolder extends RecyclerView.ViewHolder {
        private View view;
        TextView txtPrompt;
        TextView txtSendTime;

        SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtPrompt=view.findViewById(R.id.txtPrompt);
            txtSendTime=view.findViewById(R.id.txtSendTime);
        }

        void bind(MessageData message) {
            txtPrompt.setText(message.getText().trim());

            // Format the stored timestamp into a readable String using method.
            txtSendTime.setText(AppUtil.getTimeFormat(message.getCreated()));
        }
    }
    //Lớp này dùng để hiển thị giao diện và dữ liệu khi nhận được kết quả trả về từ OpenAI
    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private View view;
        TextView txtName;
        TextView txtText;
        TextView txtCreated;
        MessageData messageData = new MessageData() ;
        ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtName=view.findViewById(R.id.txtName);

            txtText=view.findViewById(R.id.txtText);

            txtCreated=view.findViewById(R.id.txtCreated);
            // on item click func
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String string_mes = txtText.getText().toString().trim();//get clicked text
                        // Perform the desired action on item click
                        // su kien alert dialog
                        AlertDialog.Builder b = new AlertDialog.Builder(context);
                        b.setTitle("Save data");
                        b.setIcon(R.drawable.saved_logo);
                        b.setMessage("Do you want to save this message?");
                        b.setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel(); // huy dialog
                            }
                        });
                        b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Dialog dialog = new Dialog(context);
                                datasaver(string_mes,dialog);

                            }
                        });
                        b.create().show();// show dialog
                       // Toast.makeText(context,""+string_mes,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        // this func is use to open data saver dialog
        void datasaver(String string_mes,Dialog dialog)
        {
            dialog.setContentView(R.layout.add_content_data_dialog);
            spinnerDataList.clear();// clear spinner data from previous data saver
            Button XBut,Donebut,addFname;
            TextView icontent;
            EditText edtIname;
            XBut = dialog.findViewById(R.id.xBut);
            Donebut = dialog.findViewById(R.id.doneBut);
            icontent = dialog.findViewById(R.id.icontent);
            addFname = dialog.findViewById(R.id.addBut);
            edtIname = dialog.findViewById(R.id.edtiname);
            // set text to textview icontent
            icontent.setText(string_mes);
            Spinner mySpinner = dialog.findViewById(R.id.spinnerfname);

            // populate the list of FolderData objects
            FolderDAO folderDAO = new FolderDAO(context);
            ItemDAO itemDAO = new ItemDAO(context);
            folderDAO.getFolder(spinnerDataList);// get data fromDAO

            SpinnerAdapter adapter = new SpinnerAdapter( context,spinnerDataList);
            mySpinner.setAdapter(adapter);
            // x but in dialog func
            XBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            Donebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send data to sqlite
                    int spinnerSelect = mySpinner.getSelectedItemPosition();
                    int Fid = spinnerDataList.get(spinnerSelect).getFid();
                    String Fname = spinnerDataList.get(spinnerSelect).getFname();
                    String Iname = edtIname.getText().toString().trim();
                    String Icontent = string_mes;
                    //Toast.makeText(context,"Fid"+Fid,Toast.LENGTH_LONG).show();
                    if(Iname.equals("") == true)
                    {
                        Toast.makeText(context,"Please fill in Item name!",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        // alert accept to send data
                        // su kien alert dialog
                        AlertDialog.Builder b = new AlertDialog.Builder(context);
                        b.setTitle("Add new item");
                        b.setIcon(R.drawable.ic_baseline_post_add_24);
                        b.setMessage("Do you want to add this item to ["+Fname+"]?");
                        b.setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel(); // huy dialog
                            }
                        });
                        b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                itemDAO.insertItem(Fid,Iname,Icontent);
                                Toast.makeText(context,"Success!",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                        b.create().show();// show dialog
                    }

                }
            });
            addFname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spinnerDataList.clear();
                    folder_add(dialog,string_mes,adapter);
                }
            });
            dialog.show();
        }
        void folder_add(Dialog dialog,String string_mes,SpinnerAdapter adapter)
        {
            dialog.setContentView(R.layout.add_folder_name_dialog);
            Button xBut,doneBut,backBut;
            EditText edtFName;
            xBut = dialog.findViewById(R.id.xBut);
            doneBut = dialog.findViewById(R.id.doneaddBut);
            backBut = dialog.findViewById(R.id.backBut);
            edtFName = dialog.findViewById(R.id.edtFoldername);

            xBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            backBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datasaver(string_mes,dialog);
                }
            });
            doneBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FolderDAO folderDAO = new FolderDAO(context);
                    String Foldername = edtFName.getText().toString().trim();
                    // send folder name to sqlite
                    if(Foldername.equals(""))// validate that folder name is not empty
                    {
                        Toast.makeText(context,"Please fill in the blank!",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(folderDAO.checkFname(Foldername)==true)// valiedate folder name
                        {
                            Toast.makeText(context,"You have this folder already!",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            // su kien alert dialog
                            AlertDialog.Builder b = new AlertDialog.Builder(context);
                            b.setTitle("Add new folder");
                            b.setIcon(R.drawable.ic_baseline_create_new_folder_24);
                            b.setMessage("Do you want to create new folder ["+Foldername+"]?");
                            b.setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel(); // huy dialog
                                }
                            });
                            b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    folderDAO.insertFolder(Foldername);
                                    Toast.makeText(context,"Success!",Toast.LENGTH_LONG).show();
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                    datasaver(string_mes,dialog);
                                }
                            });
                            b.create().show();// show dialog
                        }

                    }
                }
            });
        }
        void bind(MessageData message) {
            txtName.setText(message.getUserName());
            txtText.setText(message.getText().trim());
            txtCreated.setText(AppUtil.getTimeFormat(message.getCreated()));
        }
    }
}