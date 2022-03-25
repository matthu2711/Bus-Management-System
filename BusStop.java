public class BusStop {

    private final int stopID;
    private final int code;
    private final String name;
    private final String destination;
    private final double latitude;
    private final double longitude;
    private final String zoneID;
    private final String url;
    private final int locType;
    private final String parent;


    // DS to contain all data of each BusStop which is saved to the TST
    public BusStop(int stopID, int code, String name, String destination, double latitude, double longitude,
                   String zoneID, String url, int locType, String parent) {
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

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public String getParent() {
        return parent;
    }

    public int getStopID() {
        return stopID;
    }

    // Format strings for printing containing all stop data
    @Override
    public String toString() {
        return "\nStop ID: " + stopID + "\nStop Code: " + code + "\nStop Name: " + name +
                "\nStop Destination: " + destination + "\nStop Latitude, Longitude: " + latitude + ", " + longitude +
                "\nStop Zone ID: " + zoneID + "\nStop Location Type: " + locType + "\n";
    }
}

