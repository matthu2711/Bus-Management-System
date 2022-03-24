import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TripTable implements Iterable<Trip> {

    private static LinkedList<Trip> trips;


    public TripTable() throws FileNotFoundException {
        trips = new LinkedList<>();
        readDate();
    }

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

        @Override
        public int compareTo(TripTable.TripTime o) {
            if (this.hour == o.hour && this.min == o.min && this.sec == o.sec)
                return 0;
            else return -1;
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

    public void timeQuery(String time) {
        TripTime query = new TripTime(time);
        LinkedList<Trip> result = new LinkedList<>();

        for (Trip trip : trips) {
            for (TripInfo info : trip.transfers)
                if (info.arrive.compareTo(query) == 0)
                    result.add(trip);
        }

        Collections.sort(result);

        if(result.size() == 0){
            System.out.println("No trips contain this arrival time of " + time);
        }
        else {
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (Trip trip : result) {
                sb1.append(trip.tripID).append(", ");
                sb2.append(trip);
            }
            System.out.println(sb2);
            System.out.println(main.ANSI_GREEN + "The following " + (result.size() > 1 ? "trips all contain" : "trip contains") + " the arrival time of " + time +
                    " : (The information for " + (result.size() > 1 ? "each trip" : "the trip") + " has been outputted above)" + main.ANSI_RESET);
            System.out.println(sb1);
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("The table contains the following trips:\n\n");
        for(Trip trip : trips)
            sb.append(trip.toString());
        return sb.toString();
    }

    @Override
    public Iterator<Trip> iterator(){
        return trips.iterator();
    }
}

 class Trip implements Iterable<TripInfo>, Comparable<Trip>{
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
        sb.append(main.ANSI_YELLOW).append("\n\nTrip ").append(tripID).append("""
                     contains the following stops. The below info is in the following format of:
                    Stop ID :: Arrival Time :: Depart Time :: Distance Travelled
                    """).append(main.ANSI_RESET).append("\n");
        for(TripInfo info : transfers)
            sb.append(info.toString());
        return sb.toString();
    }

     @Override
     public Iterator<TripInfo> iterator(){
         return transfers.iterator();
     }

     @Override
     public int compareTo(@NotNull Trip o) {
         int compareID = o.tripID;
         return this.tripID - compareID;
     }
 }

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

     private void TripInfoString(StringBuilder sb, TripTable.TripTime arrive) {
         if(arrive.hour <= 9)
             sb.append("0");
         sb.append(arrive.hour).append(":");
         if(arrive.min <= 9)
             sb.append("0");
         sb.append(arrive.min).append(":");
         if(arrive.sec <= 9)
             sb.append("0");
         sb.append(arrive.sec).append(" :: ");
     }
 }
