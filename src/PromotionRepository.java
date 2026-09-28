import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PromotionRepository{
    private final Map<String, Promo> promotions = new HashMap<>();

    public void add(Promo promotion) {

        if (promotion == null) {
            throw new IllegalArgumentException("Promotion cannot be null");
        }

        String code = promotion.getCode().toUpperCase();

        if (promotions.containsKey(code)) {
            throw new IllegalArgumentException("Promotion code already exists");
        }

        promotions.put(code, promotion);
    }

    public Promo findByCode(String code) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Promotion code is required");
        }

        return promotions.get(code.toUpperCase());
    }

    public Map<String, Promo> getAll() {
        return Collections.unmodifiableMap(promotions);
    }
}