import java.math.BigDecimal;

public class WeightedItem extends MenuItem
{

    public WeightedItem(String id, String name, BigDecimal pricePerKg, String category, int preparationTime, boolean available, BigDecimal  stock)
    {
        super(id, name, pricePerKg, category, preparationTime, available, stock);
    }

    @Override
    public BigDecimal calculatePrice(BigDecimal kilograms)
    {
        if (kilograms == null || kilograms.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Weight must be greater than zero");
        }

        return getPrice().multiply(kilograms);
    }
}
