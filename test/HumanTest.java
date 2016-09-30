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

/**
 * Created by Oleh_Kakherskyi on 9/28/2016.
 */
public class HumanTest {

    private Human human;

    private static LocalDateTime targetTime;

    private static Map<ActivityType, Integer> activityAmount;

    @BeforeClass
    public static void beforeClass() throws Exception {
        targetTime = LocalDateTime.now();
        activityAmount = new HashMap<>();
        activityAmount.put(ActivityType.EAT, 2000);
        activityAmount.put(ActivityType.DRINK, 2600);
        activityAmount.put(ActivityType.WALK, 2000);
    }

    @Before
    public void beforeTest() throws Exception {
        human = new Human(activityAmount);
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

        addActivityTemplate();
        assertThat(human.getDayActivityAmounts(targetTime.toLocalDate()), is(expectedDayActivityAmounts));
    }

    @Test
    public void testGetDayStats() throws Exception {
        Map<ActivityType, Double> dayStats = new HashMap<ActivityType, Double>() {{
            put(ActivityType.DRINK, 1200 / 2600.0);
            put(ActivityType.EAT, 2500 / 2000.0);
            put(ActivityType.WALK, 1000 / 2000.0);
        }};
        addActivityTemplate();
        assertThat(human.getDayStats(targetTime.toLocalDate()), is(dayStats));
    }

    private void addActivityTemplate() {
        human.addActivity(targetTime, 1000, ActivityType.EAT);
        human.addActivity(targetTime.plus(2, ChronoUnit.HOURS), 400, ActivityType.DRINK);
        human.addActivity(targetTime.plus(3, ChronoUnit.HOURS), 800, ActivityType.DRINK);
        human.addActivity(targetTime.plus(3, ChronoUnit.HOURS), 1500, ActivityType.EAT);
        human.addActivity(targetTime.plus(4, ChronoUnit.HOURS), 1000, ActivityType.WALK);
    }

}