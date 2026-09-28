import java.math.BigDecimal;
import java.time.LocalDate;

public class FreeDeliveryPromo extends Promo
{
    public FreeDeliveryPromo(String code, BigDecimal minimumSubtotal, LocalDate expiryDate, String restrictedDistrict, boolean firstTimeOnly)
    {
        super(code, minimumSubtotal, expiryDate, restrictedDistrict, firstTimeOnly);
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal subtotal, BigDecimal deliveryFee)
    {
        return deliveryFee;
    }
}