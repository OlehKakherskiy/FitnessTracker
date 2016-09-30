import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class HumanTest {

    private Human human;

    private LocalDateTime targetTime;

    private static Map<ActivityType, Integer> activityAmount;

    @BeforeClass
    public static void beforeClass() throws Exception {

        activityAmount = new HashMap<>();
        activityAmount.put(ActivityType.EAT, 2000);
        activityAmount.put(ActivityType.DRINK, 2600);
        activityAmount.put(ActivityType.WALK, 2000);
    }

    @Before
    public void beforeTest() throws Exception {
        human = new Human(activityAmount);
        targetTime = LocalDateTime.now();
    }

    @Test
    public void testAddActivity() throws Exception {
        human.addActivity(targetTime, 500, ActivityType.DRINK);
        human.addActivity(targetTime, 300, ActivityType.EAT);
        human.addActivity(targetTime, 500, ActivityType.WALK);
        Human expected = new Human(activityAmount,
                Arrays.asList(new HumanActivity(targetTime, 500, ActivityType.DRINK),
                        new HumanActivity(targetTime, 300, ActivityType.EAT),
                        new HumanActivity(targetTime, 500, ActivityType.WALK)));
        assertThat(human, is(expected));
    }

    @Test
    public void testGetDayActivityAmounts() throws Exception {
        Map<ActivityType, Double> expectedDayActivityAmounts = new HashMap<>();
        expectedDayActivityAmounts.put(ActivityType.EAT, 2500.0);
        expectedDayActivityAmounts.put(ActivityType.WALK, 1000.0);
        expectedDayActivityAmounts.put(ActivityType.DRINK, 1200.0);

        addActivityTemplate(targetTime, 1000, 400, 800, 1500, 1000);
        assertThat(human.getDayActivityAmounts(targetTime.toLocalDate()), is(expectedDayActivityAmounts));
    }

    @Test
    public void testGetDayStats() throws Exception {
        Map<ActivityType, Double> dayStats = new HashMap<ActivityType, Double>() {{
            put(ActivityType.DRINK, 1200 / 2600.0);
            put(ActivityType.EAT, 2500 / 2000.0);
            put(ActivityType.WALK, 1000 / 2000.0);
        }};
        addActivityTemplate(targetTime, 1000, 400, 800, 1500, 1000);
        assertThat(human.getDayStats(targetTime.toLocalDate()), is(dayStats));
    }

    @Test
    public void testGetPeriodMedianOneDay() throws Exception {
        Map<ActivityType, Double> periodMedian = new HashMap<>();
        periodMedian.put(ActivityType.DRINK, 700.0);
        periodMedian.put(ActivityType.WALK, 2000.0);
        periodMedian.put(ActivityType.EAT, 1350.0);

        LocalDateTime startTime = targetTime;
        addActivityTemplate(startTime, 1000, 400, 800, 1500, 1000);

        startTime = targetTime.plus(1, ChronoUnit.DAYS);
        addActivityTemplate(startTime, 500, 800, 700, 1350, 2000);

        startTime = targetTime.plus(2, ChronoUnit.DAYS);
        addActivityTemplate(startTime, 1500, 900, 100, 1370, 2300);

        startTime = targetTime.plus(3, ChronoUnit.DAYS);
        human.addActivity(startTime, 700, ActivityType.EAT);
        human.addActivity(startTime, 200, ActivityType.DRINK);


        startTime = targetTime.plus(2, ChronoUnit.WEEKS);
        addActivityTemplate(startTime, 100, 300, 1000, 2000, 1200);


        assertThat(human.getPeriodActivityMedian(targetTime, targetTime.plus(1, ChronoUnit.WEEKS)), is(periodMedian));
    }

    @Test
    public void testGetPeriodMedianTwoDaysAvg() throws Exception {
        Map<ActivityType, Double> periodMedian = new HashMap<>();
        periodMedian.put(ActivityType.DRINK, 750.0);
        periodMedian.put(ActivityType.WALK, 2150.0);
        periodMedian.put(ActivityType.EAT, 1175.0);

        LocalDateTime startTime = targetTime;
        addActivityTemplate(startTime, 1000, 400, 800, 1500, 1000);

        startTime = targetTime.plus(1, ChronoUnit.DAYS);
        addActivityTemplate(startTime, 500, 800, 700, 1350, 2000);

        startTime = targetTime.plus(2, ChronoUnit.DAYS);
        addActivityTemplate(startTime, 1500, 900, 100, 1370, 2300);

        startTime = targetTime.plus(3, ChronoUnit.DAYS);
        human.addActivity(startTime, 700, ActivityType.EAT);
        human.addActivity(startTime, 200, ActivityType.DRINK);

        human.addActivity(startTime, 2500, ActivityType.WALK);
        human.addActivity(startTime, 3000, ActivityType.DRINK);
        human.addActivity(startTime, 200, ActivityType.EAT);


        startTime = targetTime.plus(2, ChronoUnit.WEEKS);
        addActivityTemplate(startTime, 100, 300, 1000, 2000, 1200);


        assertThat(human.getPeriodActivityMedian(targetTime, targetTime.plus(1, ChronoUnit.WEEKS)), is(periodMedian));
    }

    private void addActivityTemplate(LocalDateTime targetTime, int eat1, int drink1, int drink2, int eat2, int walk1) {
        human.addActivity(targetTime, eat1, ActivityType.EAT);
        human.addActivity(targetTime.plus(2, ChronoUnit.HOURS), drink1, ActivityType.DRINK);
        human.addActivity(targetTime.plus(3, ChronoUnit.HOURS), drink2, ActivityType.DRINK);
        human.addActivity(targetTime.plus(3, ChronoUnit.HOURS), eat2, ActivityType.EAT);
        human.addActivity(targetTime.plus(4, ChronoUnit.HOURS), walk1, ActivityType.WALK);
    }

}