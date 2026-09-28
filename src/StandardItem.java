import java.math.BigDecimal;

public class StandardItem extends MenuItem
{
    public StandardItem(String id, String name, BigDecimal price, String category, int preparationTime, boolean available, BigDecimal  stock)
    {
        super(id, name, price, category, preparationTime, available, stock);
    }

    @Override
    public BigDecimal calculatePrice(BigDecimal quantity)
    {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        return getPrice().multiply(quantity);
    }
}