import java.time.LocalDateTime;


public class HumanActivity {

    private LocalDateTime time;

    private int amount;

    private ActivityType activityType;

    public HumanActivity(LocalDateTime time, int amount, ActivityType activityType) {
        this.time = time;
        this.amount = amount;
        this.activityType = activityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HumanActivity that = (HumanActivity) o;

        if (amount != that.amount) return false;
        if (!time.equals(that.time)) return false;
        return activityType == that.activityType;

    }

    @Override
    public int hashCode() {
        int result = time.hashCode();
        result = 31 * result + amount;
        result = 31 * result + activityType.hashCode();
        return result;
    }

    public double getAmount() {
        return (double) amount;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "HumanActivity{" +
                "time=" + time +
                ", amount=" + amount +
                ", activityType=" + activityType +
                '}';
    }
}
