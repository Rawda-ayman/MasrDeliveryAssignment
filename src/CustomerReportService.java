import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class CustomerReportService
{
    public CustomerOrderHistory getOrderHistory(Customer customer, java.util.Collection<Order> orders)
    {
        if (customer == null)
        {
            throw new IllegalArgumentException("Customer is required");
        }

        List<Order> customerOrders = orders.stream().filter(order -> order.getCustomer().equals(customer)).sorted(Comparator.comparing(Order::getPlacedAt).reversed()).toList();

        BigDecimal totalSpent = customerOrders.stream().filter(order -> order.getTotalPaid() != null).map(Order::getTotalPaid).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CustomerOrderHistory(customerOrders, totalSpent);
    }
}
