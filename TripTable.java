import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class TripTable {

    private static LinkedList<Trip> trips;

    public static void TripTable(String time){
        trips = new LinkedList<>();
    }

    static public class Trip {
        public int tripID;
        public static LinkedList<TripInfo> transfers;

        public Trip(int tripID) {
            this.tripID = tripID;
            transfers = new LinkedList<>();
        }

        public void addTripInfo(String[] info){
            transfers.add(new TripInfo(info));
        }

        static public class TripInfo {

            public TripTime arrive, depart;
            public int stopID, stopSeq, stopHeadSign, pickupType, dropType, distTrav;

            //trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign
            //pickup_type,drop_off_type,shape_dist_traveled
            public TripInfo(String[] values) {
                this.stopSeq = -1;
                this.stopHeadSign = -1;
                this.pickupType = -1;
                this.dropType = -1;
                this.distTrav = -1;

                if (values.length == 9 && !values[8].equals("") && !values[8].equals(" "))
                    distTrav = Integer.parseInt(values[8]);
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
                stopID = Integer.parseInt(values[0]);
            }

        }
    }

    static public class TripTime {

        public int hour;
        public int min;
        public int sec;

        public  TripTime(String time){
            String[] values = time.split(":");
            this.hour = Integer.parseInt(values[0].strip());
            this.min = Integer.parseInt(values[1].strip());
            this.sec = Integer.parseInt(values[2].strip());
        }

    }

    public void readDate(String fileName) throws FileNotFoundException {
        // Read each line and make a new trip per ID and add info for each line to the trip

        //trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign
        //pickup_type,drop_off_type,shape_dist_traveled
        Scanner sr = new Scanner(new File("stop_times.txt"));
        sr.nextLine();

        Trip newTrip = new Trip(9017927);

        while(sr.hasNextLine()){
            String[] values = sr.nextLine().split(",");
            if(main.validTime(values[1]) && main.validTime(values[2])) {
                int tripID = Integer.parseInt(values[0]);
                if (tripID != newTrip.tripID) {
                    trips.add(newTrip);
                    newTrip = new Trip(tripID);
                }
                newTrip.addTripInfo(values);
            }
        }
    }

}
