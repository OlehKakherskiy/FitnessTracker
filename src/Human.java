import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Oleh_Kakherskyi on 9/28/2016.
 */
public class Human {

    private Map<ActivityType, Integer> expectedAmount;

    private List<HumanActivity> humanActivities;

    public Human(Map<ActivityType, Integer> expectedAmount) {
        this.expectedAmount = expectedAmount;
        this.humanActivities = new ArrayList<>();
    }

    public Human(Map<ActivityType, Integer> expectedAmount, List<HumanActivity> humanActivities) {
        this.expectedAmount = expectedAmount;
        this.humanActivities = humanActivities;
    }

    public void addActivity(LocalDateTime drinkTime, int amount, ActivityType activityType) {
        humanActivities.add(new HumanActivity(drinkTime, amount, activityType));
    }

    public Map<ActivityType, Double> getDayStats(LocalDate localDate) {
        Map<ActivityType, Double> stats = new HashMap<>();
        for (Map.Entry<ActivityType, Double> activity : getDayActivityAmounts(localDate).entrySet()) {
            stats.put(activity.getKey(), activity.getValue() / expectedAmount.get(activity.getKey()));
        }
        return stats;
    }

    public Map<ActivityType, Double> getDayActivityAmounts(LocalDate date) {
        return humanActivities.stream().filter(humanActivity -> humanActivity.getTime().getDayOfYear() == date.getDayOfYear())
                .collect(Collectors.groupingBy(HumanActivity::getActivityType,
                        Collectors.summingDouble(HumanActivity::getAmount)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Human human = (Human) o;

        if (!expectedAmount.equals(human.expectedAmount)) return false;
        return humanActivities.equals(human.humanActivities);

    }

    @Override
    public int hashCode() {
        int result = expectedAmount.hashCode();
        result = 31 * result + humanActivities.hashCode();
        return result;
    }
}
