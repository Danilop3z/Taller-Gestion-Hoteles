package view;

import controller.AuthController;
import controller.ReservationController;
import controller.RoomController;
import model.Employee;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private final AuthController authController;
    private final RoomController roomController;
    private final ReservationController reservationController;

    private final JTextField txtUser = new JTextField(20);
    private final JPasswordField txtPass = new JPasswordField(20);

    public LoginView(AuthController authController,
                     RoomController roomController,
                     ReservationController reservationController) {
        this.authController = authController;
        this.roomController = roomController;
        this.reservationController = reservationController;

        setTitle("Hotel - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 230);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8,8));

        add(buildForm(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        p.add(new JLabel("Usuario:"), c);
        c.gridx = 1;
        p.add(txtUser, c);

        c.gridx = 0; c.gridy = 1;
        p.add(new JLabel("Contraseña:"), c);
        c.gridx = 1;
        p.add(txtPass, c);

        return p;
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogin = new JButton("Ingresar");
        JButton btnRegister = new JButton("Registrar");

        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> doRegister());

        p.add(btnRegister);
        p.add(btnLogin);
        return p;
    }

    private void doLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña requeridos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean ok = authController.login(user, pass);
        if (ok) {
            Employee logged = authController.getLoggedEmployee();
            JOptionPane.showMessageDialog(this, "Bienvenido " + logged.getName(), "OK", JOptionPane.INFORMATION_MESSAGE);
            // Abrir main view y ocultar login
            new MainView(authController, roomController, reservationController);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doRegister() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña requeridos para registrar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean created = authController.register(user, pass);
        if (created) {
            JOptionPane.showMessageDialog(this, "Usuario registrado. Ya puedes ingresar.", "OK", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "El nombre de usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
