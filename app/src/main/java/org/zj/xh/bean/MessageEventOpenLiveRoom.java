package org.zj.xh.bean;

import lombok.Data;

@Data
public class MessageEventOpenLiveRoom {
    private String roomId;

    public MessageEventOpenLiveRoom() {
    }

    public MessageEventOpenLiveRoom(String roomId) {
        this.roomId = roomId;
    }
}
