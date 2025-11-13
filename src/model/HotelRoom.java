package model;

import enums.RoomStatus;
import enums.RoomType;

public class HotelRoom {
    private int roomNumber;
    private RoomType typeRoom;
    private RoomStatus state;
    private String employeeName;

    public HotelRoom(int roomNumber, RoomType typeRoom, RoomStatus state, String employeeName) {
        this.roomNumber = roomNumber;
        this.typeRoom = typeRoom;
        this.state = state;
        this.employeeName = employeeName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getTypeRoom() {
        return typeRoom;
    }

    public void setTypeRoom(RoomType typeRoom) {
        this.typeRoom = typeRoom;
    }

    public RoomStatus getState() {
        return state;
    }

    public void setState(RoomStatus state) {
        this.state = state;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "HotelRoom{" +
                "roomNumber=" + roomNumber +
                ", typeOfRoom='" + typeRoom + '\'' +
                ", estado='" + state + '\'' +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}
