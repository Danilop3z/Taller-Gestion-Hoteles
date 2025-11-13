package model;

public class HotelRoom {
    private int roomNumber;
    private String typeOfRoom;
    private String state;
    private String employeeName;

    public HotelRoom(int roomNumber, String typeOfRoom, String estado, String employeeName) {
        this.roomNumber = roomNumber;
        this.typeOfRoom = typeOfRoom;
        this.state = estado;
        this.employeeName = employeeName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTypeOfRoom() {
        return typeOfRoom;
    }

    public void setTypeOfRoom(String typeOfRoom) {
        this.typeOfRoom = typeOfRoom;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
                ", typeOfRoom='" + typeOfRoom + '\'' +
                ", estado='" + estado + '\'' +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}
