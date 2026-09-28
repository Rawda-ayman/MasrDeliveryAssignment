import java.math.BigDecimal;

public class OrderLine
{
    private final MenuItem menuItem;
    private final BigDecimal quantity;

    public OrderLine(MenuItem menuItem, BigDecimal quantity)
    {
        if (menuItem == null)
        {
            throw new IllegalArgumentException("Menu item is required");
        }

        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem()
    {
        return menuItem;
    }

    public BigDecimal getQuantity()
    {
        return quantity;
    }

    public BigDecimal calculateTotal()
    {
        return menuItem.calculatePrice(quantity);
    }
}