package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class Config {
    private static Config config;
    private String pathFiles;
    private String nameFileRoomJson;
    private String nameFileReservationJson;
    private String nameFileSer;
    private Properties properties;

    private Config(){
        this.properties = new Properties();
        try(FileInputStream entrada =
                    new FileInputStream("resources/config/appconfig.properties")){
            properties.load(entrada);
            this.pathFiles = properties.getProperty("path.files");
            this.nameFileSer = properties.getProperty("file.name.ser");
            this.nameFileRoomJson = properties.getProperty("file.room.json");
            this.nameFileReservationJson = properties.getProperty("file.reservation.json");
        }catch (IOException ex) {
            System.err.println("Error al cargar el archivo properties de configuracion: " + ex.getMessage());
        }
    }

    public static Config getInstance() {
        if (Objects.isNull(config)) {
            config = new Config();
        }
        return config;
    }

    public String getPathFiles() {
        return pathFiles;
    }

    public void setPathFiles(String pathFiles) {
        this.pathFiles = pathFiles;
    }

    public String getNameFileRoomJson() {
        return nameFileRoomJson;
    }

    public void setNameFileRoomJson(String nameFileRoomJson) {
        this.nameFileRoomJson = nameFileRoomJson;
    }

    public String getNameFileReservationJson() {
        return nameFileReservationJson;
    }

    public void setNameFileReservationJson(String nameFileReservationJson) {
        this.nameFileReservationJson = nameFileReservationJson;
    }

    public String getNameFileSer() {
        return nameFileSer;
    }

    public void setNameFileSer(String nameFileSer) {
        this.nameFileSer = nameFileSer;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
