import java.math.BigDecimal;

public abstract class MenuItem
{
    private String id;
    private String name;
    private final BigDecimal price;
    private String category;
    private int preparationTime;
    private boolean available;
    private BigDecimal stock;

    public MenuItem(String id, String name, BigDecimal price, String category, int preparationTime, boolean available, BigDecimal stock)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.preparationTime = preparationTime;
        this.available = available;
        this.stock = stock;
    }

    public abstract BigDecimal calculatePrice(BigDecimal quantity);

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public String getCategory()
    {
        return category;
    }

    public int getPreparationTime()
    {
        return preparationTime;
    }

    public boolean isAvailable()
    {
        return available;
    }

    public BigDecimal getStock()
    {
        return stock;
    }

    public void setAvailable(boolean available)
    {
        this.available = available;
    }

    public void setStock(BigDecimal stock)
    {
        if (stock == null || stock.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        this.stock = stock;
    }

    public void adjustStock(BigDecimal amount)
    {
        BigDecimal newStock = stock.add(amount);

        if (newStock.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        stock = newStock;
    }


    public boolean hasEnoughStock(BigDecimal quantity)
    {
        return quantity != null && quantity.compareTo(BigDecimal.ZERO) > 0 && stock.compareTo(quantity) >= 0;
    }

    public void reduceStock(BigDecimal quantity)
    {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (!hasEnoughStock(quantity))
        {
            throw new IllegalStateException("Not enough stock");
        }

        stock = stock.subtract(quantity);
    }

}
