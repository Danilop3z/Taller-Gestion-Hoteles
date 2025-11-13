package view;

import controller.RoomController;
import controller.AuthController;
import model.HotelRoom;
import enums.RoomType;
import enums.RoomStatus;

import java.util.List;
import java.util.Scanner;

public class RoomView {
    private final RoomController roomController;
    private final AuthController authController;
    private final Scanner scanner;

    public RoomView(RoomController roomController, AuthController authController, Scanner scanner) {
        this.roomController = roomController;
        this.authController = authController;
        this.scanner = scanner;
    }

    public void menu() {
        int option;
        do {
            System.out.println("\n=== GESTIÓN DE HABITACIONES ===");
            System.out.println("1. Crear habitación");
            System.out.println("2. Ver habitaciones");
            System.out.println("3. Cambiar estado");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> createRoom();
                case 2 -> listRooms();
                case 3 -> updateRoomStatus();
                case 0 -> System.out.println("Regresando...");
                default -> System.out.println("Opción inválida.");
            }
        } while (option != 0);
    }

    private void createRoom() {
        try {
            System.out.print("Número de habitación: ");
            int number = Integer.parseInt(scanner.nextLine());
            System.out.print("Tipo (SIMPLE, DOUBLE, SUITE): ");
            RoomType type = RoomType.valueOf(scanner.nextLine().toUpperCase());

            String empName = authController.getLoggedEmployee().getName();
            boolean ok = roomController.createRoom(number, type, empName);
            System.out.println(ok ? "Habitación creada correctamente." : "Ya existe una habitación con ese número.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listRooms() {
        List<HotelRoom> rooms = roomController.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No hay habitaciones registradas.");
            return;
        }
        System.out.println("\n--- LISTA DE HABITACIONES ---");
        for (HotelRoom r : rooms) {
            System.out.println(r);
        }
    }

    private void updateRoomStatus() {
        System.out.print("Número de habitación: ");
        int num = Integer.parseInt(scanner.nextLine());
        System.out.print("Nuevo estado (AVAILABLE, BUSY): ");
        RoomStatus status = RoomStatus.valueOf(scanner.nextLine().toUpperCase());

        boolean ok = roomController.updateRoomStatus(num, status);
        System.out.println(ok ? "Estado actualizado." : "Habitación no encontrada.");
    }
}

