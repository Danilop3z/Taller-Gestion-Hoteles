package view;

import controller.ReservationController;
import model.RoomReservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ReservationView {
    private final ReservationController reservationController;
    private final Scanner scanner;

    public ReservationView(ReservationController reservationController, Scanner scanner) {
        this.reservationController = reservationController;
        this.scanner = scanner;
    }

    public void menu() {
        int option;
        do {
            System.out.println("\n=== GESTIÓN DE RESERVAS ===");
            System.out.println("1. Crear nueva reserva");
            System.out.println("2. Ver todas las reservas");
            System.out.println("3. Registrar salida");
            System.out.println("4. Actualizar reserva");
            System.out.println("5. Ver reservas activas");
            System.out.println("0. Volver");
            System.out.print("Opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                option = -1;
            }

            switch (option) {
                case 1 -> createReservation();
                case 2 -> listReservations();
                case 3 -> closeReservation();
                case 4 -> updateReservation();
                case 5 -> listActiveReservations();
                case 0 -> System.out.println("Regresando al menú principal...");
                default -> System.out.println("Opción inválida, intente nuevamente.");
            }
        } while (option != 0);
    }

    private void createReservation() {
        try {
            System.out.println("\n--- CREAR NUEVA RESERVA ---");
            System.out.print("Código de reserva: ");
            String code = scanner.nextLine();

            System.out.print("Fecha de entrada (YYYY-MM-DD): ");
            LocalDate entryDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Nombre del cliente: ");
            String customer = scanner.nextLine();

            System.out.print("Número de habitación: ");
            int room = Integer.parseInt(scanner.nextLine());

            boolean ok = reservationController.createReservation(code, entryDate, customer, room);
            System.out.println(ok ? "Reserva creada correctamente." : "Error al crear la reserva.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listReservations() {
        List<RoomReservation> list = reservationController.getAllReservations();
        if (list.isEmpty()) {
            System.out.println("No hay reservas registradas.");
            return;
        }
        System.out.println("\n--- LISTA DE RESERVAS ---");
        for (RoomReservation r : list) {
            System.out.println(r);
        }
    }

    private void closeReservation() {
        try {
            System.out.println("\n--- REGISTRAR SALIDA ---");
            System.out.print("Código de reserva: ");
            String code = scanner.nextLine();

            System.out.print("Fecha de salida (YYYY-MM-DD): ");
            LocalDate departure = LocalDate.parse(scanner.nextLine());

            boolean ok = reservationController.closeReservation(code, departure);
            System.out.println(ok ? "Fecha de salida registrada." : "No se encontró la reserva.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listActiveReservations() {
        List<RoomReservation> list = reservationController.getAllReservations();
        boolean found = false;
        System.out.println("\n--- RESERVAS ACTIVAS ---");
        for (RoomReservation r : list) {
            if (r.isActive()) {
                System.out.println(r);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No hay reservas activas.");
        }
    }

    private void updateReservation() {
        try {
            System.out.println("\n--- ACTUALIZAR RESERVA ---");
            System.out.print("Código de reserva: ");
            String code = scanner.nextLine();

            RoomReservation existing = reservationController.findByCode(code);
            if (existing == null) {
                System.out.println("No se encontró la reserva con ese código.");
                return;
            }

            System.out.println("Reserva actual: " + existing);
            System.out.println("Deja un campo vacío si no deseas modificarlo.");

            System.out.print("Nuevo nombre del cliente: ");
            String newCustomer = scanner.nextLine();
            if (newCustomer.trim().isEmpty()) newCustomer = null;

            System.out.print("Nueva fecha de entrada (YYYY-MM-DD): ");
            String newEntryStr = scanner.nextLine();
            LocalDate newEntry = newEntryStr.trim().isEmpty() ? null : LocalDate.parse(newEntryStr);

            System.out.print("Nuevo número de habitación: ");
            String newRoomStr = scanner.nextLine();
            int newRoom = newRoomStr.trim().isEmpty() ? -1 : Integer.parseInt(newRoomStr);

            boolean ok = reservationController.updateReservation(code, newCustomer, newEntry, newRoom);
            System.out.println(ok ? "Reserva actualizada correctamente." : "Error al actualizar la reserva.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

