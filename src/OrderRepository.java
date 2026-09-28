import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OrderRepository
{
    private final Map<Integer, Order> orders;

    public OrderRepository()
    {
        orders = new HashMap<>();
    }

    public void add(Order order)
    {
        if (order == null)
        {
            throw new IllegalArgumentException(
                    "Order cannot be null"
            );
        }

        if (orders.containsKey(order.getId()))
        {
            throw new IllegalArgumentException(
                    "Order ID already exists"
            );
        }

        orders.put(
                order.getId(),
                order
        );
    }

    public Order findById(int id)
    {
        return orders.get(id);
    }

    public Map<Integer, Order> getAll()
    {
        return Collections.unmodifiableMap(orders);
    }
}