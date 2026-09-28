import java.util.* ;
import java.util.function.Predicate;

public class CustomerService
{
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final RestaurantSearchService searchService;
    private final OrderReportService orderService;

    public CustomerService(RestaurantRepository restaurantRepository, OrderRepository orderRepository,RestaurantSearchService searchService, OrderReportService orderService) {
        if (restaurantRepository == null)
        {
            throw new IllegalArgumentException("Restaurant repository is required");
        }

        if (searchService == null) {
            throw new IllegalArgumentException("Search service is required");
        }

        if (orderService == null) {
            throw new IllegalArgumentException("Order service is required");
        }

        this.restaurantRepository = restaurantRepository;
        this.orderRepository = orderRepository ;
        this.searchService = searchService;
        this.orderService = orderService;
    }

    public List<Restaurant> browseRestaurants() {

        return restaurantRepository.findAllSortedByRating();
    }

    public List<Restaurant> searchRestaurants(Predicate<Restaurant> condition) {
        return searchService.search(restaurantRepository.getAll().values(), condition);
    }

    public void recordSearch(Customer customer, String search) {

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        customer.addSearch(search);
    }

    public List<MenuItem> viewMenu(Restaurant restaurant) {

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }

        return restaurant.getMenuItems();
    }

    public MenuItem findMenuItem(Restaurant restaurant, String itemId)
    {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }

        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("Item ID is required");
        }

        return restaurant.getMenuItems().stream().filter(item -> item.getId().equals(itemId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
    }

    public Order placeOrder(int orderId, Customer customer, Restaurant restaurant, Address address, List<OrderLine> orderLines, Promo promotion) {

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        return orderService.placeOrder(orderId, customer, restaurant, address, orderLines, promotion);
    }

    public OrderStatus trackOrder(Order order) {

        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        return order.getStatus();
    }

    public void cancelOrder(Customer customer, Order order) {

        if (customer == null || order == null) {
            throw new IllegalArgumentException("Customer and order are required");
        }

        if (!order.getCustomer().equals(customer)) {
            throw new IllegalArgumentException("Order does not belong to customer");
        }

        OrderStatus status = order.getStatus();

        if (status == OrderStatus.OUT_FOR_DELIVERY || status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {

            throw new InvalidOrderTransitionException("Order cannot be cancelled");
        }

        if (order.getTotalPaid() != null) {customer.addBalance(order.getTotalPaid());
        }

        order.changeStatus(OrderStatus.CANCELLED);
    }

    public List<Order> getOrderHistory(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        return orderRepository.getAll().values().stream().filter(order -> order.getCustomer().equals(customer)).sorted(Comparator.comparing(Order::getPlacedAt).reversed()).toList();
    }
}
