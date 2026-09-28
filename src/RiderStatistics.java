public class RiderStatistics
{
    private final Rider rider;
    private final long completedDeliveries;
    private final double averageDeliveryDurationMinutes;

    public RiderStatistics(Rider rider, long completedDeliveries, double averageDeliveryDurationMinutes)
    {
        this.rider = rider;
        this.completedDeliveries = completedDeliveries;
        this.averageDeliveryDurationMinutes = averageDeliveryDurationMinutes;
    }

    public Rider getRider()
    {
        return rider;
    }

    public long getCompletedDeliveries()
    {
        return completedDeliveries;
    }

    public double getAverageDeliveryDurationMinutes()
    {
        return averageDeliveryDurationMinutes;
    }
}
