import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class PercentagePromo extends Promo
{
    private final BigDecimal percentage;
    private final BigDecimal maximumDiscount;

    public PercentagePromo(String code, BigDecimal percentage, BigDecimal maximumDiscount, BigDecimal minimumSubtotal, LocalDate expiryDate, String restrictedDistrict, boolean firstTimeOnly)
    {
        super(code, minimumSubtotal, expiryDate, restrictedDistrict, firstTimeOnly);

        if (percentage == null || percentage.compareTo(BigDecimal.ZERO) < 0 || percentage.compareTo(new BigDecimal("100")) > 0)
        {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }

        this.percentage = percentage;
        this.maximumDiscount = maximumDiscount;
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal subtotal, BigDecimal deliveryFee)
    {
        BigDecimal discount = subtotal.multiply(percentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        if (maximumDiscount != null && discount.compareTo(maximumDiscount) > 0)
        {
            discount = maximumDiscount;
        }

        return discount;
    }
}