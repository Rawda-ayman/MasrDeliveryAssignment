import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.* ;

public class RestaurantService {

    public void acceptOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        order.changeStatus(OrderStatus.ACCEPTED);
    }

    public void startPreparing(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        order.changeStatus(OrderStatus.PREPARING);
    }

    public void markReady(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        order.changeStatus(OrderStatus.READY);
    }

    public void setItemAvailability(Restaurant restaurant, String itemId, boolean available) {

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }

        restaurant.setMenuItemAvailability(itemId, available);
    }

    public void adjustStock(Restaurant restaurant, String itemId, BigDecimal amount) {

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }

        restaurant.adjustItemStock(itemId, amount);
    }

    public void addMenuItem(Restaurant restaurant, MenuItem item) {

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }

        restaurant.addMenuItem(item);
    }

    public void removeMenuItem(Restaurant restaurant, String itemId) {

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }

        restaurant.removeMenuItem(itemId);
    }

    private final OrderRepository orderRepository;

    public RestaurantService(OrderRepository orderRepository) {
        if (orderRepository == null) {
            throw new IllegalArgumentException("Order repository is required");
        }

        this.orderRepository = orderRepository;
    }

    public List<Order> getTodayOrders(Restaurant restaurant) {

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }

        LocalDate today = LocalDate.now();

        return orderRepository.getAll().values().stream().filter(order -> order.getRestaurant().equals(restaurant)).filter(order -> order.getPlacedAt().toLocalDate().equals(today)).toList();
    }

    public BigDecimal getTodayRevenue(Restaurant restaurant) {

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }

        return getTodayOrders(restaurant).stream().filter(order -> order.getStatus() == OrderStatus.DELIVERED).map(Order::getTotalPaid).filter(total -> total != null).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}