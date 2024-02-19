package model;

import model.policy.LayerModel;
import model.policy.PolicyModuleModel;

import java.util.HashMap;

public class DummyLayerModel extends LayerModel {
    public DummyLayerModel(String name) {
        super(name);
    }

    @Override
    public void addPolicyModule(PolicyModuleModel module) {
        this.policyModules.put(module.getName(), module);
    }


    public PolicyModuleModel getPolicyModule(String name) {
        return policyModules.getOrDefault(name, null);
    }

    public int getModulesNum() {
        return policyModules.size();
    }

    // EFFECT: getter for layer name
    public String getName() {
        return name;
    }
}
