
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CustomerRepository
{
    private final Map<Integer, Customer> customers;

    public CustomerRepository()
    {
        customers = new HashMap<>();
    }

    public void add(Customer customer)
    {
        if (customer == null)
        {
            throw new IllegalArgumentException(
                    "Customer cannot be null"
            );
        }

        if (customers.containsKey(customer.getId()))
        {
            throw new IllegalArgumentException(
                    "Customer ID already exists"
            );
        }

        customers.put(
                customer.getId(),
                customer
        );
    }

    public Customer findById(int id)
    {
        return customers.get(id);
    }

    public void remove(int id)
    {
        customers.remove(id);
    }

    public Map<Integer, Customer> getAll()
    {
        return Collections.unmodifiableMap(customers);
    }
}