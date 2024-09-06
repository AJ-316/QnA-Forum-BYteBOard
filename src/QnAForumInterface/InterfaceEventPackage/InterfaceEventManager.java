package QnAForumInterface.InterfaceEventPackage;

import java.util.*;

public class InterfaceEventManager {

    private static final Map<String, List<InterfaceEventListener>> interfaceEventListeners = new HashMap<>();
    private static final Map<String, InterfaceRequestListener> interfaceRequestListeners = new HashMap<>();

    public static void addListener(String event, InterfaceEventListener listener) {
        interfaceEventListeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }

    public static void addListener(String request, InterfaceRequestListener listener) {
        interfaceRequestListeners.put(request, listener);
    }

    public static void invokeEvent(String event, Object... eventConstraints) {
        List<InterfaceEventListener> listeners = interfaceEventListeners.get(event);
        if(listeners == null) return;

        for (InterfaceEventListener listener : listeners) {
            listener.onInvoked(eventConstraints);
        }
    }

    public static Object[] invokeRequest(String request) {
        InterfaceRequestListener listener = interfaceRequestListeners.get(request);
        Objects.requireNonNull(listener, "Invalid InterfaceRequest: " + request +
                " | Available Requests: " + Arrays.toString(interfaceRequestListeners.keySet().toArray()));

        return listener.onRequested();
    }

    public static void removeListener(String event, InterfaceEventListener listener) {
        List<InterfaceEventListener> listeners = interfaceEventListeners.get(event);
        if(listeners == null) return;

        listeners.remove(listener);
        if(listeners.isEmpty()) interfaceEventListeners.remove(event);
    }
}
