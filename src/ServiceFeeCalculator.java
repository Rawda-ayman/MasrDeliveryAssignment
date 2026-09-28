import java.math.BigDecimal;
import java.math.RoundingMode;

public class ServiceFeeCalculator
{
    private static final BigDecimal SERVICE_RATE = new BigDecimal("0.10");

    public BigDecimal calculate(BigDecimal subtotal)
    {
        if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("Subtotal cannot be negative");
        }

        return subtotal.multiply(SERVICE_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}
