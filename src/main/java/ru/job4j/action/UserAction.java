package ru.job4j.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserAction {
    public static final List<HandleCommand> ACTIONLIST = new ArrayList<>();
    private boolean addStartCommand = false;

    @Autowired
    public UserAction(HandleWeekMoodLogCommand handleWeekMoodLogCommand,
                      HandleMonthMoodLogCommand handleMonthMoodLogCommand,
                      HandleAwardCommand handleAwardCommand,
                      HandleStartCommand handleStartCommand) {
        ACTIONLIST.add(handleWeekMoodLogCommand);
        ACTIONLIST.add(handleMonthMoodLogCommand);
        ACTIONLIST.add(handleAwardCommand);
        ACTIONLIST.add(handleStartCommand);
    }

    public List<HandleCommand> getUserActionList() {
        return ACTIONLIST;
    }

    public void addStartAction(HandleCommand action) {
        if (!addStartCommand) {
            ACTIONLIST.add(action);
            addStartCommand = true;
        }
    }
}
