import java.math.BigDecimal;
import java.util.List;

public class CustomerOrderHistory
{
    private final List<Order> orders;
    private final BigDecimal totalSpent;

    public CustomerOrderHistory(List<Order> orders, BigDecimal totalSpent)
    {
        this.orders = List.copyOf(orders);
        this.totalSpent = totalSpent;
    }

    public List<Order> getOrders()
    {
        return orders;
    }

    public BigDecimal getTotalSpent()
    {
        return totalSpent;
    }
}
