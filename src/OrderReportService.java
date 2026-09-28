import java.math.BigDecimal;
import java.util.*;

public class OrderReportService
{
    private final PricingService pricingService;
    private final DistrictDistanceService distanceService;

    public OrderReportService(PricingService pricingService, DistrictDistanceService distanceService)
    {
        this.pricingService = pricingService;
        this.distanceService = distanceService;
    }

    public OrderPrice calculateOrderPrice(Order order, Promo promotion)
    {
        double distance = distanceService.getDistance(order.getRestaurant().getDistrict(), order.getDeliveryAddress().getDistrict());

        return pricingService.calculate(order,promotion,distance);
    }

    private void validateRestaurant(Restaurant restaurant)
    {
        if (restaurant == null)
        {
            throw new IllegalArgumentException("Restaurant is required");
        }

        if (!restaurant.isOpen())
        {
            throw new RestaurantClosedException("Restaurant is closed");
        }
    }

    private void validateAddress(Customer customer, Address address)
    {
        if (customer == null)
        {
            throw new IllegalArgumentException("Customer is required");
        }

        if (address == null)
        {
            throw new IllegalArgumentException("Address is required");
        }

        boolean belongsToCustomer = customer.getAddresses().contains(address);

        if (!belongsToCustomer)
        {
            throw new IllegalArgumentException("Delivery address does not belong to customer");
        }
    }

    private void validateItems(List<OrderLine> orderLines)
    {
        if (orderLines == null || orderLines.isEmpty())
        {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        for (OrderLine line : orderLines)
        {
            MenuItem item = line.getMenuItem();

            if (!item.isAvailable())
            {
                throw new ItemUnavailableException("Item is unavailable: " + item.getName());
            }

            if (!item.hasEnoughStock(line.getQuantity()))
            {
                throw new StockShortageException("Not enough stock for: " + item.getName());
            }
        }
    }

    private void validatePromotion(Promo promotion, Order order)
    {
        if (promotion == null)
        {
            return;
        }

        BigDecimal subtotal = order.getOrderLines().stream().map(OrderLine::calculateTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        boolean firstTimeCustomer = order.getCustomer().getCompletedOrders() == 0;

        boolean applies = promotion.appliesTo(subtotal, order.getDeliveryAddress().getDistrict(), firstTimeCustomer);

        if (!applies)
        {
            throw new PromotionException("Promotion does not apply");
        }
    }

    private void validateWallet(Customer customer, BigDecimal total)
    {
        if (customer.getWalletBalance().compareTo(total) < 0)
        {
            throw new InsufficientWalletBalanceException("Insufficient wallet balance");
        }
    }

    private void reduceStock(List<OrderLine> orderLines)
    {
        for (OrderLine line : orderLines)
        {
            line.getMenuItem().reduceStock(line.getQuantity());
        }
    }

    public Order placeOrder(int orderId, Customer customer, Restaurant restaurant, Address address, List<OrderLine> orderLines, Promo promotion)
    {
        validateRestaurant(restaurant);

        validateAddress(customer, address);

        validateItems(orderLines);

        Order order = new Order(orderId, customer, restaurant, address, orderLines);

        validatePromotion(promotion, order);

        OrderPrice price = calculateOrderPrice(order, promotion);

        validateWallet(customer, price.getTotal());

        customer.deductFromWallet(price.getTotal());

        reduceStock(orderLines);
        return order;
    }
}