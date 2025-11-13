
import controller.AuthController;
import controller.ReservationController;
import controller.RoomController;
import persistence.EmployeePersistence;
import persistence.ReservationPersistence;
import persistence.RoomPersistence;
import view.AuthView;
import view.MenuPrincipal;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        EmployeePersistence employeePersistence = new EmployeePersistence();
        RoomPersistence roomPersistence = new RoomPersistence();
        ReservationPersistence reservationPersistence = new ReservationPersistence();

        AuthController authController = new AuthController(employeePersistence);
        RoomController roomController = new RoomController(roomPersistence);
        ReservationController reservationController = new ReservationController(reservationPersistence, roomController);

        AuthView authView = new AuthView(authController, scanner);
        MenuPrincipal mainMenu = new MenuPrincipal(authController, roomController, reservationController, scanner);

        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== SISTEMA DE GESTIÓN HOTELERA =====");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrar nuevo empleado");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                option = -1;
            }

            switch (option) {
                case 1 -> {
                    if (authView.login()) {
                        mainMenu.show();
                    }
                }
                case 2 -> authView.register();
                case 0 -> {
                    System.out.println("Saliendo del sistema...");
                    exit = true;
                }
                default -> System.out.println("Opción inválida, intente nuevamente.");
            }
        }

        scanner.close();
    }
}

