import controller.AuthController;
import controller.ReservationController;
import controller.RoomController;
import persistence.EmployeePersistence;
import persistence.ReservationPersistence;
import persistence.RoomPersistence;
import view.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Crear persistencias y controllers (únicas instancias)
        EmployeePersistence empP = new EmployeePersistence();
        AuthController authController = new AuthController(empP);

        RoomPersistence roomP = new RoomPersistence();
        RoomController roomController = new RoomController(roomP);

        ReservationPersistence resP = new ReservationPersistence();
        ReservationController reservationController = new ReservationController(resP, roomController);

        // Forzar look and feel del sistema (opcional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Lanzar login en EDT
        SwingUtilities.invokeLater(() -> {
            new view.LoginView(authController, roomController, reservationController);
        });
    }
}
