import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TimeReportService
{
    public Optional<Integer> getPeakOrderingHour(Collection<Order> orders)
    {
        return orders.stream().collect(Collectors.groupingBy(order -> order.getPlacedAt().getHour(), Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
    }
}