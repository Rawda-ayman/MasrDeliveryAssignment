import java.math.BigDecimal;

public interface PromoStrategy
{
    BigDecimal calculateDiscount(BigDecimal subtotal, BigDecimal deliveryFee);
}
