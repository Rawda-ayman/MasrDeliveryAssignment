import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order
{
    private final int id;
    private final Customer customer;
    private final Restaurant restaurant;
    private final Address deliveryAddress;
    private final List<OrderLine> orderLines;
    private final LocalDateTime placedAt;
    private OrderStatus status;
    private Rider rider;
    private BigDecimal totalPaid;
    private LocalDateTime deliveredAt;

    public Order(int id, Customer customer, Restaurant restaurant, Address deliveryAddress, List<OrderLine> orderLines)
    {
        if (id <= 0)
        {
            throw new IllegalArgumentException("Order ID must be positive");
        }

        if (customer == null)
        {
            throw new IllegalArgumentException("Customer is required");
        }

        if (restaurant == null)
        {
            throw new IllegalArgumentException("Restaurant is required");
        }

        if (deliveryAddress == null)
        {
            throw new IllegalArgumentException("Delivery address is required");
        }

        if (orderLines == null || orderLines.isEmpty())
        {
            throw new IllegalArgumentException("Order must contain at least one line item");
        }

        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
        this.deliveryAddress = deliveryAddress;
        this.orderLines = new ArrayList<>(orderLines);
        this.placedAt = LocalDateTime.now();
        this.status = OrderStatus.PLACED;
        this.rider = null;
        this.totalPaid = null;
    }

    public int getId()
    {
        return id;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public Restaurant getRestaurant()
    {
        return restaurant;
    }

    public Address getDeliveryAddress()
    {
        return deliveryAddress;
    }

    public List<OrderLine> getOrderLines()
    {
        return Collections.unmodifiableList(orderLines);
    }

    public LocalDateTime getPlacedAt()
    {
        return placedAt;
    }

    public OrderStatus getStatus()
    {
        return status;
    }

    public Rider getRider()
    {
        return rider;
    }

    public BigDecimal getTotalPaid()
    {
        return totalPaid;
    }

    public LocalDateTime getDeliveredAt()
    {
        return deliveredAt;
    }

    public void setTotalPaid(BigDecimal totalPaid)
    {
        if (totalPaid == null || totalPaid.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("Invalid total");
        }

        this.totalPaid = totalPaid;
    }

    public void assignRider(Rider rider)
    {
        if (rider == null)
        {
            throw new IllegalArgumentException("Rider cannot be null");
        }

        if (this.rider != null)
        {
            throw new IllegalStateException("Order already has a rider");
        }

        this.rider = rider;
    }

    public void changeStatus(OrderStatus newStatus)
    {
        if (newStatus == null)
        {
            throw new InvalidOrderTransitionException("Illegal order status transition: " + this.status + " -> " + newStatus);
        }

        if (!isValidTransition(this.status, newStatus))
        {
            throw new InvalidOrderTransitionException("Illegal order status transition: " + this.status + " -> " + newStatus);
        }

        this.status = newStatus;

        if (newStatus == OrderStatus.DELIVERED)
        {
            this.deliveredAt = LocalDateTime.now();
        }
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next)
    {
        if (current == OrderStatus.CANCELLED) {
            return false;
        }

        if (current == OrderStatus.DELIVERED) {
            return false;
        }

        if (next == OrderStatus.CANCELLED) {
            return current != OrderStatus.OUT_FOR_DELIVERY;
        }

        return switch (current)
        {
            case PLACED -> next == OrderStatus.ACCEPTED;

            case ACCEPTED -> next == OrderStatus.PREPARING;

            case PREPARING -> next == OrderStatus.READY;

            case READY -> next == OrderStatus.ASSIGNED;

            case ASSIGNED -> next == OrderStatus.OUT_FOR_DELIVERY;

            case OUT_FOR_DELIVERY -> next == OrderStatus.DELIVERED;

            case DELIVERED, CANCELLED -> false;
        };
    }

    public void recordPayment(BigDecimal amount)
    {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("Payment amount cannot be negative");
        }

        if (this.totalPaid != null)
        {
            throw new IllegalStateException("Order has already been paid");
        }

        this.totalPaid = amount;
    }

    public void cancelOrder(Order order)
    {
        if (order == null)
        {
            throw new IllegalArgumentException("Order is required");
        }

        order.changeStatus(OrderStatus.CANCELLED);

        if (order.getTotalPaid() != null)
        {
            order.getCustomer().addToWallet(order.getTotalPaid());
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof Order))
        {
            return false;
        }

        Order other = (Order) obj;

        return id == other.id;
    }

    @Override
    public int hashCode()
    {
        return Integer.hashCode(id);
    }
}
