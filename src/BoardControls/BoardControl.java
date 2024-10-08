package BoardControls;

public interface BoardControl {

    void setBackground(String bg);

    void setFontPrimary(String type, int size);

    void setFontSecondary(String type, int size);

    void setFGDark();

    void setFGLight();

    void setFGMain();

    void addInsets(int top, int left, int bottom, int right);

    default void addInsets(int inset) {
        addInsets(inset, inset, inset, inset);
    }
}
