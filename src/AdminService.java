public class AdminService
{
    private final RestaurantRepository restaurantRepository;
    private final PromotionRepository promotionRepository;
    private final RevenueReportService revenueReportService;
    private final RestaurantReportService restaurantReportService;
    private final OrderReportService orderReportService;
    private final RiderReportService riderReportService;
    private final CustomerReportService customerReportService;
    private final TimeReportService timeReportService;
    private final CustomerActivityReport customerActivityReport;

    public AdminService(RestaurantRepository restaurantRepository, PromotionRepository promotionRepository, RevenueReportService revenueReportService, RestaurantReportService restaurantReportService, OrderReportService orderReportService, RiderReportService riderReportService, CustomerReportService customerReportService, TimeReportService timeReportService, CustomerActivityReport customerActivityReport)
    {
        this.restaurantRepository = restaurantRepository;
        this.promotionRepository = promotionRepository;
        this.revenueReportService = revenueReportService;
        this.restaurantReportService = restaurantReportService;
        this.orderReportService = orderReportService;
        this.riderReportService = riderReportService;
        this.customerReportService = customerReportService;
        this.timeReportService = timeReportService;
        this.customerActivityReport = customerActivityReport;
    }

    public int getRestaurantCount() {
        return restaurantRepository.getAll().size();
    }

    public int getPromotionCount() {
        return promotionRepository.getAll().size();
    }

    public void addRestaurant(Restaurant restaurant) {

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }

        restaurantRepository.add(restaurant);
    }

    public void removeRestaurant(int restaurantId) {

        if (restaurantId <= 0) {
            throw new IllegalArgumentException("Restaurant ID must be positive");
        }

        restaurantRepository.remove(restaurantId);
    }

    public void createPromotion(Promo promotion) {

        if (promotion == null) {
            throw new IllegalArgumentException("Promotion cannot be null");
        }

        promotionRepository.add(promotion);
    }
}