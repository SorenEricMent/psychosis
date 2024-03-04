package model;

import model.policy.LayerModel;
import model.policy.PolicyModuleModel;

// A layer that is in memory only, changes not flushed into fs
public class DummyLayerModel extends LayerModel {

    public DummyLayerModel(String name) {
        super(name);
    }

    // EFFECTS: add a PolicyModuleModel with its name as the key
    @Override
    public void addPolicyModule(PolicyModuleModel module) {
        this.policyModules.put(module.getName(), module);
    }


    // EFFECTS: return the PolicyModuleModel with name and return null if such module does not exist
    public PolicyModuleModel getPolicyModule(String name) {
        return policyModules.getOrDefault(name, null);
    }

    // EFFECTS: return the number of policy modules
    public int getModulesNum() {
        return policyModules.size();
    }

    // EFFECT: getter for layer name
    public String getName() {
        return name;
    }
}
