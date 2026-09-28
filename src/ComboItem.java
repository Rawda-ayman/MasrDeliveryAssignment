import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ComboItem extends MenuItem {

    private final List<MenuItem> items;
    private final BigDecimal discountPercentage;

    public ComboItem(String id, String name, String category, int preparationTime, boolean available, BigDecimal  stock, List<MenuItem> items, BigDecimal discountPercentage)
    {
        super(id, name, BigDecimal.ONE, category, preparationTime, available, stock);

        if (items == null || items.isEmpty())
        {
            throw new IllegalArgumentException("Combo must contain at least one item");
        }

        if (discountPercentage == null || discountPercentage.compareTo(BigDecimal.ZERO) < 0 || discountPercentage.compareTo(new BigDecimal("100")) > 0)
        {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }

        this.items = List.copyOf(items);
        this.discountPercentage = discountPercentage;
    }

    @Override
    public BigDecimal calculatePrice(BigDecimal quantity)
    {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        BigDecimal total = items.stream().map(item -> item.calculatePrice(BigDecimal.ONE)).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = total.multiply(discountPercentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        BigDecimal bundlePrice = total.subtract(discount);

        return bundlePrice.multiply(quantity);
    }

    public List<MenuItem> getItems()
    {
        return items;
    }

    public BigDecimal getDiscountPercentage()
    {
        return discountPercentage;
    }
}