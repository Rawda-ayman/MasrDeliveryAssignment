import java.util.Map;

public class DistrictDistanceService
{
    private final Map<String, Double> distances;

    public DistrictDistanceService(Map<String, Double> distances)
    {
        this.distances = Map.copyOf(distances);
    }

    public double getDistance(String restaurantDistrict, String deliveryDistrict)
    {
        String key = restaurantDistrict.toLowerCase() + "-" + deliveryDistrict.toLowerCase();

        return distances.getOrDefault(key, 0.0);
    }
}