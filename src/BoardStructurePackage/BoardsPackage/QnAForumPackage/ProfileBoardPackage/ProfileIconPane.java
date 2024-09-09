package BoardStructurePackage.BoardsPackage.QnAForumPackage.ProfileBoardPackage;

import CustomControls.BoardLabel;
import Resources.ByteBoardTheme;
import Resources.ResourceManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ProfileIconPane extends BoardLabel {

    private static ProfileIconPane SELECTED_PROFILE;

    public ProfileIconPane(String index, Consumer<ProfileIconPane> setUserProfile) {
        setName(index);
        resetIcon();

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if(isSelected()) return;
                setProfileIcon(getName(), ByteBoardTheme.ACCENT, ResourceManager.REGULAR);
            }

            public void mouseExited(MouseEvent e) {
                if(isSelected()) return;
                resetIcon();
            }

            // fixme
            public void mousePressed(MouseEvent e) {
                if(isSelected()) return;
                setProfileIcon(getName(), ByteBoardTheme.BASE, ResourceManager.REGULAR);
            }

            public void mouseReleased(MouseEvent e) {
                if(isSelected()) return;
                selectProfile();
                setUserProfile.accept(SELECTED_PROFILE);
            }
        });
    }

    public void selectProfile() {
        if(SELECTED_PROFILE != null)
            SELECTED_PROFILE.resetIcon();
        SELECTED_PROFILE = this;

        setProfileIcon(getName(), ByteBoardTheme.ACCENT_DARK, ResourceManager.REGULAR);
    }

    public void resetIcon() {
        setProfileIcon(getName(), ByteBoardTheme.MAIN_LIGHT, ResourceManager.REGULAR);
    }

    public boolean isSelected() {
        return SELECTED_PROFILE != null && SELECTED_PROFILE.equals(this);
    }

}
