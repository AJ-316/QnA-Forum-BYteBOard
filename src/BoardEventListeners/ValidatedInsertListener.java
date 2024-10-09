package BoardEventListeners;

@FunctionalInterface
public interface ValidatedInsertListener {
    void invoke(String text);
}
