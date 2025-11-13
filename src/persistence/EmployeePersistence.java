package persistence;

import enums.ETypeFile;
import interfaces.IActionsFile;
import model.Employee;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmployeePersistence extends FilePlain implements IActionsFile {
    private List<Employee> listEmployees;

    public EmployeePersistence(){
        this.listEmployees = new ArrayList<Employee>();
    }

    private Employee findEmployeeByName(String name) {
        for (Employee employee : this.listEmployees) {
            if (employee.getName().equals(name)) {
                return employee;
            }
        }
        return null;
    }

    private Boolean addEmploye(Employee employee){
        if (Objects.isNull(this.findEmployeeByName(employee.getName()))){
            this.listEmployees.add(employee);
        return true;
        }
        return false;
    }


    @Override
    public void loadFile(ETypeFile eTypeFile) {
        loadFileSerializate();
    }

    private void loadFileSerializate() {
        try(FileInputStream fileIn = new FileInputStream(
                this.config.getPathFiles()
                .concat(this.config.getNameFileSer()));
        ObjectInputStream in = new ObjectInputStream(fileIn)) {
            this.listEmployees = (List<Employee>) in.readObject();
        }catch (IOException i) {
            i.printStackTrace();
        }catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }

    @Override
    public void dumpFile(ETypeFile eTypeFile) {
        dumpFileSerializate();
    }

    private void dumpFileSerializate() {
        try(FileOutputStream fileOut = new FileOutputStream
                (this.config.getPathFiles().concat(this.config.getNameFileSer()));
        ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this.listEmployees);
        }catch (IOException i){
            i.printStackTrace();
        }
    }
}
