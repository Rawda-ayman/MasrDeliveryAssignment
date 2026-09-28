import java.math.BigDecimal;
import java.util.*;

public class Restaurant
{
    private final int id;
    private String name;
    private String district;
    private Set<String> cuisines;
    private double rating;
    private boolean open;
    private List<MenuItem> menuItems;

    public Restaurant(int id, String name, String district, Set<String> cuisines, double rating, boolean open)
    {
        if (id <= 0)
        {
            throw new IllegalArgumentException("Restaurant ID must be positive");
        }

        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Restaurant name is required");
        }

        if (district == null || district.trim().isEmpty())
        {
            throw new IllegalArgumentException("District is required");
        }

        if (cuisines == null || cuisines.isEmpty())
        {
            throw new IllegalArgumentException("Restaurant must have at least one cuisine");
        }

        if (rating < 0.0 || rating > 5.0)
        {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        this.id = id;
        this.name = name;
        this.district = district;
        this.cuisines = new HashSet<>(cuisines);
        this.rating = rating;
        this.open = open;

        this.menuItems = new ArrayList<>();
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDistrict()
    {
        return district;
    }

    public Set<String> getCuisines()
    {
        return Collections.unmodifiableSet(cuisines);
    }

    public List<MenuItem> getMenuItems()
    {
        return Collections.unmodifiableList(menuItems);
    }

    public double getRating()
    {
        return rating;
    }

    public boolean isOpen()
    {
        return open;
    }

    public void setOpen(boolean open)
    {
        this.open = open;
    }

    public void addMenuItem(MenuItem item) {

        if (item == null)
        {
            throw new IllegalArgumentException("Menu item cannot be null");
        }

        if (menuItems.stream().anyMatch(existing -> existing.getId() == item.getId())) {

            throw new IllegalArgumentException("Menu item ID already exists in this restaurant");
        }

        menuItems.add(item);
    }

    public void addCuisine(String cuisine)
    {
        if (cuisine == null || cuisine.trim().isEmpty())
        {
            throw new IllegalArgumentException("Cuisine cannot be empty");
        }

        cuisines.add(cuisine.trim());
    }

    public void setMenuItemAvailability(String itemId, boolean available)
    {
        MenuItem item = menuItems.stream().filter(i -> i.getId().equals(itemId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
        item.setAvailable(available);
    }

    public void adjustItemStock(String itemId, BigDecimal amount)
    {
        MenuItem item = menuItems.stream().filter(i -> i.getId().equals(itemId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
        item.adjustStock(amount);
    }

    public void removeMenuItem(String itemId) {

        boolean removed = menuItems.removeIf(item -> item.getId().equals(itemId));

        if (!removed) {
            throw new IllegalArgumentException("Menu item not found");
        }
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof Restaurant))
        {
            return false;
        }

        Restaurant other = (Restaurant) obj;

        return id == other.id;
    }

    @Override
    public int hashCode()
    {
        return Integer.hashCode(id);
    }
}