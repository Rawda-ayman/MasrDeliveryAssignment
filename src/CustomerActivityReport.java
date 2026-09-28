import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerActivityReport
{
    public List<Customer> getInactiveCustomers(Collection<Customer> customers, Collection<Order> orders)
    {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        Map<Customer, LocalDateTime> lastOrder = orders.stream().collect(Collectors.groupingBy(Order::getCustomer, Collectors.mapping(Order::getPlacedAt, Collectors.maxBy(java.util.Comparator.naturalOrder())))).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().orElse(null)));
        return customers.stream().filter(customer -> {LocalDateTime last = lastOrder.get(customer);return last == null || last.isBefore(cutoff);}).toList();
    }
}
