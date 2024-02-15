package model.policy;

import model.exception.NotFoundException;

import java.util.HashMap;

public class LayerModel {
    // For reference policy, there are only 5 layers
    // For convenience, the first version of Psychosis is not planning to support layers
    // And they will be static.
    // A layer contain a set of policy modules
    // SELinux roles are not in development plan at all as those are for
    // domain transitions and domains are completely out of Psychosis's scope

    protected String name;
    protected HashMap<String, PolicyModuleModel> policyModules = new HashMap<String, PolicyModuleModel>();
    // String is the file name.

    public LayerModel(String name) {
        this.name = name;
    }

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

    public String toString() {
        String res = "";
        res = res.concat("Layer name: " + getName() + "\n");
        res = res.concat("Module list: \n");
        for (String s : policyModules.keySet()) {
            res = res.concat(s + " ");
        }
        return res;
    }

    public void removePolicyModule(String moduleName) {
        if (policyModules.containsKey(moduleName)) {
            policyModules.remove(moduleName);
        } else {
            throw new NotFoundException("Policy module not found.");
        }
    }
}
