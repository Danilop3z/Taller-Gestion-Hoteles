package persistence;

import constants.CommonConstants;
import enums.ETypeFile;
import interfaces.IActionsFile;
import model.RoomReservation;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationPersistence extends FilePlain implements IActionsFile {
    private List<RoomReservation> listReservations;

    public ReservationPersistence(){
        this.listReservations = new ArrayList<RoomReservation>();
        }

        private RoomReservation findReservationByCode(String code) {
            for (RoomReservation rs : this.listReservations) {
                if (rs.getReservationCode().equals(code)) {
                    return rs;
                }
            }
            return null;
        }

        private boolean addRoomReservation(RoomReservation rs) {
        if (Objects.isNull(this.findReservationByCode(rs.getReservationCode()))){
            this.listReservations.add(rs);
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
                        config.getPathFiles().concat(config.getNameFileReservationJson()))
                .stream().filter(line -> !line.equals("[") && !line.equals("]") &&
                        !line.equals(CommonConstants.BREAK_LINE) &&
                        !line.trim().isEmpty() && !line.trim().isBlank())
                .collect(Collectors.toList());
        for(String line: contentInLine) {
            line = line.replace("{", "").replace("},", "").replace("}", "");
            StringTokenizer tokens = new StringTokenizer(line, ",");
            while(tokens.hasMoreElements()){
                String reservationCode = this.escapeValue(tokens.nextToken().split(":")[1]);
                String entryDateStr = this.escapeValue(tokens.nextToken().split(":")[1]);
                String departureDateStr = this.escapeValue(tokens.nextToken().split(":")[1]);
                String customerName = this.escapeValue(tokens.nextToken().split(":")[1]);
                int roomNumber = Integer.parseInt(this.escapeValue(tokens.nextToken().split(":")[1]));

                // Convertir fechas si existen
                LocalDate entryDate = (entryDateStr.isEmpty()) ? null : LocalDate.parse(entryDateStr);
                LocalDate departureDate = (departureDateStr.isEmpty()) ? null : LocalDate.parse(departureDateStr);

                this.listReservations.add(new RoomReservation(reservationCode, entryDate, departureDate, customerName, roomNumber));
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
                .concat(config.getNameFileReservationJson());
        StringBuilder json = null;
        List<String> content = new ArrayList<String>();
        content.add(CommonConstants.OPENING_BRACKET);
        int contador = 0;
        int total = listReservations.size();
        for (RoomReservation rs : this.listReservations) {
            json = new StringBuilder();
            json.append("{");
            json.append("  \"reservationCode\":\"").append(escape(String.valueOf(rs.getReservationCode()))).append("\",");
            json.append("  \"entryDate\":\"").append(escape(rs.getEntryDate().toString())).append("\",");
            json.append(" \"departureDate\":\"").append(escape(rs.getDepartureDate() != null ? rs.getDepartureDate().toString() : "")).append("\",");
            json.append("  \"customerName\":\"").append(escape(rs.getCustomerName())).append("\",");
            json.append("  \"roomNumber\":\"").append(escape(String.valueOf(rs.getRoomNumber()))).append("\"");
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
    public List<RoomReservation> loadAll() {
        loadFile(ETypeFile.JSON);
        return new ArrayList<>(listReservations);
    }
    public void saveAll(List<RoomReservation> reservations) {
        this.listReservations = new ArrayList<>(reservations);
        dumpFile(ETypeFile.JSON);
    }

}