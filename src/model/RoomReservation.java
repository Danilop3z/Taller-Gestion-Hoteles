package model;

import java.time.LocalDate;
import java.util.Objects;

public class RoomReservation {
    private final String reservationCode;
    private final LocalDate entryDate;
    private LocalDate departureDate; // puede ser null
    private String customerName;
    private final int roomNumber;
    private boolean active; // true mientras esté vigente

    public RoomReservation(String reservationCode, LocalDate entryDate, LocalDate departureDate,
                           String customerName, int roomNumber) {
        if (reservationCode == null || reservationCode.trim().isEmpty())
            throw new IllegalArgumentException("El código de reserva no puede estar vacío.");
        if (entryDate == null)
            throw new IllegalArgumentException("La fecha de entrada no puede ser nula.");
        if (customerName == null || customerName.trim().isEmpty())
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        if (roomNumber <= 0)
            throw new IllegalArgumentException("El número de habitación debe ser mayor que 0.");
        if (departureDate != null && departureDate.isBefore(entryDate))
            throw new IllegalArgumentException("La fecha de salida no puede ser anterior a la de entrada.");

        this.reservationCode = reservationCode.trim();
        this.entryDate = entryDate;
        this.departureDate = departureDate;
        this.customerName = customerName.trim();
        this.roomNumber = roomNumber;
        this.active = (departureDate == null); // activa mientras no tenga salida
    }

    public String getReservationCode() {
        return reservationCode;
    }
    public LocalDate getEntryDate() {
        return entryDate;
    }
    public LocalDate getDepartureDate() {
        return departureDate;
    }
    public String getCustomerName() {
        return customerName;
    }
    public int getRoomNumber() {
        return roomNumber;
    }
    public boolean isActive() {
        return active;
    }

    public void setDepartureDate(LocalDate departureDate) {
        if (departureDate != null && departureDate.isBefore(entryDate))
            throw new IllegalArgumentException("La fecha de salida no puede ser anterior a la de entrada.");
        this.departureDate = departureDate;
        this.active = (departureDate == null);
    }

    public void setCustomerName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty())
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        this.customerName = customerName.trim();
    }

    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "RoomReservation{" +
                "code='" + reservationCode + '\'' +
                ", entry=" + entryDate +
                ", departure=" + (departureDate == null ? "Pendiente" : departureDate) +
                ", customer='" + customerName + '\'' +
                ", room=" + roomNumber +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomReservation that = (RoomReservation) o;
        return Objects.equals(reservationCode, that.reservationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationCode);
    }
}

