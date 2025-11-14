package view;

import controller.ReservationController;
import controller.RoomController;
import model.RoomReservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ReservationPanel extends JPanel {

    private final ReservationController reservationController;
    private final RoomController roomController;

    private final DefaultTableModel tableModel;
    private final JTable table;

    // CREATE FORM FIELDS
    private final JTextField txtCode = new JTextField(10);
    private final JTextField txtEntry = new JTextField(10); // YYYY-MM-DD
    private final JTextField txtCustomer = new JTextField(15);
    private final JTextField txtRoomNumber = new JTextField(6);

    // CLOSE FORM FIELDS
    private final JTextField txtCloseCode = new JTextField(10);
    private final JTextField txtCloseDate = new JTextField(10); // YYYY-MM-DD

    public ReservationPanel(ReservationController reservationController, RoomController roomController) {
        this.reservationController = reservationController;
        this.roomController = roomController;

        setLayout(new BorderLayout(8, 8));

        // -------------- 🚀 PRIMERO CREAR TABLA Y MODELO -----------------
        tableModel = new DefaultTableModel(
                new Object[]{"Código", "Entrada", "Salida", "Cliente", "Hab.", "Activa"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        // -----------------------------------------------------------------

        // Construir interfaz ahora que table YA EXISTE
        add(buildCreateForm(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildBottomActions(), BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel buildCreateForm() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.setBorder(BorderFactory.createTitledBorder("Crear reserva (YYYY-MM-DD)"));

        p.add(new JLabel("Código:")); p.add(txtCode);
        p.add(new JLabel("Entrada:")); p.add(txtEntry);
        p.add(new JLabel("Cliente:")); p.add(txtCustomer);
        p.add(new JLabel("Hab.:")); p.add(txtRoomNumber);

        JButton btn = new JButton("Crear");
        btn.addActionListener(e -> createReservation());
        p.add(btn);

        return p;
    }

    private JScrollPane buildTablePanel() {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder("Listado de reservas"));
        return sp;
    }

    private JPanel buildBottomActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.setBorder(BorderFactory.createTitledBorder("Acciones"));

        p.add(new JLabel("Cerrar (código):"));
        p.add(txtCloseCode);
        p.add(new JLabel("Salida:"));
        p.add(txtCloseDate);

        JButton btnClose = new JButton("Cerrar");
        btnClose.addActionListener(e -> closeReservation());
        p.add(btnClose);

        JButton btnRefresh = new JButton("Refrescar");
        btnRefresh.addActionListener(e -> refreshTable());
        p.add(btnRefresh);

        JButton btnDelete = new JButton("Eliminar lógico");
        btnDelete.addActionListener(e -> logicalDeleteSelected());
        p.add(btnDelete);

        JButton btnEdit = new JButton("Editar seleccionada");
        btnEdit.addActionListener(e -> editSelected());
        p.add(btnEdit);

        return p;
    }

    private void createReservation() {
        try {
            String code = txtCode.getText().trim();
            LocalDate entry = LocalDate.parse(txtEntry.getText().trim());
            String customer = txtCustomer.getText().trim();
            int room = Integer.parseInt(txtRoomNumber.getText().trim());

            boolean ok = reservationController.createReservation(code, entry, customer, room);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Reserva creada.");
                clearCreateForm();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Formato: YYYY-MM-DD");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número de habitación inválido.");
        }
    }

    private void clearCreateForm() {
        txtCode.setText("");
        txtEntry.setText("");
        txtCustomer.setText("");
        txtRoomNumber.setText("");
    }

    private void closeReservation() {
        try {
            String code = txtCloseCode.getText().trim();
            LocalDate departure = LocalDate.parse(txtCloseDate.getText().trim());

            boolean ok = reservationController.closeReservation(code, departure);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Reserva cerrada.");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo cerrar la reserva.");
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Fecha inválida (YYYY-MM-DD)");
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);

        List<RoomReservation> list = reservationController.getAllReservations();
        for (RoomReservation r : list) {
            tableModel.addRow(new Object[]{
                    r.getReservationCode(),
                    r.getEntryDate(),
                    r.getDepartureDate() != null ? r.getDepartureDate() : "",
                    r.getCustomerName(),
                    r.getRoomNumber(),
                    r.isActive()
            });
        }
    }

    private void logicalDeleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una reserva.");
            return;
        }

        String code = tableModel.getValueAt(row, 0).toString();

        var r = reservationController.findByCode(code);
        if (r == null) {
            JOptionPane.showMessageDialog(this, "No encontrada.");
            return;
        }

        r.setActive(false);

        roomController.updateRoomStatus(r.getRoomNumber(), enums.RoomStatus.AVAILABLE);

        reservationController.updateReservation(
                r.getReservationCode(),
                r.getCustomerName(),
                r.getEntryDate(),
                r.getRoomNumber()
        );

        JOptionPane.showMessageDialog(this, "Reserva desactivada. Habitación liberada.");
        refreshTable();
    }


    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una reserva.");
            return;
        }

        String code = tableModel.getValueAt(row, 0).toString();
        var r = reservationController.findByCode(code);
        if (r == null) return;

        JTextField fCustomer = new JTextField(r.getCustomerName());
        JTextField fEntry = new JTextField(r.getEntryDate().toString());
        JTextField fRoom = new JTextField(String.valueOf(r.getRoomNumber()));

        Object[] msg = {
                "Cliente:", fCustomer,
                "Entrada:", fEntry,
                "Habitación:", fRoom
        };

        int option = JOptionPane.showConfirmDialog(this, msg, "Editar reserva", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                boolean ok = reservationController.updateReservation(
                        code,
                        fCustomer.getText().trim(),
                        LocalDate.parse(fEntry.getText().trim()),
                        Integer.parseInt(fRoom.getText().trim())
                );

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Reserva actualizada.");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage());
            }
        }
    }
}

