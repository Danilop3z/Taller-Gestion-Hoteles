package persistence;

import constants.CommonConstants;
import enums.ETypeFile;
import enums.RoomStatus;
import enums.RoomType;
import interfaces.IActionsFile;
import model.HotelRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class RoomPersistence extends FilePlain implements IActionsFile {
    private List<HotelRoom> listRooms;

    public RoomPersistence() {
        this.listRooms = new ArrayList<HotelRoom>();
    }

    private HotelRoom findRoomById(int id) {
        for(HotelRoom hotelRoom: this.listRooms) {
            if(hotelRoom.getRoomNumber() == id) {
                return hotelRoom;
            }
        }
        return null;
    }

    public Boolean addHotelRoom(HotelRoom hotelRoom) {
        if(Objects.isNull(this.findRoomById(hotelRoom.getRoomNumber()))) {
            this.listRooms.add(hotelRoom);
            return true;
        }
        return false;
    }


    @Override
    public void loadFile(ETypeFile eTypeFile) {
        loadFileJSON();
    }

    private void loadFileJSON() {

        //Limpiar Lista antes

        this.listRooms.clear();

        List<String> contentInLine = this.reader(
                        config.getPathFiles().concat(config.getNameFileRoomJson()))
                .stream().filter(line -> !line.equals("[") && !line.equals("]") &&
                        !line.equals(CommonConstants.BREAK_LINE) &&
                        !line.trim().isEmpty() && !line.trim().isBlank())
                .collect(Collectors.toList());
        for(String line: contentInLine) {
            line = line.replace("{", "").replace("},", "").replace("}", "");
            StringTokenizer tokens = new StringTokenizer(line, ",");
            while(tokens.hasMoreElements()){
                int roomNumber = Integer.parseInt(this.escapeValue(tokens.nextToken().split(":")[1]));
                String typeRoomStr = this.escapeValue(tokens.nextToken().split(":")[1]);
                String stateStr = this.escapeValue(tokens.nextToken().split(":")[1]);
                String employeeName = this.escapeValue(tokens.nextToken().split(":")[1]);
                RoomType typeRoom = RoomType.valueOf(typeRoomStr);
                RoomStatus state = RoomStatus.valueOf(stateStr);
                this.listRooms.add(new HotelRoom(roomNumber, typeRoom, state, employeeName));
            }
        }
    }

    private String escapeValue(String value) {
        return value.replace("\"", "");
    }

    @Override
    public void dumpFile(ETypeFile eTypeFile) {
        dumpFileJSON();
    }
    private void dumpFileJSON() {
        String rutaArchivo = config.getPathFiles()
                .concat(config.getNameFileRoomJson());
        StringBuilder json = null;
        List<String> content = new ArrayList<String>();
        content.add(CommonConstants.OPENING_BRACKET);
        int contador = 0;
        int total = listRooms.size();
        for (HotelRoom h : this.listRooms) {
            json = new StringBuilder();
            json.append("{");
            json.append("  \"roomNumber\":\"").append(escape(String.valueOf(h.getRoomNumber()))).append("\",");
            json.append("  \"typeOfRoom\":\"").append(escape(h.getTypeRoom().name())).append("\","); //name() para obtener representacion textual
            json.append("  \"state\":\"").append(escape(h.getState().name())).append("\",");
            json.append("  \"employeeName\":\"").append(escape(h.getEmployeeName())).append("\"");
            json.append("}");

            contador++;
            if (contador < total) {
                json.append(",");
            }
            content.add(json.toString());
        }
        content.add("]");
        this.writer(rutaArchivo, content);
    }

    /** Escapa comillas dobles y backslashes para que el JSON no se rompa */
    private String escape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public List<HotelRoom> getListRooms() {
        return listRooms;
    }

    public void setListRooms(List<HotelRoom> listRooms) {
        this.listRooms = listRooms;
    }
}
