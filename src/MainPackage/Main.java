package MainPackage;

import BYteBOardDatabase.DatabaseManager;
import QnAForumInterface.AuthenticationForm;
import Resources.ResourceManager;

public class Main {

    public static void main(String[] args) {
        ResourceManager.init();
        DatabaseManager.init();
        AuthenticationForm.init(true);
    }
}
