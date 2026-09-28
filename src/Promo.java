import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Promo
{
    private final String code;
    private final BigDecimal minimumSubtotal;
    private final LocalDate expiryDate;
    private final String restrictedDistrict;
    private final boolean firstTimeOnly;

    public Promo(
            String code,
            BigDecimal minimumSubtotal,
            LocalDate expiryDate,
            String restrictedDistrict,
            boolean firstTimeOnly)
    {
        if (code == null || code.isBlank())
        {
            throw new IllegalArgumentException(
                    "Promotion code cannot be empty"
            );
        }

        if (minimumSubtotal != null &&
                minimumSubtotal.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException(
                    "Minimum subtotal cannot be negative"
            );
        }

        this.code = code.toUpperCase();
        this.minimumSubtotal = minimumSubtotal;
        this.expiryDate = expiryDate;
        this.restrictedDistrict = restrictedDistrict;
        this.firstTimeOnly = firstTimeOnly;
    }

    public String getCode()
    {
        return code;
    }

    public abstract BigDecimal calculateDiscount(
            BigDecimal subtotal,
            BigDecimal deliveryFee);

    public boolean appliesTo(
            BigDecimal subtotal,
            String district,
            boolean firstTimeCustomer)
    {
        if (minimumSubtotal != null &&
                subtotal.compareTo(minimumSubtotal) < 0)
        {
            return false;
        }

        if (expiryDate != null &&
                LocalDate.now().isAfter(expiryDate))
        {
            return false;
        }

        if (restrictedDistrict != null &&
                !restrictedDistrict.equalsIgnoreCase(district))
        {
            return false;
        }

        if (firstTimeOnly && !firstTimeCustomer)
        {
            return false;
        }

        return true;
    }
}