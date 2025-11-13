package controller;

import enums.ETypeFile;
import model.Employee;
import persistence.EmployeePersistence;

import java.util.List;

public class AuthController {
    private final EmployeePersistence employeePersistence;
    private Employee loggedEmployee;

    public AuthController(EmployeePersistence employeePersistence) {
        this.employeePersistence = employeePersistence;
        // Cargue de los empleados desde el archivo plano
        this.employeePersistence.loadFile(ETypeFile.FILE_PLAIN);
    }

    public boolean login(String username, String password) {
        // Verificar la lista cargara
        this.employeePersistence.loadFile(ETypeFile.FILE_PLAIN);

        List<Employee> employees = this.employeePersistence.getListEmployees();

        for (Employee emp : employees) {
            if (emp.getName().equals(username) && emp.getPassword().equals(password)) {
                this.loggedEmployee = emp;
                return true;
            }
        }
        return false;
    }

    public boolean register(String username, String password) {
        // Carga de los datos actuales antes de registrar
        this.employeePersistence.loadFile(ETypeFile.FILE_PLAIN);

        List<Employee> employees = this.employeePersistence.getListEmployees();

        for (Employee emp : employees) {
            if (emp.getName().equals(username)) {
                return false;
            }
        }

        Employee newEmp = new Employee(username, password);
        employees.add(newEmp);

        // Guardar cambios
        this.employeePersistence.setListEmployees(employees);
        this.employeePersistence.dumpFile(ETypeFile.FILE_PLAIN);
        return true;
    }

    public Employee getLoggedEmployee() {
        return loggedEmployee;
    }

    public void logout() {
        this.loggedEmployee = null;
    }
}
