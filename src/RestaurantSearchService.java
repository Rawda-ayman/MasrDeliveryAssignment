import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class RestaurantSearchService
{
    public List<Restaurant> search(Collection<Restaurant> restaurants, Predicate<Restaurant> condition)
    {
        if (restaurants == null)
        {
            throw new IllegalArgumentException("Restaurants cannot be null");
        }

        if (condition == null)
        {
            throw new IllegalArgumentException("Search condition is required");
        }

        return restaurants.stream().filter(condition).toList();
    }


    public Predicate<Restaurant> byDistrict(String district)
    {
        if (district == null || district.isBlank())
        {
            throw new IllegalArgumentException("District cannot be empty");
        }

        return restaurant -> restaurant.getDistrict().equalsIgnoreCase(district);
    }


    public Predicate<Restaurant> minimumRating(double minimumRating)
    {
        if (minimumRating < 0 || minimumRating > 5)
        {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        return restaurant -> restaurant.getRating() >= minimumRating;
    }


    public Predicate<Restaurant> byCuisine(String cuisine)
    {
        if (cuisine == null || cuisine.isBlank())
        {
            throw new IllegalArgumentException("Cuisine cannot be empty");
        }

        return restaurant -> restaurant.getCuisines().stream().anyMatch(c -> c.equalsIgnoreCase(cuisine));
    }


    public Predicate<Restaurant> priceCeiling(BigDecimal maximumPrice)
    {
        if (maximumPrice == null || maximumPrice.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("Price ceiling cannot be negative");
        }

        return restaurant -> restaurant.getMenuItems().stream().anyMatch(item -> item.getPrice().compareTo(maximumPrice) <= 0);
    }
}
