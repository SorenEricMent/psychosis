package model.policy;

import java.util.HashMap;

public class LayerModel {
    // For reference policy, there are only 5 layers
    // For convenience, the first version of Psychosis is not planning to support layers
    // And they will be static.
    // A layer contain a set of policy modules
    // SELinux roles are not in development plan at all as those are for
    // domain transitions and domains are completely out of Psychosis's scope

    private String name;
    private HashMap<String, PolicyModuleModel> policyModules = new HashMap<String, PolicyModuleModel>();
    // String is the file path.

    public LayerModel(String name) {
        this.name = name;
    }

    public void addPolicyModules() {

    }

    public PolicyModuleModel getPolicyModules() {
        return null; // stub
    }

    public int getModulesNum() {
        return policyModules.size();
    }

    // EFFECT: getter for layer name
    public String getName() {
        return name;
    }
}
