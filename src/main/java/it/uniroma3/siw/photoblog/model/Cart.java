package it.uniroma3.siw.photoblog.model;

import java.util.LinkedHashSet;
import java.util.Set;

public class Cart {

    private final Set<Long> eventIds = new LinkedHashSet<>();

    public Set<Long> getEventIds() {
        return eventIds;
    }

    public void add(Long eventId) {
        eventIds.add(eventId);
    }

    public void remove(Long eventId) {
        eventIds.remove(eventId);
    }

    public boolean contains(Long eventId) {
        return eventIds.contains(eventId);
    }

    public int size() {
        return eventIds.size();
    }

    public boolean isEmpty() {
        return eventIds.isEmpty();
    }

    public void clear() {
        eventIds.clear();
    }
}
