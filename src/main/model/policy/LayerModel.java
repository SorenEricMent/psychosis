package model.policy;

import model.exception.DuplicateException;
import model.exception.NotFoundException;

import java.util.HashMap;

// For reference policy, there are only 5 layers
// For convenience, the first version of Psychosis is not planning to support layers
// And they will be static.
// A layer contain a set of policy modules
// SELinux roles are not in development plan at all as those are for
// domain transitions and domains are completely out of Psychosis's scope

public class LayerModel {
    protected String name;
    protected HashMap<String, PolicyModuleModel> policyModules = new HashMap<>();
    // String is the file name.

    // EFFECTS: init this layer with name
    public LayerModel(String name) {
        this.name = name;
    }

    // EFFECTS: add PolicyModule to this layer with its name as the index
    // MODIFIES: this
    public void addPolicyModule(PolicyModuleModel module) throws DuplicateException {
        if (this.policyModules.containsKey(module.getName())) {
            throw new DuplicateException(module.getName() + " already exists;");
        } else {
            this.policyModules.put(module.getName(), module);
        }
    }

    // Fot testing only
    public HashMap<String, PolicyModuleModel> getPolicyModules() {
        return policyModules;
    }

    // EFFECTS: get policyModule with name, throw NotFoundException if there is no such module
    public PolicyModuleModel getPolicyModule(String name) {
        if (policyModules.containsKey(name)) {
            return policyModules.get(name);
        }
        throw new NotFoundException("Policy module not found in layer.");
    }

    // EFFECTS: return the number of policyModules
    public int getModulesNum() {
        return policyModules.size();
    }

    // EFFECTS: getter for layer name
    public String getName() {
        return name;
    }

    // EFFECTS: override object default toString, return an overview of this layer
    @Override
    public String toString() {
        String res = "";
        res = res.concat("Layer name: " + getName() + "\n");
        res = res.concat("Module list: \n");
        for (String s : policyModules.keySet()) {
            res = res.concat(s + " ");
        }
        return res;
    }

    // MODIFIES: this
    // EFFECTS: remove a policyModule with given name
    public void removePolicyModule(String moduleName) {
        if (policyModules.containsKey(moduleName)) {
            policyModules.remove(moduleName);
        } else {
            throw new NotFoundException("Policy module not found.");
        }
    }
}
