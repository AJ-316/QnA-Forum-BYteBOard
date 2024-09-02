package QnAForumInterface.InterfaceEventPackage;

@FunctionalInterface
public interface InterfaceEventListener {

    void onInvoked(Object... eventConstraints);

}
