package MainPackage;

import QnAForumDatabase.Database;
import QnAForumInterface.AuthenticationForm;
import Resources.ResourceManager;

public class Main {

    public static void main(String[] args) {
        ResourceManager.init();
        Database.init();
        AuthenticationForm.directAccess("Example");
    }
}
