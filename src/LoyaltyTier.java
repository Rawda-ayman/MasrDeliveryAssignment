public enum LoyaltyTier
{
    BRONZE,
    SILVER,
    GOLD;


    public static LoyaltyTier fromCompletedOrders(int completedOrders) {

        if (completedOrders < 0)
        {
            throw new IllegalArgumentException("Completed orders cannot be negative");
        }

        if (completedOrders >= 30)
        {
            return GOLD;
        }

        if (completedOrders >= 10)
        {
            return SILVER;
        }

        return BRONZE;
    }
}
