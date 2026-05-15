package com.ecoskiller.analytics.server;

import com.ecoskiller.analytics.agents.BaseAgent;
import java.util.*;

public class AgentRegistry {
    private final List<BaseAgent> agents = new ArrayList<>();
    public void register(BaseAgent a) { agents.add(a); }
    public List<BaseAgent> all()      { return Collections.unmodifiableList(agents); }
    public int count()                { return agents.size(); }
}
