package BoardStructurePackage.BoardsPackage.AuthenticationPackage;

import BoardStructurePackage.BoardFrame;
import BoardStructurePackage.BoardPanel;
import BoardStructurePackage.MainFrame;
import CustomControls.*;
import Resources.ByteBoardTheme;

import java.awt.*;

public class AuthenticateBoardFrame extends BoardFrame {

    public AuthenticateBoardFrame(MainFrame main) {
        super(main, (context) -> true);
    }

    public void init(MainFrame main) {
        addInsets(20);
        GridBagBuilder builder = new GridBagBuilder(this, 2);
        builder.fill(GridBagConstraints.BOTH);
        builder.gridWeightY(1);

        BoardPanel titlePanel = new BoardPanel(main, this, ByteBoardTheme.MAIN);
        titlePanel.addInsets(50);
        titlePanel.setCornerRadius(90);
        titlePanel.setLayout(new BorderLayout());

        BoardLabel titleIcon = new BoardLabel("byteboard/byteboard-logo-transparent", -512);
        titlePanel.add(titleIcon, BorderLayout.CENTER);
        builder.add(titlePanel);
        addPanel("byteboard", titlePanel);

        builder.gridWeightX(1);

        LoginFormPanel loginFormPanel = new LoginFormPanel(main, this);
        builder.add(loginFormPanel);
        addPanel("loginForm", loginFormPanel);

        SignupFormPanel signupFormPanel = new SignupFormPanel(main, this);
        signupFormPanel.setVisible(false);
        builder.skipCells(-1);
        builder.add(signupFormPanel);

        addPanel("signupForm", signupFormPanel);
    }
}
