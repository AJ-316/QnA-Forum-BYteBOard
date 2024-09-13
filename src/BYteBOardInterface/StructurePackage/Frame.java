package BYteBOardInterface.StructurePackage;

public interface Frame {

    void init(MainFrame main);

    void setContext(String... context);

    String recoverContext();

    void applyFrameSwitchContext(BoardFrameSwitchDelegate delegate);

    BoardFrame getBoardFrame();
}
