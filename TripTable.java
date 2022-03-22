import java.util.LinkedList;

public class TripTable {

    private static LinkedList<Trip> trips;

    public static void TripTable(String time){
        trips = new LinkedList<>();
    }

    static public class Trip {
        public int tripID;
        public LinkedList<TripInfo> transfers;

        public Trip(){
            transfers = new LinkedList<>();
        }

    }

    static public class TripInfo {

        public TripTime arrive, depart;
        public int stopID, stopSeq, stopHeadSign, pickupType, dropType,distTrav;

        public TripInfo(String info){

        }

    }

    static public class TripTime {

        public int hour;
        public int min;
        public int sec;

        public void TripTime(String time){
            String[] values = time.split(":");
            this.hour = Integer.parseInt(values[0].strip());
            this.min = Integer.parseInt(values[1].strip());
            this.sec = Integer.parseInt(values[2].strip());
        }

    }

    public void readDate(String fileName){

    }

}
