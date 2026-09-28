import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestaurantReportService
{
    public List<Restaurant> getHighlyRatedRestaurants(Collection<Restaurant> restaurants, Collection<Order> orders) {
        if (restaurants == null)
        {
            throw new IllegalArgumentException("Restaurants cannot be null");
        }

        if (orders == null) {
            throw new IllegalArgumentException("Orders cannot be null");
        }

        Map<Restaurant, Long> completedOrdersCount = orders.stream().filter(order -> order.getStatus() == OrderStatus.DELIVERED).collect(Collectors.groupingBy(Order::getRestaurant, Collectors.counting()));

        return restaurants.stream().filter(restaurant -> restaurant.getRating() > 4.5).filter(restaurant -> completedOrdersCount.getOrDefault(restaurant, 0L) >= 20).toList();
    }
}