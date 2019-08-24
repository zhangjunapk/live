package org.zj.xh.bean;

import lombok.Data;

@Data
public class RoomBean {
    private String roomId;
    private String roomName;

    public RoomBean(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public RoomBean() {
    }
}
