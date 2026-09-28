import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;
import java.util.function.Predicate;

public class Main
{
    private static final Scanner scanner = new Scanner(System.in);

    private static CustomerRepository customerRepository;
    private static RestaurantRepository restaurantRepository;
    private static RiderRepository riderRepository;
    private static OrderRepository orderRepository;
    private static PromotionRepository promotionRepository;

    private static CustomerService customerService;
    private static RestaurantService restaurantService;
    private static RiderService riderService;
    private static AdminService adminService;
    private static OrderReportService orderReportService;

    public static void main(String[] args)
    {
        initializeApplication();
        boolean running = true;
        while (running)
        {
            System.out.println("================================");
            System.out.println("MASR DELIVERY");
            System.out.println("================================");
            System.out.println("0. Exit");
            System.out.println("1. Customer");
            System.out.println("2. Restaurant");
            System.out.println("3. Rider");
            System.out.println("4. Admin");
            System.out.println("Choose: ");

            int choice = scanner.nextInt();

            try
            {
                switch (choice)
                {
                    case 1 -> customerMenu();
                    case 2 -> restaurantMenu();
                    case 3 -> riderMenu();
                    case 4 -> adminMenu();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Masr Delivery closed.");
    }
    // APPLICATION INITIALIZATION
    private static void initializeApplication()
    {
        customerRepository = new CustomerRepository();
        restaurantRepository = new RestaurantRepository();
        riderRepository = new RiderRepository();
        orderRepository = new OrderRepository();
        promotionRepository = new PromotionRepository();

        Map<String, Double> distances = new HashMap<>();

        distances.put("nasr city-heliopolis", 6.0);
        distances.put("heliopolis-nasr city", 6.0);

        distances.put("nasr city-maadi", 15.0);
        distances.put("maadi-nasr city", 15.0);

        distances.put("heliopolis-maadi", 17.0);
        distances.put("maadi-heliopolis", 17.0);

        distances.put("nasr city-nasr city", 0.0);
        distances.put("heliopolis-heliopolis", 0.0);
        distances.put("maadi-maadi", 0.0);

        DistrictDistanceService distanceService = new DistrictDistanceService(distances);
        // Pricing
        DeliveryFeeCalculator deliveryFeeCalculator = new DeliveryFeeCalculator();

        PricingService pricingService = new PricingService(deliveryFeeCalculator);
        // Search
        RestaurantSearchService searchService = new RestaurantSearchService();
        // Order service
        orderReportService = new OrderReportService(pricingService, distanceService);
        // Customer service
        customerService = new CustomerService(restaurantRepository, orderRepository, searchService, orderReportService);
        // Restaurant service
        restaurantService = new RestaurantService(orderRepository);
        // Rider service
        riderService = new RiderService();
        // Reports
        RevenueReportService revenueReportService = new RevenueReportService();
        RestaurantReportService restaurantReportService = new RestaurantReportService();
        RiderReportService riderReportService = new RiderReportService();
        CustomerReportService customerReportService = new CustomerReportService();
        TimeReportService timeReportService = new TimeReportService();
        CustomerActivityReport customerActivityReport = new CustomerActivityReport();
        // Admin service
        adminService = new AdminService(restaurantRepository, promotionRepository, revenueReportService, restaurantReportService, orderReportService, riderReportService, customerReportService, timeReportService, customerActivityReport);
    }
    // CUSTOMER MENU
    private static void customerMenu()
    {
        Customer customer = chooseCustomer();
        if (customer == null)
        {
            return;
        }

        boolean running = true;

        while (running)
        {
            System.out.println("================================");
            System.out.println("CUSTOMER MENU");
            System.out.println("================================");
            System.out.println("Customer: " + customer.getName());
            System.out.println("Loyalty: " + customer.getLoyaltyTier());
            System.out.println("Wallet: " + customer.getWalletBalance());
            System.out.println("1. Browse Restaurants");
            System.out.println("2. Search Restaurants");
            System.out.println("3. View Restaurant Menu");
            System.out.println("4. Place Order");
            System.out.println("5. Track Order");
            System.out.println("6. Cancel Order");
            System.out.println("7. Order History");
            System.out.println("8. Recent Searches");
            System.out.println("9. Add Wallet Balance");
            System.out.println("0. Back");
            System.out.println("choose: ");

            int choice = scanner.nextInt();

            try
            {
                switch (choice)
                {
                    case 1 -> browseRestaurants();

                    case 2 -> searchRestaurants(customer);

                    case 3 -> viewRestaurantMenu();

                    case 4 -> placeOrder(customer);

                    case 5 -> trackOrder(customer);

                    case 6 -> cancelOrder(customer);

                    case 7 -> showCustomerHistory(customer);

                    case 8 -> showRecentSearches(customer);

                    case 9 -> addWalletBalance(customer);

                    case 0 -> running = false;

                    default -> System.out.println("Invalid choice.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    // CUSTOMER - BROWSE
    private static void browseRestaurants()
    {
        List<Restaurant> restaurants = customerService.browseRestaurants();

        if (restaurants.isEmpty())
        {
            System.out.println("No restaurants found.");
            return;
        }

        System.out.println("--- Restaurants ---");

        for (Restaurant restaurant : restaurants)
        {
            printRestaurant(restaurant);
        }
    }
    // CUSTOMER - SEARCH
    private static void searchRestaurants(Customer customer)
    {
        System.out.println("Search by:");
        System.out.println("1. District");
        System.out.println("2. Cuisine");
        System.out.println("3. Minimum Rating");
        System.out.println("4. Maximum Menu Price");
        System.out.println("Choose");
        int choice = scanner.nextInt();

        Predicate<Restaurant> condition;
        String searchDescription;

        switch (choice)
        {
            case 1 ->
            {
                String district = readString("District: ");

                condition = new RestaurantSearchService().byDistrict(district);

                searchDescription = "District: " + district;
            }

            case 2 ->
            {
                String cuisine = readString("Cuisine: ");

                condition = new RestaurantSearchService().byCuisine(cuisine);

                searchDescription = "Cuisine: " + cuisine;
            }

            case 3 ->
            {
                double rating = readDouble("Minimum rating: ");

                condition = new RestaurantSearchService().minimumRating(rating);

                searchDescription = "Minimum rating: " + rating;
            }

            case 4 ->
            {
                BigDecimal price = readBigDecimal("Maximum price: ");

                condition = new RestaurantSearchService().priceCeiling(price);

                searchDescription = "Maximum price: " + price;
            }

            default ->
            {
                System.out.println("Invalid choice.");
                return;
            }
        }

        List<Restaurant> results = customerService.searchRestaurants(condition);

        customerService.recordSearch(customer, searchDescription);

        System.out.println("--- Search Results ---");

        if (results.isEmpty())
        {
            System.out.println("No restaurants found.");
            return;
        }

        for (Restaurant restaurant : results)
        {
            printRestaurant(restaurant);
        }
    }
    // CUSTOMER - VIEW MENU
    private static void viewRestaurantMenu()
    {
        Restaurant restaurant = chooseRestaurant();

        if (restaurant == null)
        {
            return;
        }

        System.out.println("--- " + restaurant.getName() + " MENU ---");

        List<MenuItem> items = customerService.viewMenu(restaurant);
        if (items.isEmpty())
        {
            System.out.println("No menu items.");
            return;
        }

        for (MenuItem item : items)
        {
            printMenuItem(item);
        }
    }
    // CUSTOMER - PLACE ORDER
    private static void placeOrder(Customer customer)
    {
        Restaurant restaurant = chooseRestaurant();

        if (restaurant == null)
        {
            return;
        }

        if (!restaurant.isOpen())
        {
            System.out.println("Restaurant is closed.");
            return;
        }

        Address address = chooseAddress(customer);

        if (address == null)
        {
            return;
        }

        List<OrderLine> orderLines = new ArrayList<>();

        boolean adding = true;

        while (adding)
        {
            System.out.println("--- Menu ---");

            for (MenuItem item : restaurant.getMenuItems())
            {
                printMenuItem(item);
            }

            String itemId = readString("Enter item ID : ");

            if (itemId.equals("0"))
            {
                adding = false;
                continue;
            }

            MenuItem item = customerService.findMenuItem(restaurant, itemId);

            BigDecimal quantity = readBigDecimal("Quantity: ");

            orderLines.add(new OrderLine(item, quantity));

            System.out.println("Item added.");
        }

        if (orderLines.isEmpty())
        {
            System.out.println("Order cannot be empty.");
            return;
        }
        // Promotion
        Promo promotion = null;

        String promoCode = readString("Promotion code (press Enter to skip): ");

        if (!promoCode.isBlank())
        {
            promotion = promotionRepository.findByCode(promoCode);

            if (promotion == null)
            {
                System.out.println("Promotion not found.");
                return;
            }
        }

        int orderId = generateNextOrderId();
        Order order = customerService.placeOrder(orderId, customer, restaurant, address, orderLines, promotion);
        orderRepository.add(order);

        OrderPrice price = orderReportService.calculateOrderPrice(order, promotion);

        order.setTotalPaid(price.getTotal());

        System.out.println("================================");
        System.out.println("ORDER CREATED");
        System.out.println("================================");

        System.out.println("Order ID: " + order.getId());
        System.out.println("Subtotal: " + price.getSubtotal());
        System.out.println("Delivery: " + price.getDeliveryFee());
        System.out.println("Service: " + price.getServiceFee());
        System.out.println("Discount: " + price.getPromotionDiscount());
        System.out.println("Total: " + price.getTotal());
        System.out.println("Status: " + order.getStatus());
    }
    // CUSTOMER - TRACK ORDER
    private static void trackOrder(Customer customer)
    {
        Order order = chooseCustomerOrder(customer);

        if (order == null)
        {
            return;
        }

        System.out.println("Order ID: " + order.getId());
        System.out.println("Restaurant: " + order.getRestaurant().getName());

        System.out.println("Status: " + customerService.trackOrder(order));

        if (order.getRider() != null)
        {
            System.out.println("Rider: " + order.getRider().getName());
        }
    }
    // CUSTOMER - CANCEL ORDER
    private static void cancelOrder(Customer customer)
    {
        Order order = chooseCustomerOrder(customer);

        if (order == null)
        {
            return;
        }

        customerService.cancelOrder(customer, order);

        System.out.println("Order cancelled.");
    }
    // CUSTOMER - HISTORY
    private static void showCustomerHistory(Customer customer)
    {
        CustomerOrderHistory history = new CustomerReportService().getOrderHistory(customer, orderRepository.getAll().values());

        System.out.println("--- ORDER HISTORY ---");

        for (Order order : history.getOrders())
        {
            System.out.println("Order " + order.getId() + " | " + order.getStatus() + " | " + order.getTotalPaid());
        }

        System.out.println("Lifetime spent: " + history.getTotalSpent());
    }
    // CUSTOMER - RECENT SEARCHES
    private static void showRecentSearches(Customer customer)
    {
        System.out.println("--- Recent Searches ---");

        if (customer.getRecentSearches().isEmpty())
        {
            System.out.println("No searches.");
            return;
        }

        for (String search : customer.getRecentSearches())
        {
            System.out.println(search);
        }
    }
    // CUSTOMER - WALLET
    private static void addWalletBalance(Customer customer)
    {
        BigDecimal amount = readBigDecimal("Amount: ");

        customer.addBalance(amount);

        System.out.println("New balance: " + customer.getWalletBalance());
    }
    // RESTAURANT MENU
    private static void restaurantMenu()
    {
        Restaurant restaurant = chooseRestaurant();

        if (restaurant == null)
        {
            return;
        }

        boolean running = true;

        while (running)
        {
            System.out.println("================================");
            System.out.println("RESTAURANT MENU");
            System.out.println("================================");
            System.out.println("Restaurant: " + restaurant.getName());
            System.out.println("Open: " + restaurant.isOpen());
            System.out.println("1. Open Restaurant");
            System.out.println("2. Close Restaurant");
            System.out.println("3. View Today's Orders");
            System.out.println("4. Accept Order");
            System.out.println("5. Start Preparing");
            System.out.println("6. Mark Order Ready");
            System.out.println("7. Add Menu Item");
            System.out.println("8. Remove Menu Item");
            System.out.println("9. Change Item Availability");
            System.out.println("10. Adjust Stock");
            System.out.println("11. Today's Revenue");
            System.out.println("0. Back");

            int choice = readInt("Choose: ");

            try
            {
                switch (choice)
                {
                    case 1 ->
                    {
                        restaurant.setOpen(true);
                        System.out.println("Restaurant opened.");
                    }

                    case 2 ->
                    {
                        restaurant.setOpen(false);
                        System.out.println("Restaurant closed.");
                    }

                    case 3 -> showTodayOrders(restaurant);

                    case 4 -> acceptRestaurantOrder();

                    case 5 -> startPreparingOrder();

                    case 6 -> markOrderReady();

                    case 7 -> addMenuItem(restaurant);

                    case 8 -> removeMenuItem(restaurant);

                    case 9 -> changeItemAvailability(restaurant);

                    case 10 -> adjustStock(restaurant);

                    case 11 -> showTodayRevenue(restaurant);

                    case 0 -> running = false;

                    default -> System.out.println("Invalid choice.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    // RESTAURANT - TODAY ORDERS
    private static void showTodayOrders(Restaurant restaurant)
    {
        List<Order> orders = restaurantService.getTodayOrders(restaurant);

        if (orders.isEmpty())
        {
            System.out.println("No orders today.");
            return;
        }

        for (Order order : orders)
        {
            System.out.println("Order " + order.getId() + " | " + order.getStatus());
        }
    }
    // RESTAURANT - ACCEPT
    private static void acceptRestaurantOrder()
    {
        Order order = chooseOrder();

        restaurantService.acceptOrder(order);

        System.out.println("Order accepted.");
    }
    // RESTAURANT - PREPARING
    private static void startPreparingOrder()
    {
        Order order = chooseOrder();

        restaurantService.startPreparing(order);

        System.out.println("Order is preparing.");
    }
    // RESTAURANT - READY
    private static void markOrderReady()
    {
        Order order = chooseOrder();

        restaurantService.markReady(order);

        System.out.println("Order is ready.");
    }
    // RESTAURANT - ADD MENU ITEM
    private static void addMenuItem(Restaurant restaurant)
    {
        System.out.println("--- Add Menu Item ---");

        System.out.println("1. Standard");
        System.out.println("2. Weighted");
        System.out.println("3. Combo");

        int type = readInt("Type: ");

        String id = readString("Item ID: ");

        String name = readString("Name: ");

        String category = readString("Category: ");

        int preparationTime = readInt("Preparation time: ");

        boolean available = readBoolean("Available? (true/false): ");

        BigDecimal stock = readBigDecimal("Stock: ");

        MenuItem item;

        switch (type)
        {
            case 1 ->
            {
                BigDecimal price = readBigDecimal("Price: ");

                item = new StandardItem(id, name, price, category, preparationTime, available, stock);
            }

            case 2 ->
            {
                BigDecimal pricePerKg = readBigDecimal("Price per KG: ");

                item = new WeightedItem(id, name, pricePerKg, category, preparationTime, available, stock);
            }

            case 3 ->
            {
                System.out.println("Enter existing item IDs separated by comma:");
                String ids = readString("IDs: ");

                List<MenuItem> comboItems = new ArrayList<>();

                for (String itemId : ids.split(","))
                {
                    String cleanId = itemId.trim();

                    MenuItem comboItem = customerService.findMenuItem(restaurant, cleanId);

                    comboItems.add(comboItem);
                }

                BigDecimal discount = readBigDecimal("Discount percentage: ");

                item = new ComboItem(id, name, category, preparationTime, available, stock, comboItems, discount);
            }

            default ->
            {
                System.out.println("Invalid type.");
                return;
            }
        }

        restaurantService.addMenuItem(restaurant, item);

        System.out.println("Menu item added successfully.");
    }
    // RESTAURANT - REMOVE ITEM
    private static void removeMenuItem(Restaurant restaurant)
    {
        String itemId = readString("Item ID: ");

        restaurantService.removeMenuItem(restaurant, itemId);

        System.out.println("Item removed.");
    }
    // RESTAURANT - AVAILABILITY
    private static void changeItemAvailability(Restaurant restaurant)
    {
        String itemId = readString("Item ID: ");
        boolean available = readBoolean("Available? (true/false): ");

        restaurantService.setItemAvailability(restaurant, itemId, available);

        System.out.println("Availability updated.");
    }
    // RESTAURANT - STOCK
    private static void adjustStock(Restaurant restaurant)
    {
        String itemId = readString("Item ID: ");

        BigDecimal amount = readBigDecimal("Stock adjustment: ");

        restaurantService.adjustStock(restaurant, itemId, amount);

        System.out.println("Stock updated.");
    }
    // RESTAURANT - REVENUE
    private static void showTodayRevenue(Restaurant restaurant)
    {
        BigDecimal revenue = restaurantService.getTodayRevenue(restaurant);

        System.out.println("Today's revenue: " + revenue);
    }
    // RIDER MENU
    private static void riderMenu()
    {
        Rider rider = chooseRider();

        if (rider == null)
        {
            return;
        }
        boolean running = true;

        while (running)
        {
            System.out.println("================================");
            System.out.println("RIDER MENU");
            System.out.println("================================");
            System.out.println("Rider: " + rider.getName());
            System.out.println("On duty: " + rider.isOnDuty());
            System.out.println("Available: " + rider.isAvailable());
            System.out.println("1. Go On Duty");
            System.out.println("2. Go Off Duty");
            System.out.println("3. View Current Order");
            System.out.println("4. Assign Order");
            System.out.println("5. Pick Up Order");
            System.out.println("6. Deliver Order");
            System.out.println("7. View Statistics");
            System.out.println("0. Back");

            int choice = readInt("Choose: ");

            try
            {
                switch (choice)
                {
                    case 1 ->
                    {
                        riderService.goOnDuty(rider);
                        System.out.println("Rider is on duty.");
                    }

                    case 2 ->
                    {
                        riderService.goOffDuty(rider);
                        System.out.println("Rider is off duty.");
                    }

                    case 3 -> showCurrentOrder(rider);

                    case 4 -> assignOrderToRider(rider);

                    case 5 ->
                    {
                        riderService.pickUpOrder(rider);System.out.println("Order picked up.");
                    }

                    case 6 ->
                    {
                        riderService.deliverOrder(rider);System.out.println("Order delivered.");
                    }

                    case 7 -> showRiderStatistics(rider);

                    case 0 -> running = false;

                    default -> System.out.println("Invalid choice.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    // RIDER - CURRENT ORDER
    private static void showCurrentOrder(Rider rider)
    {
        Order order = riderService.getCurrentOrder(rider);

        if (order == null)
        {
            System.out.println("No active order.");
            return;
        }

        System.out.println("Order ID: " + order.getId());

        System.out.println("Status: " + order.getStatus());
    }
    // RIDER - ASSIGN
    private static void assignOrderToRider(
            Rider rider)
    {
        Order order = chooseOrder();

        riderService.assignOrder(rider, order);

        System.out.println("Order assigned.");
    }
    // RIDER - STATISTICS
    private static void showRiderStatistics(Rider rider)
    {
        RiderStatistics statistics = new RiderReportService().getRiderStatistics(List.of(rider), orderRepository.getAll().values()).get(0);

        System.out.println("Completed deliveries: " + statistics.getCompletedDeliveries());

        System.out.println("Average delivery duration: " + statistics.getAverageDeliveryDurationMinutes() + " minutes");
    }
    // ADMIN MENU
    private static void adminMenu()
    {
        boolean running = true;

        while (running)
        {
            System.out.println("================================");
            System.out.println("ADMIN MENU");
            System.out.println("================================");
            System.out.println("1. View Restaurant Count");
            System.out.println("2. View Promotion Count");
            System.out.println("3. Add Restaurant");
            System.out.println("4. Remove Restaurant");
            System.out.println("5. Create Promotion");
            System.out.println("6. Revenue Report");
            System.out.println("7. Top Restaurants");
            System.out.println("8. Average Order Value");
            System.out.println("9. Highly Rated Restaurants");
            System.out.println("10. Rider Statistics");
            System.out.println("11. Customer History");
            System.out.println("12. Peak Ordering Hour");
            System.out.println("13. Inactive Customers");
            System.out.println("0. Back");

            int choice = readInt("Choose: ");

            try
            {
                switch (choice)
                {
                    case 1 -> System.out.println("Restaurants: " + adminService.getRestaurantCount());

                    case 2 -> System.out.println("Promotions: " + adminService.getPromotionCount());

                    case 3 -> addRestaurant();

                    case 4 -> removeRestaurant();

                    case 5 -> createPromotion();

                    case 6 -> revenueReport();

                    case 7 -> topRestaurants();

                    case 8 -> averageOrderValue();

                    case 9 -> highlyRatedRestaurants();

                    case 10 -> allRiderStatistics();

                    case 11 -> adminCustomerHistory();

                    case 12 -> peakOrderingHour();

                    case 13 -> inactiveCustomers();

                    case 0 -> running = false;

                    default -> System.out.println("Invalid choice.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    // ADMIN - ADD RESTAURANT
    private static void addRestaurant()
    {
        int id = readInt("Restaurant ID: ");

        String name = readString("Name: ");

        String district = readString("District: ");

        String cuisinesInput = readString("Cuisines separated by comma: ");

        Set<String> cuisines = Set.of(Arrays.stream(cuisinesInput.split(",")).map(String::trim).filter(s -> !s.isBlank()).toArray(String[]::new));

        double rating = readDouble("Rating: ");

        boolean open = readBoolean("Open? (true/false): ");

        Restaurant restaurant = new Restaurant(id, name, district, cuisines, rating, open);

        adminService.addRestaurant(restaurant);

        System.out.println("Restaurant added.");
    }
    // ADMIN - REMOVE RESTAURANT
    private static void removeRestaurant()
    {
        int id = readInt("Restaurant ID: ");

        adminService.removeRestaurant(id);

        System.out.println("Restaurant removed.");
    }
    // ADMIN - CREATE PROMOTION
    private static void createPromotion()
    {
        System.out.println("--- Create Promotion ---");
        System.out.println("1. Percentage");
        System.out.println("2. Fixed Amount");
        System.out.println("3. Free Delivery");

        int type = readInt("Type: ");

        String code = readString("Code: ");

        BigDecimal minimumSubtotal = readOptionalBigDecimal("Minimum subtotal: ");

        String expiryInput = readString("Expiry date YYYY-MM-DD : ");

        LocalDate expiryDate = null;

        if (!expiryInput.isBlank())
        {
            expiryDate = LocalDate.parse(expiryInput);
        }

        String district = readString("Restricted district (Enter for none): ");

        if (district.isBlank())
        {
            district = null;
        }

        boolean firstTimeOnly = readBoolean("First-time customer only? (true/false): ");

        Promo promotion;

        switch (type)
        {
            case 1 ->
            {
                BigDecimal percentage = readBigDecimal("Percentage: ");

                BigDecimal maximumDiscount = readOptionalBigDecimal("Maximum discount (Enter for none): ");

                promotion = new PercentagePromo(code, percentage, maximumDiscount, minimumSubtotal, expiryDate, district, firstTimeOnly);
            }

            case 2 ->
            {
                BigDecimal amount = readBigDecimal("Fixed amount: ");

                promotion = new FixedAmountPromo(code, amount, minimumSubtotal, expiryDate, district, firstTimeOnly);
            }

            case 3 ->
            {
                promotion = new FreeDeliveryPromo(code, minimumSubtotal, expiryDate, district, firstTimeOnly);
            }

            default ->
            {
                System.out.println("Invalid promotion type.");
                return;
            }
        }

        adminService.createPromotion(promotion);

        System.out.println("Promotion created successfully.");
    }
    // ADMIN - REVENUE
    private static void revenueReport()
    {
        LocalDateTime from = LocalDateTime.parse(readString("From (YYYY-MM-DDTHH:MM): "));

        LocalDateTime to = LocalDateTime.parse(readString("To (YYYY-MM-DDTHH:MM): "));

        RevenueReportService service = new RevenueReportService();

        BigDecimal revenue = service.calculateRevenue(orderRepository.getAll().values(), from, to);

        System.out.println("Revenue: " + revenue);
    }
    // ADMIN - TOP RESTAURANTS
    private static void topRestaurants()
    {
        String monthInput = readString("Month YYYY-MM: ");

        YearMonth month = YearMonth.parse(monthInput);

        RevenueReportService service = new RevenueReportService();

        Map<Restaurant, BigDecimal> result = service.getTopFiveRestaurantsByRevenue(orderRepository.getAll().values(), month);

        for (Map.Entry<Restaurant, BigDecimal> entry : result.entrySet())
        {
            System.out.println(entry.getKey().getName() + " -> " + entry.getValue());
        }
    }
    // ADMIN - AVERAGE ORDER VALUE
    private static void averageOrderValue()
    {
        RevenueReportService service = new RevenueReportService();

        Map<String, BigDecimal> result = service.getAverageOrderValueByDistrict(orderRepository.getAll().values());

        for (Map.Entry<String, BigDecimal> entry : result.entrySet())
        {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
    // ADMIN - HIGHLY RATED
    private static void highlyRatedRestaurants()
    {
        RestaurantReportService service = new RestaurantReportService();

        List<Restaurant> result = service.getHighlyRatedRestaurants(restaurantRepository.getAll().values(), orderRepository.getAll().values());

        for (Restaurant restaurant : result)
        {
            System.out.println(restaurant.getName() + " | Rating: " + restaurant.getRating());
        }
    }
    // ADMIN - RIDER REPORT
    private static void allRiderStatistics()
    {
        RiderReportService service = new RiderReportService();

        List<RiderStatistics> result = service.getRiderStatistics(riderRepository.getAll().values(), orderRepository.getAll().values());

        for (RiderStatistics statistics : result)
        {
            System.out.println(statistics.getRider().getName() + " | Deliveries: " + statistics.getCompletedDeliveries() + " | Average: " + statistics.getAverageDeliveryDurationMinutes() + " minutes");
        }
    }
    // ADMIN - CUSTOMER HISTORY
    private static void adminCustomerHistory()
    {
        Customer customer = chooseCustomer();

        if (customer == null)
        {
            return;
        }

        CustomerReportService service = new CustomerReportService();
        CustomerOrderHistory history = service.getOrderHistory(customer, orderRepository.getAll().values());
        System.out.println("Total spent: " + history.getTotalSpent());

        for (Order order : history.getOrders())
        {
            System.out.println("Order " + order.getId() + " | " + order.getStatus());
        }
    }
    // ADMIN - PEAK HOUR
    private static void peakOrderingHour()
    {
        TimeReportService service = new TimeReportService();
        service.getPeakOrderingHour(orderRepository.getAll().values()).ifPresentOrElse(hour -> System.out.println("Peak ordering hour: " + hour), () -> System.out.println("No orders found."));
    }
    // ADMIN - INACTIVE CUSTOMERS
    private static void inactiveCustomers()
    {
        CustomerActivityReport service = new CustomerActivityReport();

        List<Customer> inactive = service.getInactiveCustomers(customerRepository.getAll().values(), orderRepository.getAll().values());
        if (inactive.isEmpty())
        {
            System.out.println("No inactive customers.");
            return;
        }

        for (Customer customer : inactive)
        {
            System.out.println(customer.getId() + " | " + customer.getName());
        }
    }
    // HELPERS - CHOOSE CUSTOMER
    private static Customer chooseCustomer()
    {
        if (customerRepository.getAll().isEmpty())
        {
            System.out.println("No customers available.");
            return null;
        }

        System.out.println("--- Customers ---");

        for (Customer customer : customerRepository.getAll().values())
        {
            System.out.println(customer.getId() + " - " + customer.getName());
        }

        System.out.println("Customer id : ");
        int id = scanner.nextInt();

        Customer customer = customerRepository.findById(id);

        if (customer == null)
        {
            System.out.println("Customer not found.");
        }

        return customer;
    }
    // HELPERS - CHOOSE RESTAURANT
    private static Restaurant chooseRestaurant()
    {
        if (restaurantRepository.getAll().isEmpty())
        {
            System.out.println("No restaurants available.");
            return null;
        }

        System.out.println("--- Restaurants ---");

        for (Restaurant restaurant : restaurantRepository.getAll().values())
        {
            System.out.println(restaurant.getId() + " - " + restaurant.getName());
        }

        System.out.println("Restaurant id : ");
        int id = scanner.nextInt();

        Restaurant restaurant = restaurantRepository.findById(id);

        if (restaurant == null)
        {
            System.out.println("Restaurant not found.");
        }

        return restaurant;
    }
    // HELPERS - CHOOSE RIDER
    private static Rider chooseRider()
    {
        if (riderRepository.getAll().isEmpty())
        {
            System.out.println("No riders available.");
            return null;
        }

        System.out.println("--- Riders ---");

        for (Rider rider : riderRepository.getAll().values())
        {
            System.out.println(rider.getId() + " - " + rider.getName());
        }

        System.out.println("Order id : ");
        int id = scanner.nextInt();

        Rider rider = riderRepository.findById(id);

        if (rider == null)
        {
            System.out.println("Rider not found.");
        }

        return rider;
    }
    // HELPERS - CHOOSE ORDER
    private static Order chooseOrder()
    {
        if (orderRepository.getAll().isEmpty())
        {
            System.out.println("No orders available.");
            return null;
        }

        System.out.println("--- Orders ---");

        for (Order order : orderRepository.getAll().values())
        {
            System.out.println(order.getId() + " | " + order.getStatus() + " | " + order.getRestaurant().getName());
        }

        System.out.println("Order id : ");
        int id = scanner.nextInt();

        Order order = orderRepository.findById(id);

        if (order == null)
        {
            System.out.println("Order not found.");
        }

        return order;
    }
    // HELPERS - CUSTOMER ORDER
    private static Order chooseCustomerOrder(Customer customer)
    {
        List<Order> orders = orderRepository.getAll().values().stream().filter(order -> order.getCustomer().equals(customer)).toList();

        if (orders.isEmpty())
        {
            System.out.println("No orders found.");
            return null;
        }

        for (Order order : orders)
        {
            System.out.println(order.getId() + " | " + order.getStatus());
        }

        System.out.println("Order id : ");
        int id = scanner.nextInt();

        Order order = orderRepository.findById(id);

        if (order == null ||
                !order.getCustomer().equals(customer))
        {
            System.out.println("Invalid order.");
            return null;
        }

        return order;
    }
    // HELPERS - ADDRESS
    private static Address chooseAddress(Customer customer)
    {
        if (customer.getAddresses().isEmpty())
        {
            System.out.println("Customer has no saved addresses.");
            return null;
        }

        System.out.println("--- Addresses ---");

        List<Address> addresses = customer.getAddresses();

        for (int i = 0; i < addresses.size(); i++)
        {
            Address address = addresses.get(i);

            System.out.println((i + 1) + ". " + address.getDistrict() + " - " + address.getDetails());
        }
        System.out.println("Choose Address : ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > addresses.size())
        {
            System.out.println("Invalid address.");
            return null;
        }
        return addresses.get(choice - 1);
    }
    // HELPERS - PRINT RESTAURANT
    private static void printRestaurant(Restaurant restaurant)
    {
        System.out.println(restaurant.getId() + " | " + restaurant.getName() + " | District: " + restaurant.getDistrict() + " | Rating: " + restaurant.getRating() + " | Open: " + restaurant.isOpen());
    }
    // HELPERS - PRINT MENU ITEM
    private static void printMenuItem(MenuItem item)
    {
        System.out.println(item.getId() + " | " + item.getName() + " | Price: " + item.getPrice() + " | Category: " + item.getCategory() + " | Stock: " + item.getStock() + " | Available: " + item.isAvailable());
    }
    // HELPERS - INPUT
    private static String readString(String message)
    {
        System.out.print(message);
        return scanner.nextLine().trim();
    }
    private static int readInt(String message)
    {
        while (true)
        {
            try
            {
                return Integer.parseInt(readString(message));
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a valid integer.");
            }
        }
    }
    private static double readDouble(String message)
    {
        while (true)
        {
            try
            {
                return Double.parseDouble(readString(message));
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    private static BigDecimal readBigDecimal(String message)
    {
        while (true)
        {
            try
            {
                return new BigDecimal(readString(message));
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a valid amount.");
            }
        }
    }
    private static BigDecimal readOptionalBigDecimal(String message)
    {
        while (true)
        {
            String input = readString(message);

            if (input.isBlank())
            {
                return null;
            }

            try
            {
                return new BigDecimal(input);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a valid amount or press Enter.");
            }
        }
    }
    private static boolean readBoolean(String message)
    {
        while (true)
        {
            String input = readString(message);

            if (input.equalsIgnoreCase("true"))
            {
                return true;
            }

            if (input.equalsIgnoreCase("false"))
            {
                return false;
            }

            System.out.println("Please enter true or false.");
        }
    }
    // ORDER ID GENERATOR
    private static int generateNextOrderId()
    {
        if (orderRepository.getAll().isEmpty())
        {
            return 1;
        }

        return orderRepository.getAll().keySet().stream().max(Integer::compareTo).orElse(0) + 1;
    }
}