package model;

import model.policy.FileContextModel;
import model.policy.InterfaceSetModel;
import model.policy.TypeEnfModel;

public class DummyPolicyModuleModel {
    // I have to define a separate class for temp projects because
    // Java DOESN't have higher-kinded Types / dependent types

    // If only I could write public class C<X<Y>>

    private String name;

    private TypeEnfModel typeEnfObject = new TypeEnfModel();
    private FileContextModel fileContextObject = new FileContextModel();
    private InterfaceSetModel interfaceSetObject = new InterfaceSetModel();

}
