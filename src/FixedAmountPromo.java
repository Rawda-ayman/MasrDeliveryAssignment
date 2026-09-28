import java.math.BigDecimal;
import java.time.LocalDate;

public class FixedAmountPromo extends Promo
{
    private final BigDecimal amount;

    public FixedAmountPromo(String code, BigDecimal amount, BigDecimal minimumSubtotal, LocalDate expiryDate, String restrictedDistrict, boolean firstTimeOnly)
    {
        super(code, minimumSubtotal, expiryDate, restrictedDistrict, firstTimeOnly);

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }

        this.amount = amount;
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal subtotal, BigDecimal deliveryFee)
    {
        return amount.min(subtotal);
    }
}