package model;

import model.policy.LayerModel;
import model.policy.PolicyModuleModel;

import java.util.HashMap;

public class DummyLayerModel extends LayerModel {
    // For reference policy, there are only 5 layers
    // For convenience, the first version of Psychosis is not planning to support layers
    // And they will be static.
    // A layer contain a set of policy modules
    // SELinux roles are not in development plan at all as those are for
    // domain transitions and domains are completely out of Psychosis's scope

    // String is the file name.

    public DummyLayerModel(String name) {
        super(name);
    }

    public void addPolicyModules(PolicyModuleModel module) {

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
