import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RevenueReportService
{
    public BigDecimal calculateRevenue(Collection<Order> orders, LocalDateTime from, LocalDateTime to)
    {
        if (orders == null)
        {
            throw new IllegalArgumentException("Orders cannot be null");
        }

        if (from == null || to == null)
        {
            throw new IllegalArgumentException("Date range is required");
        }

        if (from.isAfter(to))
        {
            throw new IllegalArgumentException("From date cannot be after to date");
        }

        return orders.stream().filter(order -> order.getStatus() == OrderStatus.DELIVERED).filter(order -> !order.getPlacedAt().isBefore(from) && !order.getPlacedAt().isAfter(to)).map(Order::getTotalPaid).filter(amount -> amount != null).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<Restaurant, BigDecimal> getTopFiveRestaurantsByRevenue(Collection<Order> orders, YearMonth month)
    {
        if (orders == null)
        {throw new IllegalArgumentException("Orders cannot be null");
        }

        if (month == null)
        {throw new IllegalArgumentException("Month is required");
        }

        return orders.stream().filter(order -> order.getStatus() == OrderStatus.DELIVERED).filter(order -> YearMonth.from(order.getPlacedAt()).equals(month)).filter(order -> order.getTotalPaid() != null).collect(Collectors.groupingBy(Order::getRestaurant, Collectors.reducing(BigDecimal.ZERO, Order::getTotalPaid, BigDecimal::add))).entrySet().stream().sorted(Map.Entry.<Restaurant, BigDecimal>comparingByValue().reversed()).limit(5).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

    public Map<String, BigDecimal> getAverageOrderValueByDistrict(Collection<Order> orders)
    {
        if (orders == null)
        {
            throw new IllegalArgumentException("Orders cannot be null");
        }

        return orders.stream().filter(order -> order.getStatus() == OrderStatus.DELIVERED).filter(order -> order.getTotalPaid() != null).collect(Collectors.groupingBy(order -> order.getDeliveryAddress().getDistrict(), Collectors.toList())).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().map(Order::getTotalPaid).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(entry.getValue().size()), 2, RoundingMode.HALF_UP), (a, b) -> a, LinkedHashMap::new));
    }
}