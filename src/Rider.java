public class Rider
{
    private final int id;
    private String name;
    private VehicleType vehicleType;
    private String currentDistrict;
    private boolean onDuty;
    private int completedDeliveries;
    private Order activeOrder;

    public Rider(int id, String name, VehicleType vehicleType, String currentDistrict)
    {
        if (id <= 0)
        {
            throw new IllegalArgumentException("Rider ID must be positive");
        }

        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Rider name is required");
        }

        if (vehicleType == null)
        {
            throw new IllegalArgumentException("Vehicle type is required");
        }

        if (currentDistrict == null || currentDistrict.trim().isEmpty())
        {
            throw new IllegalArgumentException("Current district is required");
        }

        this.id = id;
        this.name = name;
        this.vehicleType = vehicleType;
        this.currentDistrict = currentDistrict;
        this.onDuty = false;
        this.completedDeliveries = 0;
        this.activeOrder = null;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public VehicleType getVehicleType()
    {
        return vehicleType;
    }

    public String getCurrentDistrict()
    {
        return currentDistrict;
    }

    public boolean isAvailable() {
        return onDuty && activeOrder == null;
    }

    public void goOnDuty() {
        onDuty = true;
    }

    public boolean isOnDuty() {
        return onDuty;
    }

    public int getCompletedDeliveries()
    {
        return completedDeliveries;
    }

    public Order getActiveOrder()
    {
        return activeOrder;
    }

    public boolean hasActiveOrder()
    {
        return activeOrder != null;
    }

    public void goOffDuty() {
        if (activeOrder != null) {
            throw new IllegalStateException("Cannot go off duty while having an active order");
        }

        onDuty = false;
    }

    public void assignOrder(Order order)
    {

        if (!onDuty) {
            throw new RiderBusyException("Rider already has an active order");
        }

        if (activeOrder != null) {
            throw new RiderBusyException("Rider already has an active order");
        }

        if (order == null) {
            throw new RiderBusyException("Rider already has an active order");
        }

        this.activeOrder = order;
        this.onDuty = false;
    }

    public void completeDelivery()
    {
        if (activeOrder == null)
        {
            throw new IllegalStateException("Rider has no active order");
        }

        activeOrder = null;
        completedDeliveries++;
        onDuty = true;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof Rider))
        {
            return false;
        }

        Rider other = (Rider) obj;

        return id == other.id;
    }

    @Override
    public int hashCode()
    {
        return Integer.hashCode(id);
    }
}
