import java.math.BigDecimal;

public class PricingResult
{
    private final BigDecimal subtotal;
    private final BigDecimal deliveryFee;
    private final BigDecimal serviceFee;
    private final BigDecimal promoDiscount;
    private final BigDecimal total;

    public PricingResult(BigDecimal subtotal, BigDecimal deliveryFee, BigDecimal serviceFee, BigDecimal promoDiscount, BigDecimal total)
    {
        this.subtotal = subtotal;
        this.deliveryFee = deliveryFee;
        this.serviceFee = serviceFee;
        this.promoDiscount = promoDiscount;
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

    public BigDecimal getPromoDiscount()
    {
        return promoDiscount;
    }

    public BigDecimal getTotal()
    {
        return total;
    }
}