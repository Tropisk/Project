import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookingSystem {
    private static Appointment appointment;
    private List<Appointment> appointments;
    private List<String> holidays;
    private String date;


    public BookingSystem() {
        this.appointments = new ArrayList<>();
        this.holidays = new ArrayList<>();
    }

    public void addHoliday(String date) {
        this.holidays.add(date);
    }

    public void removeHoliday(String date) {
        this.holidays.remove(date);
    }

    // ledig tider
    public List<String> getAvailableAppointments(String date) {
        List<String> availableAppointments = new ArrayList<>();

        String[] possibleTimes = {"10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"};

        List<String> bookedAppointments = getAppointmentsForDate(date);

        for (String time : possibleTimes) {
            if (!bookedAppointments.contains(time)) {
                availableAppointments.add(time);
            }
        }

        return availableAppointments;
    }

    private List<String> getAppointmentsForDate(String date) {
        this.date = date;
        List<String> appointmentsForDate = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date)) {
                appointmentsForDate.add(appointment.getTime());
            }
        }
        return appointmentsForDate;
    }

    // Ledig tider resten af ugen
    public List<String> getAvailableAppointmentsForWeek(String date) {
        List<String> availableAppointmentsForWeek = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        for (int i = 0; i < 6; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            if (isBusinessDay(currentDate.toString())) {
                List<String> availableAppointments = getAvailableAppointments(currentDate.toString());
                availableAppointmentsForWeek.add(currentDate.toString() + ": " + availableAppointments);
            }
        }
        return availableAppointmentsForWeek;
    }

    // Arbejdsdage
    public boolean isBusinessDay(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    //Booking
    public void bookAppointment(String date, String time, String clientName) {
        if (!isBusinessDay(date)) {
            System.out.println("Aftalen kunne ikke oprettes, da det ikke er en arbejdsdag.");
            return;
        }

        List<String> availableAppointments = getAvailableAppointments(date);
        if (!availableAppointments.contains(time)) {
            System.out.println("Tiden er ikke tilgængelig.");
            return;
        }
        Appointment newAppointment = new Appointment(date, time, clientName);
        appointments.add(newAppointment);

        System.out.println("Bookingen er blevet oprettet for " + clientName + " d. " + date + " kl. " + time);
    }

    //Slettet tider
    public void cancelAppointment(String date, String time) {
        Appointment appointmentToRemove = null;

        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date) && appointment.getTime().equals(time)) {
                appointmentToRemove = appointment;
                break;
            }
        }

        if (appointmentToRemove != null) {
            appointments.remove(appointmentToRemove);
            System.out.println("Frisøraftalen slettet for " + appointmentToRemove.getClientName() + " d. " + date + " kl. " + time);
        } else {
            System.out.println("Frisøraftalen ikke fundet på den angivne dato og tid.");
        }
    }

    // Login
    public boolean authenticate(String password) {
        return password.equals("hairyharry");
    }

    //betaling
    public void markAppointmentAsPaid(String date, String time) {
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date) && appointment.getTime().equals(time)) {
                appointment.markAsPaid();
                return;
            }
        }
        System.out.println("Booking d. " + date + " findes ikke ");
    }

    public boolean isAppointmentPaid(String date, String time) {
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date) && appointment.getTime().equals(time)) {
                return appointment.isPaid();
            }
        }
        return false;
    }

    //Programmet
    public static void main (String... args){
        BookingSystem bookingSystem; bookingSystem = new BookingSystem();
        Scanner scan = new Scanner(System.in);

        System.out.println("Velkommen til Denmarks bedste salon:");
        System.out.println("Harry's Salon");

        System.out.println("_____________________________________________________________________________");

        List<String> availableAppointments = bookingSystem.getAvailableAppointments("2023-11-03");
        System.out.println("Ledige tider d. November 3, 2023: " + availableAppointments);

        System.out.println("_____________________________________________________________________________");

        bookingSystem.bookAppointment("2023-11-02", "13:00", "Marc");
        bookingSystem.bookAppointment("2023-11-03", "14:00", "Kasper");
        bookingSystem.bookAppointment("2023-11-06", "12:00", "Laura");


        System.out.println("_____________________________________________________________________________");

        List<String> availableAppointmentsForWeek = bookingSystem.getAvailableAppointmentsForWeek("2023-11-01");
        System.out.println("Ledige tider i de kommende dage startende d. November 1, 2023: ");
        for (String appointments : availableAppointmentsForWeek) {
            System.out.println(appointments);
        }
        System.out.println("_____________________________________________________________________________");

        bookingSystem.cancelAppointment("2023-11-02", "13:00");

        System.out.println("_____________________________________________________________________________");

        boolean isAuthenticated = bookingSystem.authenticate("hairyharry");
        if (isAuthenticated) {
            System.out.println("Adgang Godkendt.");
        } else {
            System.out.println("Prøv igen.");
        }

        System.out.println("_____________________________________________________________________________");

        bookingSystem.markAppointmentAsPaid("2023-11-03", "14:00");
        bookingSystem.markAppointmentAsPaid("2023-11-02", "13:00");

        boolean isPaid = bookingSystem.isAppointmentPaid("2023-11-03", "14:00");
        if (isPaid) {
            System.out.println("Er betalt.");
        } else {
            System.out.println("Er ikke betalt endnu." + appointment.getDate() + appointment.getTime());
        }
    }
}