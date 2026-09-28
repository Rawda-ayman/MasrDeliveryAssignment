import java.math.BigDecimal;

public class SubtotalCalculator
{
    public BigDecimal calculate(Order order)
    {
        if (order == null)
        {
            throw new IllegalArgumentException("Order is required");
        }

        return order.getOrderLines().stream().map(line -> line.getMenuItem().calculatePrice(line.getQuantity())).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}