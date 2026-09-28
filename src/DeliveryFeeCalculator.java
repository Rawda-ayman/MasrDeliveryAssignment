import java.math.BigDecimal;

public class DeliveryFeeCalculator
{
    private static final BigDecimal BASE_FEE = new BigDecimal("15");

    private static final BigDecimal EXTRA_KM_FEE = new BigDecimal("3");

    public BigDecimal calculate(double distanceKm)
    {
        if (distanceKm < 0)
        {
            throw new IllegalArgumentException("Distance cannot be negative");
        }

        if (distanceKm <= 3)
        {
            return BASE_FEE;
        }

        BigDecimal extraDistance = BigDecimal.valueOf(distanceKm - 3);

        return BASE_FEE.add(extraDistance.multiply(EXTRA_KM_FEE));
    }
}