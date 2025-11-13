package persistence;

import constants.CommonConstants;
import enums.ETypeFile;
import interfaces.IActionsFile;
import model.HotelRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class HotelRoomPersistence extends FilePlain implements IActionsFile {
    private List<HotelRoom> listHotelRooms;

    public HotelRoomPersistence () {
        this.listHotelRooms = new ArrayList<HotelRoom>();
    }

    private HotelRoom findRoomById(int id) {
        for(HotelRoom hotelRoom: this.listHotelRooms) {
            if(hotelRoom.getRoomNumber() == id) {
                return hotelRoom;
            }
        }
        return null;
    }

    public Boolean addHotelRoom(HotelRoom hotelRoom) {
        if(Objects.isNull(this.findRoomById(hotelRoom.getRoomNumber()))) {
            this.listHotelRooms.add(hotelRoom);
            return true;
        }
        return false;
    }


    @Override
    public void loadFile(ETypeFile eTypeFile) {
        loadFileJSON();
    }

    private void loadFileJSON() {
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
                String typeOfRoom = this.escapeValue(tokens.nextToken().split(":")[1]);
                String state = this.escapeValue(tokens.nextToken().split(":")[1]);
                String employeeName = this.escapeValue(tokens.nextToken().split(":")[1]);
                this.listHotelRooms.add(new HotelRoom(roomNumber, typeOfRoom, state, employeeName));
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
        int total = listHotelRooms.size();
        for (HotelRoom h : this.listHotelRooms) {
            json = new StringBuilder();
            json.append("{");
            json.append("  \"roomNumber\":\"").append(escape(String.valueOf(h.getRoomNumber()))).append("\",");
            json.append("  \"typeOfRoom\":\"").append(escape(h.getTypeOfRoom())).append("\",");
            json.append("  \"state\":\"").append(escape(h.getEstado())).append("\",");
            json.append("  \"employeeName\":\"").append(escape(h.getEmployeeName())).append("\",");
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
}
