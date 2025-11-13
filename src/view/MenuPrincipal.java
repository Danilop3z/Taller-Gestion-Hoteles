package view;

import controller.AuthController;
import controller.RoomController;
import controller.ReservationController;

import java.util.Scanner;

public class MenuPrincipal {
    private final AuthController authController;
    private final RoomController roomController;
    private final ReservationController reservationController;
    private final Scanner scanner;

    public MenuPrincipal(AuthController authController, RoomController roomController,
                         ReservationController reservationController, Scanner scanner) {
        this.authController = authController;
        this.roomController = roomController;
        this.reservationController = reservationController;
        this.scanner = scanner;
    }

    public void show() {
        RoomView roomView = new RoomView(roomController, authController, scanner);
        ReservationView reservationView = new ReservationView(reservationController, scanner);

        int option;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Gestionar habitaciones");
            System.out.println("2. Gestionar reservas");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");
            option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> roomView.menu();
                case 2 -> reservationView.menu();
                case 0 -> {
                    System.out.println("Cerrando sesión...");
                    authController.logout();
                }
                default -> System.out.println("Opción inválida.");
            }
        } while (option != 0);
    }
}

