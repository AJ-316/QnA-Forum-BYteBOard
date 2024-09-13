package BYteBOardInterface.BoardsPackage.AuthenticationPackage;

import BYteBOardInterface.StructurePackage.BoardFrame;
import BYteBOardInterface.StructurePackage.BoardFrameSwitchDelegate;
import BYteBOardInterface.StructurePackage.BoardPanel;
import BYteBOardInterface.StructurePackage.MainFrame;
import CustomControls.*;
import Resources.ByteBoardTheme;

import java.awt.*;

public class AuthenticateBoardFrame extends BoardFrame {

    public AuthenticateBoardFrame(MainFrame main) {
        super(main, (delegate, context) -> null);
    }

    public void init(MainFrame main) {
        GridBagBuilder builder = new GridBagBuilder(this, 2);
        builder.fill(GridBagConstraints.BOTH);
        builder.weightY(1);

        BoardPanel titlePanel = new BoardPanel(main, this, ByteBoardTheme.MAIN);
        titlePanel.addInsets(50);
        titlePanel.setCornerRadius(90);
        titlePanel.setLayout(new BorderLayout());

        BoardLabel titleIcon = new BoardLabel("byteboard/byteboard-logo-transparent2", -512);
        titlePanel.add(titleIcon, BorderLayout.CENTER);
        builder.add(titlePanel);

        builder.weightX(1);

        LoginFormPanel loginFormPanel = new LoginFormPanel(main, this);
        builder.add(loginFormPanel);
        addPanel(LoginFormPanel.class, loginFormPanel);

        SignupFormPanel signupFormPanel = new SignupFormPanel(main, this);
        signupFormPanel.setVisible(false);
        builder.skipCells(-1);
        builder.add(signupFormPanel);
        addPanel(SignupFormPanel.class, signupFormPanel);
    }

    public void applyFrameSwitchContext(BoardFrameSwitchDelegate frameSwitchDelegate) {

    }
}
