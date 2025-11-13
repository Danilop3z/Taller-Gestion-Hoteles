package model;

public class RoomReservation {
    private int reservationCode;
    private String entryDate;
    private String departureDate; //null
    private String customerName;
    private int roomNumber;

    public RoomReservation(int reservationCode, String entryDate, String departureDate, String customerName, int roomNumber) {
        this.reservationCode = reservationCode;
        this.entryDate = entryDate;
        this.departureDate = departureDate;
        this.customerName = customerName;
        this.roomNumber = roomNumber;
    }

    public int getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(int reservationCode) {
        this.reservationCode = reservationCode;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Override
    public String toString() {
        return "RoomReservation{" +
                "reservationCode=" + reservationCode +
                ", entryDate='" + entryDate + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
