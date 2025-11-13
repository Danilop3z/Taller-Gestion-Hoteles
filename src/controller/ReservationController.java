package controller;

import model.RoomReservation;
import enums.RoomStatus;
import persistence.ReservationPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationController {
    private final ReservationPersistence persistence;
    private final RoomController roomController;
    private final List<RoomReservation> reservations;

    public ReservationController(ReservationPersistence persistence, RoomController roomController) {
        this.persistence = persistence;
        this.roomController = roomController;
        this.reservations = new ArrayList<>(persistence.loadAll());
    }

    public boolean createReservation(String code, LocalDate entryDate, String customerName, int roomNumber) {

        if (code == null || code.trim().isEmpty()) {
            System.out.println("El código de reserva no puede estar vacío.");
            return false;
        }
        if (entryDate == null) {
            System.out.println("La fecha de entrada no puede ser nula.");
            return false;
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            System.out.println("El nombre del cliente no puede estar vacío.");
            return false;
        }
        if (roomNumber <= 0) {
            System.out.println("Número de habitación inválido.");
            return false;
        }

        for (RoomReservation existing : reservations) {
            if (existing.getReservationCode().equals(code)) {
                System.out.println("Ya existe una reserva con ese código.");
                return false;
            }
        }

        var room = roomController.findRoom(roomNumber);
        if (room == null) {
            System.out.println("La habitación " + roomNumber + " no existe.");
            return false;
        }
        if (room.getState() != RoomStatus.AVAILABLE) {
            System.out.println("La habitación " + roomNumber + " no está disponible.");
            return false;
        }

        RoomReservation newReservation = new RoomReservation(code, entryDate, null, customerName, roomNumber);
        reservations.add(newReservation);
        roomController.updateRoomStatus(roomNumber, RoomStatus.BUSY);

        persistence.saveAll(reservations);
        return true;
    }

    public boolean closeReservation(String reservationCode, LocalDate departureDate) {
        for (RoomReservation r : reservations) {
            if (r.getReservationCode().equals(reservationCode)) {
                if (departureDate == null || departureDate.isBefore(r.getEntryDate())) {
                    System.out.println("La fecha de salida no puede ser anterior a la de entrada.");
                    return false;
                }
                r.setDepartureDate(departureDate);
                r.setActive(false);
                roomController.updateRoomStatus(r.getRoomNumber(), RoomStatus.AVAILABLE);
                persistence.saveAll(reservations);
                return true;
            }
        }
        System.out.println("Reserva no encontrada.");
        return false;
    }

    public boolean updateReservation(String code, String newCustomerName, LocalDate newEntryDate, int newRoomNumber) {
        for (RoomReservation r : new ArrayList<>(reservations)) {
            if (r.getReservationCode().equals(code)) {
                if (newCustomerName != null && newCustomerName.trim().isEmpty()) {
                    System.out.println("Nombre de cliente inválido.");
                    return false;
                }
                if (newEntryDate != null && r.getDepartureDate() != null && newEntryDate.isAfter(r.getDepartureDate())) {
                    System.out.println("La nueva fecha de entrada no puede ser posterior a la fecha de salida registrada.");
                    return false;
                }

                int oldRoom = r.getRoomNumber();

                if (newRoomNumber > 0 && newRoomNumber != oldRoom) {
                    var targetRoom = roomController.findRoom(newRoomNumber);
                    if (targetRoom == null) {
                        System.out.println("La habitación destino " + newRoomNumber + " no existe.");
                        return false;
                    }
                    if (targetRoom.getState() != RoomStatus.AVAILABLE) {
                        System.out.println("La habitación destino " + newRoomNumber + " no está disponible.");
                        return false;
                    }
                    roomController.updateRoomStatus(oldRoom, RoomStatus.AVAILABLE);
                    roomController.updateRoomStatus(newRoomNumber, RoomStatus.BUSY);
                }

                String finalCustomer = (newCustomerName != null) ? newCustomerName : r.getCustomerName();
                LocalDate finalEntry = (newEntryDate != null) ? newEntryDate : r.getEntryDate();
                int finalRoom = (newRoomNumber > 0) ? newRoomNumber : oldRoom;
                LocalDate finalDeparture = r.getDepartureDate();

                RoomReservation updated = new RoomReservation(code, finalEntry, finalDeparture, finalCustomer, finalRoom);
                reservations.remove(r);
                reservations.add(updated);
                persistence.saveAll(reservations);
                return true;
            }
        }
        System.out.println("No se encontró la reserva con el código indicado.");
        return false;
    }

    public List<RoomReservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    public RoomReservation findByCode(String code) {
        for (RoomReservation r : reservations) {
            if (r.getReservationCode().equals(code)) return r;
        }
        return null;
    }
}
