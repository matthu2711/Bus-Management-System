import org.jetbrains.annotations.NotNull;

public class BusStop {

    int stopID;
    int code;
    String name;
    String destination;
    double latitude;
    double longitude;
    String zoneID;
    String url;
    int locType;
    int parent;

    public BusStop(int stopID, int code, String name, String destination, double latitude, double longitude, String zoneID, String url, int locType, int parent) {
        this.stopID = stopID;
        this.code = code;
        this.name = name;
        this.destination = destination;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoneID = zoneID;
        this.url = url;
        this.locType = locType;
        this.parent = parent;
    }

    public BusStop(@NotNull String stopInfo) {
        String[] values = stopInfo.split(",");
        this.stopID = Integer.parseInt(values[0]);
        this.code = Integer.parseInt(values[1]);
        this.name = values[2];
        this.destination = values[3];
        this.latitude = Double.parseDouble(values[4]);
        this.longitude = Double.parseDouble(values[5]);
        this.zoneID = values[6];
        this.url = values[7];
        this.locType = Integer.parseInt(values[8]);
        this.parent = ArrayIndexOutOfBoundsException ? Integer.parseInt(values[9]);
    }

    public int getStopID() {
        return stopID;
    }

    public void setStopID(int stopID) {
        this.stopID = stopID;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLocType() {
        return locType;
    }

    public void setLocType(int locType) {
        this.locType = locType;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }
}
