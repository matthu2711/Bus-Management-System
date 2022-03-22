import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class TripTable {

    private static LinkedList<Trip> trips;
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public TripTable() {
        trips = new LinkedList<>();
    }

    static public class Trip {
        public int tripID;
        public LinkedList<TripInfo> transfers = new LinkedList<>();

        public Trip(int tripID) {
            this.tripID = tripID;
        }

        public void addTripInfo(String[] info) {
            transfers.add(new TripInfo(info));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(ANSI_YELLOW).append("\nTrip ").append(tripID).append("""
                     contains the following stops. The below info is in the following format of:
                    Stop ID :: Arrival Time :: Depart Time :: Stop Sequence :: Distance Travelled
                    """).append(ANSI_RESET);
            for(TripInfo info : transfers)
                sb.append(info.toString());
            return sb.toString();
        }

        static public class TripInfo {

            public TripTime arrive, depart;
            public int stopID, stopSeq, stopHeadSign, pickupType, dropType;
            public double dist;

            //trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign
            //pickup_type,drop_off_type,shape_dist_traveled
            public TripInfo(String[] values) {
                this.stopSeq = -1;
                this.stopHeadSign = -1;
                this.pickupType = -1;
                this.dropType = -1;
                this.dist = 0;

                if (values.length == 9 && !values[8].equals("") && !values[8].equals(" "))
                    dist = Double.parseDouble(values[8]);
                if (!values[4].equals("") && !values[4].equals(" "))
                    stopSeq = Integer.parseInt(values[4]);
                if (!values[5].equals("") && !values[5].equals(" "))
                    stopHeadSign = Integer.parseInt(values[5]);
                if (!values[6].equals("") && !values[6].equals(" "))
                    pickupType = Integer.parseInt(values[6]);
                if (!values[7].equals("") && !values[7].equals(" "))
                    dropType = Integer.parseInt(values[7]);

                arrive = new TripTime(values[1]);
                depart = new TripTime(values[2]);
                stopID = Integer.parseInt(values[3]);
            }

            @Override
            public String toString() {
                return stopID + " :: " + arrive.toString() + " :: " + depart.toString() + " :: " + stopSeq + " :: " + dist + "\n";
            }

        }
    }

    static public class TripTime {

        public int hour;
        public int min;
        public int sec;

        public TripTime(String time) {
            String[] values = time.split(":");
            this.hour = Integer.parseInt(values[0].strip());
            this.min = Integer.parseInt(values[1].strip());
            this.sec = Integer.parseInt(values[2].strip());
        }

        @Override
        public String toString() {
            return hour + ":" + min + ":" + sec;
        }

    }

    public void readDate() throws FileNotFoundException {
        // Read each line and make a new trip per ID and add info for each line to the trip

        //trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign
        //pickup_type,drop_off_type,shape_dist_traveled
        Scanner sr = new Scanner(new File("stop_times.txt"));
        sr.nextLine();

        Trip newTrip = new Trip(9017927);

        while (sr.hasNextLine()) {
            String[] values = sr.nextLine().split(",");
            if (main.validTime(values[1]) && main.validTime(values[2])) {
                int tripID = Integer.parseInt(values[0]);
                if (tripID != newTrip.tripID) {
                    trips.add(newTrip);
                    newTrip = new Trip(tripID);
                }
                newTrip.addTripInfo(values);
            }
        }
    }

    public Iterable<Trip> timeQuery(String time) {
        TripTime query = new TripTime(time);
        LinkedList<Trip> result = new LinkedList<>();

        for (Trip trip : trips) {
            for (Trip.TripInfo info : trip.transfers)
                if (info.arrive.equals(query))
                    result.add(trip);
            break;

        }

        return result;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("The table contains the following trips:\n\n");
        for(Trip trip : trips)
            sb.append(trip.toString());
        return sb.toString();
    }
}
