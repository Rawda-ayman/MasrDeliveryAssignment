import java.math.BigDecimal;
import java.math.RoundingMode;

public class LoyaltyDeliveryFeeCalculator
{
    public BigDecimal calculate(BigDecimal baseDeliveryFee, Customer customer)
    {
        if (baseDeliveryFee == null || baseDeliveryFee.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("Delivery fee cannot be negative");
        }

        if (customer == null)
        {
            throw new IllegalArgumentException("Customer is required");
        }

        LoyaltyTier tier = customer.getLoyaltyTier();

        return switch (tier)
        {
            case BRONZE -> baseDeliveryFee;

            case SILVER -> baseDeliveryFee.multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP);

            case GOLD -> BigDecimal.ZERO;
        };
    }
}