public class Address
{
    private String district;
    private String details;

    public Address(String district, String details)
    {
        if (district == null || district.trim().isEmpty()) {
            throw new IllegalArgumentException("District is required");
        }

        if (details == null || details.trim().isEmpty()) {
            throw new IllegalArgumentException("Address details are required");
        }

        this.district = district;
        this.details = details;
    }

    public String getDistrict()
    {
        return district;
    }

    public String getDetails()
    {
        return details;
    }
}