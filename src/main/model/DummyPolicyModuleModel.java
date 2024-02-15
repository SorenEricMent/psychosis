package model;

import model.policy.*;

public class DummyPolicyModuleModel extends PolicyModuleModel {
    // I have to define a separate class for temp projects because
    // Java DOESN't have higher-kinded Types / dependent types

    // If only I could write public class C<X<Y>>

    public DummyPolicyModuleModel(String name) {
        super(name);
    }

    private TypeEnfModel typeEnfDummy = new TypeEnfModel();
    private FileContextModel fileContextDummy = new FileContextModel();
    private InterfaceSetModel interfaceSetDummy = new InterfaceSetModel();

    // EFFECT: Override Object.toString, for tui command show_module
    @Override
    public String toString() {
        return ""; //stub
    }

    @Override
    public InterfaceModel findInterface(String name) {
        return null; //stub
    }

}
