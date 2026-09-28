public class RiderService
{
    public void goOnDuty(Rider rider) {

        if (rider == null) {
            throw new IllegalArgumentException("Rider cannot be null");
        }

        rider.goOnDuty();
    }

    public void goOffDuty(Rider rider) {

        if (rider == null) {
            throw new IllegalArgumentException("Rider cannot be null");
        }

        rider.goOffDuty();
    }

    public boolean isAvailable(Rider rider) {

        if (rider == null) {
            throw new IllegalArgumentException("Rider cannot be null");
        }

        return rider.isAvailable();
    }

    public void assignOrder(Rider rider, Order order) {

        if (rider == null) {
            throw new IllegalArgumentException("Rider cannot be null");
        }

        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        if (!rider.isOnDuty()) {
            throw new IllegalStateException("Rider is off duty");
        }

        if (!rider.isAvailable()) {
            throw new RiderBusyException("Rider already has an active order");
        }

        if (order.getStatus() != OrderStatus.READY) {
            throw new IllegalStateException("Only READY orders can be assigned");
        }

        if (order.getRider() != null) {
            throw new IllegalStateException("Order already has a rider");
        }

        rider.assignOrder(order);
        order.assignRider(rider);
        order.changeStatus(OrderStatus.ASSIGNED);
    }

    public void pickUpOrder(Rider rider) {

        if (rider == null) {
            throw new IllegalArgumentException("Rider cannot be null");
        }

        Order order = rider.getActiveOrder();

        if (order == null) {
            throw new IllegalStateException("Rider has no active order");
        }

        order.changeStatus(OrderStatus.OUT_FOR_DELIVERY);
    }

    public void deliverOrder(Rider rider) {

        if (rider == null) {
            throw new IllegalArgumentException("Rider cannot be null");
        }

        Order order = rider.getActiveOrder();

        if (order == null) {
            throw new IllegalStateException("Rider has no active order");
        }

        if (order.getStatus() != OrderStatus.OUT_FOR_DELIVERY) {
            throw new IllegalStateException("Order must be OUT_FOR_DELIVERY");
        }

        order.changeStatus(OrderStatus.DELIVERED);

        order.getCustomer().completeOrder();

        rider.completeDelivery();
    }

    public Order getCurrentOrder(Rider rider) {

        if (rider == null) {
            throw new IllegalArgumentException("Rider cannot be null");
        }

        return rider.getActiveOrder();
    }

    public int getCompletedDeliveries(Rider rider) {

        if (rider == null) {
            throw new IllegalArgumentException("Rider cannot be null");
        }

        return rider.getCompletedDeliveries();
    }
}
