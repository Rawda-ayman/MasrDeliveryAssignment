import java.time.Duration;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RiderReportService
{
    public List<RiderStatistics> getRiderStatistics(Collection<Rider> riders, Collection<Order> orders)
    {
        Map<Rider, List<Order>> deliveredOrders = orders.stream().filter(order -> order.getStatus() == OrderStatus.DELIVERED).filter(order -> order.getRider() != null).filter(order -> order.getDeliveredAt() != null).collect(Collectors.groupingBy(Order::getRider));
        return riders.stream().map(rider -> {List<Order> riderOrders = deliveredOrders.getOrDefault(rider, List.of());double average = riderOrders.stream().mapToLong(order -> Duration.between(order.getPlacedAt(), order.getDeliveredAt()).toMinutes()).average().orElse(0.0);
            return new RiderStatistics(rider, riderOrders.size(), average);}).sorted(Comparator.comparingLong(RiderStatistics::getCompletedDeliveries).reversed()).toList();
    }
}