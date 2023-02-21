package com.example.gpt_connect.model;

public class Item {
    private int ItemID;
    private int FolderID;
    private String ItemName;
    private String ItemContent;

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public int getFolderID() {
        return FolderID;
    }

    public void setFolderID(int folderID) {
        FolderID = folderID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemContent() {
        return ItemContent;
    }

    public void setItemContent(String itemContent) {
        ItemContent = itemContent;
    }
}
