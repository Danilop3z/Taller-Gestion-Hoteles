package view;

import controller.AuthController;
import controller.RoomController;
import enums.RoomType;
import model.HotelRoom;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoomPanel extends JPanel {
    private final RoomController roomController;
    private final AuthController authController;

    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField txtNumber = new JTextField(8);
    private final JComboBox<RoomType> cbType = new JComboBox<>(RoomType.values());
    private final JTextField txtEmployee = new JTextField(20);

    public RoomPanel(RoomController roomController, AuthController authController) {
        this.roomController = roomController;
        this.authController = authController;

        // PRIMERO crear tabla y modelo
        tableModel = new DefaultTableModel(new Object[]{"Número", "Tipo", "Estado", "Empleado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        setLayout(new BorderLayout(8, 8));
        add(buildForm(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);

        // autollenar empleado
        txtEmployee.setEditable(false);
        if (authController.getLoggedEmployee() != null) {
            txtEmployee.setText(authController.getLoggedEmployee().getName());
        }
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,10));
        p.setBorder(BorderFactory.createTitledBorder("Crear / Eliminar habitación"));

        p.add(new JLabel("Número:"));
        p.add(txtNumber);
        p.add(new JLabel("Tipo:"));
        p.add(cbType);
        p.add(new JLabel("Empleado:"));
        p.add(txtEmployee);

        JButton btnCreate = new JButton("Crear");
        btnCreate.addActionListener(e -> createRoom());
        p.add(btnCreate);

        JButton btnDelete = new JButton("Eliminar");
        btnDelete.addActionListener(e -> deleteSelectedRoom());
        p.add(btnDelete);

        return p;
    }


    private JScrollPane buildTablePanel() {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder("Listado de habitaciones"));
        return sp;
    }

    private void createRoom() {
        try {
            int number = Integer.parseInt(txtNumber.getText().trim());
            RoomType type = (RoomType) cbType.getSelectedItem();
            String employeeName = txtEmployee.getText().trim();

            boolean created = roomController.createRoom(number, type, employeeName);
            if (created) {
                JOptionPane.showMessageDialog(this, "Habitación creada.", "OK", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear. ¿Número ya existe?", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número de habitación inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);

        // Forzar recarga para tabla
        List<HotelRoom> rooms = roomController.getAllRooms();

        for (HotelRoom r : rooms) {
            tableModel.addRow(new Object[]{
                    r.getRoomNumber(),
                    r.getTypeRoom().name(),
                    r.getState().name(),
                    r.getEmployeeName()
            });
        }
    }
    private void deleteSelectedRoom() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una habitación.");
            return;
        }

        int roomNumber = (int) tableModel.getValueAt(row, 0);

        int opt = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la habitación " + roomNumber + "?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (opt != JOptionPane.YES_OPTION) return;

        var rooms = roomController.getAllRooms();
        boolean removed = rooms.removeIf(r -> r.getRoomNumber() == roomNumber);

        if (removed) {
            roomController.getPersistence().setListRooms(rooms);
            roomController.getPersistence().dumpFile(enums.ETypeFile.JSON);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Habitación eliminada.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar.");
        }
    }

}

