package controller;

import enums.ETypeFile;
import enums.RoomStatus;
import enums.RoomType;
import model.HotelRoom;
import persistence.RoomPersistence;

import java.util.ArrayList;
import java.util.List;

public class RoomController {
    private final RoomPersistence persistence;

    public RoomController(RoomPersistence persistence) {
        this.persistence = persistence;
        // Cargar las habitaciones desde archivo JSON al iniciar
        this.persistence.loadFile(ETypeFile.JSON);
    }

    public boolean createRoom(int number, RoomType type, String employeeName) {
        HotelRoom newRoom = new HotelRoom(number, type, RoomStatus.AVAILABLE, employeeName);
        boolean added = persistence.addHotelRoom(newRoom);

        if (added) {
            persistence.dumpFile(ETypeFile.JSON); // Guardar cambios
        }
        return added;
    }

    public boolean updateRoomStatus(int roomNumber, RoomStatus newState) {
        // Asegurar que los datos estén actualizados
        persistence.loadFile(ETypeFile.JSON);

        // Buscar la habitación directamente desde la lista interna
        HotelRoom room = findRoom(roomNumber);
        if (room != null) {
            room.setState(newState);
            persistence.dumpFile(ETypeFile.JSON); // Guardar cambios
            return true;
        }
        return false;
    }

    public List<HotelRoom> getAllRooms() {
        persistence.loadFile(ETypeFile.JSON);
        return new ArrayList<>(persistence.getListRooms());
    }

    public HotelRoom findRoom(int roomNumber) {
        persistence.loadFile(ETypeFile.JSON);
        for (HotelRoom r : persistence.getListRooms()) {
            if (r.getRoomNumber() == roomNumber) return r;
        }
        return null;
    }
}
