package model.policy;

import java.security.Policy;
import java.util.HashMap;

public class LayerModel {
    // For reference policy, there are only 5 layers
    // For convenience, the first version of Psychosis is not planning to support layers
    // And they will be static.
    // A layer contains the following:
    // A set of FileContext (For the current plan, the content will be ignored))
    // A set of Interface
    // A set of Attributes
    // A set of rules (Psychosis don't support MLSConstrain or constrain, only dontaudit)
    // A set of Templates (For the current plan, the content will be ignored)
    // SELinux roles are not in development plan at all as those are for
    // domain transitions and domains are completely out of Psychosis's scope

    private String name;
    private HashMap<String, PolicyModel> policyModules = new HashMap<String, PolicyModel>();
    // String is the file path.

    LayerModel(String name) {
        this.name = name;
    }

    public PolicyModel getPolicyModel() {
        return null;//stub
    }

    // EFFECT: return layer name
    public String getName() {
        return name;
    }
}
