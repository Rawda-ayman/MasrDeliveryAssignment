import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.List;

public class RestaurantRepository
{
    private final Map<Integer, Restaurant> restaurants;

    public RestaurantRepository()
    {
        restaurants = new HashMap<>();
    }

    public void add(Restaurant restaurant)
    {
        if (restaurant == null)
        {
            throw new IllegalArgumentException(
                    "Restaurant cannot be null"
            );
        }

        if (restaurants.containsKey(restaurant.getId()))
        {
            throw new IllegalArgumentException(
                    "Restaurant ID already exists"
            );
        }

        restaurants.put(
                restaurant.getId(),
                restaurant
        );
    }

    public Restaurant findById(int id)
    {
        return restaurants.get(id);
    }

    public void remove(int id)
    {
        restaurants.remove(id);
    }

    public Map<Integer, Restaurant> getAll()
    {
        return Collections.unmodifiableMap(restaurants);
    }

    public List<Restaurant> findAllSortedByRating()
    {
        return restaurants.values().stream().sorted(Comparator.comparingDouble(Restaurant::getRating).reversed().thenComparing(Restaurant::getName, String.CASE_INSENSITIVE_ORDER)).toList();
    }
}