
public class Appointment {
    private String date;
    private String time;
    private String clientName;
    private boolean isPaid;

    public Appointment(String date, String time, String clientName) {
        this.date = date;
        this.time = time;
        this.clientName = clientName;
        this.isPaid = false;
    }

    public Object getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getClientName(){
        return clientName;
    }

    public boolean isPaid() {

        return isPaid;
    }

    public void markAsPaid() {
        this.isPaid = true;
    }

}
