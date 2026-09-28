import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RiderRepository
{
    private final Map<Integer, Rider> riders;

    public RiderRepository()
    {
        riders = new HashMap<>();
    }

    public void add(Rider rider)
    {
        if (rider == null)
        {
            throw new IllegalArgumentException(
                    "Rider cannot be null"
            );
        }

        if (riders.containsKey(rider.getId()))
        {
            throw new IllegalArgumentException(
                    "Rider ID already exists"
            );
        }

        riders.put(
                rider.getId(),
                rider
        );
    }

    public Rider findById(int id)
    {
        return riders.get(id);
    }

    public void remove(int id)
    {
        riders.remove(id);
    }

    public Map<Integer, Rider> getAll()
    {
        return Collections.unmodifiableMap(riders);
    }
}