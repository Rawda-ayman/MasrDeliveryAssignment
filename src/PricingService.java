import java.math.BigDecimal;
import java.math.RoundingMode;

public class PricingService
{
    private final DeliveryFeeCalculator deliveryFeeCalculator;

    public PricingService(DeliveryFeeCalculator deliveryFeeCalculator)
    {
        this.deliveryFeeCalculator = deliveryFeeCalculator;
    }

    public OrderPrice calculate(Order order, Promo promotion, double distanceKm)
    {
        BigDecimal subtotal = order.getOrderLines().stream().map(OrderLine::calculateTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal deliveryFee = deliveryFeeCalculator.calculate(distanceKm);

        deliveryFee = applyLoyaltyBenefit(deliveryFee, order.getCustomer().getLoyaltyTier());

        BigDecimal serviceFee = subtotal.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal promotionDiscount = BigDecimal.ZERO;

        if (promotion != null)
        {
            promotionDiscount = promotion.calculateDiscount(subtotal, deliveryFee);
        }

        BigDecimal total = subtotal.add(deliveryFee).add(serviceFee).subtract(promotionDiscount);

        if (total.compareTo(BigDecimal.ZERO) < 0)
        {
            total = BigDecimal.ZERO;
        }

        return new OrderPrice(subtotal, deliveryFee, serviceFee, promotionDiscount, total);
    }

    private BigDecimal applyLoyaltyBenefit(BigDecimal deliveryFee, LoyaltyTier tier)
    {
        return switch (tier)
        {
            case BRONZE -> deliveryFee;

            case SILVER -> deliveryFee.multiply(new BigDecimal("0.90"));
            case GOLD -> BigDecimal.ZERO;
        };
    }
}