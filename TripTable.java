import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// Data Structure used to categorize trips and their bus stops within - Used for searching by stopID
public class TripTable implements Iterable<Trip> {

    private static LinkedList<Trip> trips;


    public TripTable() throws FileNotFoundException {
        trips = new LinkedList<>();
        readDate();
    }

    // Formats arrival and destination time for easier printing, validating and comparison
    static public class TripTime implements Comparable<TripTime> {

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

        // Used to compare times
        @Override
        public int compareTo(TripTable.TripTime o) {
            if (this.hour == o.hour && this.min == o.min && this.sec == o.sec)
                return 0;
            else return -1;
        }
    }

    // Called to read in stop data and group trips via ID and store into our DS if they have a valid time
    public void readDate() throws FileNotFoundException {
        // Read each line and make a new trip per ID and add info for each line to the trip

        Scanner sr = new Scanner(new File("stop_times.txt"));
        sr.nextLine();

        Trip newTrip = new Trip(9017927);

        while (sr.hasNextLine()) {
            String[] values = sr.nextLine().split(",");
            if (main.validateTime(values[1]) && main.validateTime(values[2])) {
                int tripID = Integer.parseInt(values[0]);
                if (tripID != newTrip.tripID) {
                    trips.add(newTrip);
                    newTrip = new Trip(tripID);
                }
                newTrip.addTripInfo(values);
            }
        }
    }

    // Function used to search for all trips containg the passed arrival time parameter (and outputs error message if no trip exists)
    public void timeQuery(String time) {
        TripTime query = new TripTime(time);
        LinkedList<Trip> result = new LinkedList<>();

        for (Trip trip : trips) {
            for (TripInfo info : trip.transfers)
                if (info.arrive.compareTo(query) == 0)
                    result.add(trip);
        }

        // Sort added trips by TripID for outputting
        Collections.sort(result);

        if (result.size() == 0) {
            System.out.println(main.ANSI_RED + "No trips contain this arrival time of " + time + main.ANSI_RESET + "\n");
        } else {
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (Trip trip : result) {
                sb1.append(trip.tripID).append(", ");
                sb2.append(trip);
            }
            System.out.println(sb2);
            System.out.println(main.ANSI_CYAN + "The following " + (result.size() > 1 ? "trips all contain" : "trip contains") + " the arrival time of " + time +
                    " : \n(The information for " + (result.size() > 1 ? "each trip" : "the trip") + " has been outputted above)\n" + main.ANSI_RESET);
            System.out.println(sb1 + "\n");
        }
    }

    // Used for printing trip tables
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The table contains the following trips:\n\n");
        for (Trip trip : trips)
            sb.append(trip.toString());
        return sb.toString();
    }

    @Override
    public Iterator<Trip> iterator() {
        return trips.iterator();
    }
}

// Class used for individual trip, containing all stop/transfer info contained in that trip
class Trip implements Iterable<TripInfo>, Comparable<Trip> {
    public int tripID;
    public LinkedList<TripInfo> transfers = new LinkedList<>();

    public Trip(int tripID) {
        this.tripID = tripID;
    }

    public void addTripInfo(String[] info) {
        transfers.add(new TripInfo(info));
    }

    // Formats trip info to output all the details
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(main.ANSI_CYAN).append("\n\nTrip ").append(tripID).append("""
                 contains the following stops. The below info is in the following format of:
                Stop ID :: Arrival Time :: Depart Time :: Distance Travelled
                """).append(main.ANSI_RESET).append("\n");
        for (TripInfo info : transfers)
            sb.append(info.toString());
        return sb.toString();
    }


    @Override
    public Iterator<TripInfo> iterator() {
        return transfers.iterator();
    }

    @Override
    public int compareTo(@NotNull Trip o) {
        int compareID = o.tripID;
        return this.tripID - compareID;
    }
}

// Trip info class used to store data for each part of a trip
class TripInfo {

    public TripTable.TripTime arrive, depart;
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

        // Used to check if values are correct and present, if not uses a placeholder which is used for formatting
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

        arrive = new TripTable.TripTime(values[1]);
        depart = new TripTable.TripTime(values[2]);
        stopID = Integer.parseInt(values[3]);
    }

    // Formats string into columns
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(stopID);
        sb.append(" ".repeat(Math.max(0, 5 - String.valueOf(stopID).length())));
        sb.append(" :: ");

        TripInfoString(sb, arrive);

        TripInfoString(sb, depart);

        sb.append(dist).append("\n");
        return sb.toString();
    }

    // Formats the times for the string output
    private void TripInfoString(StringBuilder sb, TripTable.TripTime arrive) {
        if (arrive.hour <= 9)
            sb.append("0");
        sb.append(arrive.hour).append(":");
        if (arrive.min <= 9)
            sb.append("0");
        sb.append(arrive.min).append(":");
        if (arrive.sec <= 9)
            sb.append("0");
        sb.append(arrive.sec).append(" :: ");
    }
}
