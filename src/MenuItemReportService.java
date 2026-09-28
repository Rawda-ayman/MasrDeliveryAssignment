import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MenuItemReportService
{
    public Optional<MenuItem> getMostFrequentlyOrderedItem(Collection<Order> orders)
    {
        return orders.stream().filter(order -> order.getStatus() == OrderStatus.DELIVERED).flatMap(order -> order.getOrderLines().stream()).collect(Collectors.groupingBy(OrderLine::getMenuItem, Collectors.reducing(BigDecimal.ZERO, OrderLine::getQuantity, BigDecimal::add))).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
    }
}
