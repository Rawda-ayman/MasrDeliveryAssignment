import java.math.BigDecimal;

public class OrderPrice
{
    private final BigDecimal subtotal;
    private final BigDecimal deliveryFee;
    private final BigDecimal serviceFee;
    private final BigDecimal promotionDiscount;
    private final BigDecimal total;

    public OrderPrice(BigDecimal subtotal, BigDecimal deliveryFee, BigDecimal serviceFee, BigDecimal promotionDiscount, BigDecimal total)
    {
        this.subtotal = subtotal;
        this.deliveryFee = deliveryFee;
        this.serviceFee = serviceFee;
        this.promotionDiscount = promotionDiscount;
        this.total = total;
    }

    public BigDecimal getSubtotal()
    {
        return subtotal;
    }

    public BigDecimal getDeliveryFee()
    {
        return deliveryFee;
    }

    public BigDecimal getServiceFee()
    {
        return serviceFee;
    }

    public BigDecimal getPromotionDiscount()
    {
        return promotionDiscount;
    }

    public BigDecimal getTotal()
    {
        return total;
    }
}