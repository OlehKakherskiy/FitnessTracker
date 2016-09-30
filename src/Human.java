import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    public Map<ActivityType, Double> getPeriodActivityMedian(LocalDateTime startFrom, LocalDateTime upTo) {
        System.out.println(humanActivities);
        Map<ActivityType, List<Double>> activitiesInPeriod = humanActivities.stream()
                .filter(humanActivity -> !startFrom.isAfter(humanActivity.getTime()) && upTo.isAfter(humanActivity.getTime())).
                        collect(Collectors.groupingBy(HumanActivity::getActivityType,
                                Collectors.mapping(HumanActivity::getAmount, Collectors.toList())));
        System.out.println(activitiesInPeriod);

        Map<ActivityType, Double> medians = new HashMap<>();
        for (Map.Entry<ActivityType, List<Double>> entry : activitiesInPeriod.entrySet()) {
            medians.put(entry.getKey(), calculateMedian(entry.getValue().stream().sorted().collect(Collectors.toList())));
        }
        System.out.println(medians);
        return medians;
    }

    private Double calculateMedian(List<Double> list) {
        System.out.println(list);
        int size = list.size();
        if (size % 2 == 1) {
            return list.get(size / 2);
        } else {
            return (list.get((size - 1) / 2) + list.get((list.size() - 1) / 2 + 1)) / 2; // (left_median + right_median) / 2
        }
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
