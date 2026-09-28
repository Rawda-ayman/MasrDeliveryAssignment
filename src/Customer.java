import java.math.BigDecimal;
import java.util.*;

public class Customer
{
    private final int id;
    private String name;
    private String mobileNumber;
    private List<Address> addresses;
    private BigDecimal walletBalance;
    private int completedOrders;
    private final Deque<String> recentSearches;
    private int completedOrderCount;

    public Customer(int id, String name, String mobileNumber, BigDecimal walletBalance) {

        if (id <= 0)
        {
            throw new IllegalArgumentException("Customer ID must be positive");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }

        if (!isValidEgyptianMobile(mobileNumber)) {
            throw new IllegalArgumentException("Invalid Egyptian mobile number");
        }

        if (walletBalance == null || walletBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Wallet balance cannot be negative");
        }

        this.id = id;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.walletBalance = walletBalance;
        this.addresses = new ArrayList<>();
        this.completedOrders = 0;
        this.recentSearches = new ArrayDeque<>();
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getMobileNumber()
    {
        return mobileNumber;
    }

    public int getCompletedOrders()
    {
        return completedOrders;
    }

    public List<Address> getAddresses()
    {
        return Collections.unmodifiableList(addresses);
    }

    public BigDecimal getWalletBalance()
    {
        return walletBalance;
    }

    public LoyaltyTier getLoyaltyTier() {
        return LoyaltyTier.fromCompletedOrders(completedOrders);
    }

    public List<String> getRecentSearches()
    {
        return List.copyOf(recentSearches);
    }

    private boolean isValidEgyptianMobile(String mobile)
    {
        return mobile != null && mobile.matches("01[0125]\\d{8}");
    }

    public void addAddress(Address address)
    {
        if (address == null)
        {
            throw new IllegalArgumentException("Address cannot be null");
        }

        addresses.add(address);
    }

    public void addBalance(BigDecimal amount)
    {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        walletBalance = walletBalance.add(amount);
    }

    public void addSearch(String search)
    {
        if (search == null || search.trim().isEmpty())
        {
            throw new IllegalArgumentException("Search cannot be empty");
        }

        search = search.trim();

        recentSearches.remove(search);
        recentSearches.addFirst(search);

        if (recentSearches.size() > 5)
        {
            recentSearches.removeLast();
        }
    }

    public void deductFromWallet(BigDecimal amount)
    {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        if (walletBalance.compareTo(amount) < 0)
        {
            throw new InsufficientWalletBalanceException("Insufficient wallet balance");
        }

        walletBalance = walletBalance.subtract(amount);
    }

    public void addToWallet(BigDecimal amount)
    {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        walletBalance = walletBalance.add(amount);
    }

    public void completeOrder()
    {
        completedOrderCount++;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof Customer))
        {
            return false;
        }

        Customer other = (Customer) obj;

        return id == other.id;
    }

    @Override
    public int hashCode()
    {
        return Integer.hashCode(id);
    }
}
