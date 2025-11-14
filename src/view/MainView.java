package view;

import controller.AuthController;
import controller.ReservationController;
import controller.RoomController;
import model.Employee;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private final AuthController authController;
    private final RoomController roomController;
    private final ReservationController reservationController;

    // Panels
    private final JPanel cards;
    private final RoomPanel roomPanel;
    private final ReservationPanel reservationPanel;

    public MainView(AuthController authController,
                    RoomController roomController,
                    ReservationController reservationController) {
        this.authController = authController;
        this.roomController = roomController;
        this.reservationController = reservationController;

        setTitle("Hotel - Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cards = new BackgroundPanel("resources/img/fondo.jpg");
        cards.setLayout(new CardLayout());


        // Crear panels
        roomPanel = new RoomPanel(roomController, authController);
        reservationPanel = new ReservationPanel(reservationController, roomController);
        roomPanel.setOpaque(false);
        reservationPanel.setOpaque(false);
        cards.add(roomPanel, "ROOMS");
        cards.add(reservationPanel, "RESERVATIONS");

        SwingUtilities.invokeLater(() -> roomPanel.refreshTable());

        add(buildSideMenu(), BorderLayout.WEST);
        add(cards, BorderLayout.CENTER);
        add(buildTopBar(), BorderLayout.NORTH);

        // Mostrar panel por defecto
        showCard("ROOMS");
        setVisible(true);
    }

    private JPanel buildSideMenu() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 1, 6,6));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JButton btnRooms = new JButton("Habitaciones");
        JButton btnReservations = new JButton("Reservas");
        JButton btnLogout = new JButton("Cerrar sesión");

        btnRooms.addActionListener(e -> showCard("ROOMS"));
        btnReservations.addActionListener(e -> {
            reservationPanel.refreshTable();
            showCard("RESERVATIONS");
        });
        btnLogout.addActionListener(e -> logout());

        p.add(btnRooms);
        p.add(btnReservations);
        p.add(Box.createVerticalStrut(20));
        p.add(btnLogout);
        return p;
    }

    private JPanel buildTopBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(6,10,6,10));
        Employee emp = authController.getLoggedEmployee();
        JLabel lblUser = new JLabel("Usuario: " + (emp != null ? emp.getName() : "N/A"));
        p.add(lblUser, BorderLayout.WEST);
        return p;
    }

    private void showCard(String name) {
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, name);
    }

    private void logout() {
        authController.logout();
        // Volver a login
        this.dispose();
        SwingUtilities.invokeLater(() ->
                new LoginView(authController, roomController, reservationController));
    }
}
