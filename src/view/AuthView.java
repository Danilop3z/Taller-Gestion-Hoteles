package view;

import controller.AuthController;

import java.util.Scanner;

public class AuthView {
    private final AuthController authController;
    private final Scanner scanner;

    public AuthView(AuthController authController, Scanner scanner) {
        this.authController = authController;
        this.scanner = scanner;
    }

    public boolean login() {
        System.out.println("=== INICIO DE SESIÓN ===");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        boolean ok = authController.login(user, pass);
        if (ok) {
            System.out.println("Bienvenido, " + authController.getLoggedEmployee().getName() + "!");
        } else {
            System.out.println("Credenciales inválidas.");
        }
        return ok;
    }

    public void register() {
        System.out.println("=== REGISTRAR EMPLEADO ===");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        boolean ok = authController.register(user, pass);
        if (ok) {
            System.out.println("Empleado registrado correctamente.");
        } else {
            System.out.println("El usuario ya existe.");
        }
    }
}

