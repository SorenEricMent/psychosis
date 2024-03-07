package model;

import model.exception.DuplicateException;
import model.exception.NotFoundException;
import model.exception.SyntaxParseException;
import model.exception.UnknownCapabilityException;
import model.policy.*;

import java.util.ArrayList;
import java.util.HashMap;

// A psychosis project. Basically the whole policy
// This is the version with autosave
// However, currently psychosis operate fully on virtual objects and require manual save
// To make it simpler and comply with Phase 2 requirements.

public class ProjectModel {

    private final String name;

    private final String projectPath;

    private HashMap<PolicyCapabilities, Boolean>
            capabilities = new HashMap<PolicyCapabilities, Boolean>();

    private AccessVectorModel accessVectors;

    private final TrackerModel tracker = new TrackerModel();

    private final InterfaceSetModel globalInterfaceSet = new InterfaceSetModel();

    protected ArrayList<LayerModel> layers = new ArrayList<LayerModel>();

    public InterfaceSetModel getGlobalInterfaceSet() {
        return globalInterfaceSet;
    }

    // EFFECTS: return the enabled capability list
    public ArrayList<PolicyCapabilities> getCapabilities() {
        ArrayList<PolicyCapabilities> cap = new ArrayList<>();
        for (PolicyCapabilities p : capabilities.keySet()) {
            if (capabilities.get(p)) {
                cap.add(p);
            }
        }
        return cap;
    }

    // Layers are often in small sizes
    // Therefore it is defined as an ArrayList

    // EFFECTS: create a new Project with name and points to projectPath
    // and empty access vector definition and all caps disabled
    public ProjectModel(String name, String projectPath) {
        this.name = name;
        this.projectPath = projectPath;
        // Initialize policy capabilities
        for (PolicyCapabilities cap : PolicyCapabilities.values()) {
            capabilities.put(cap, false);
        }
        this.accessVectors = new AccessVectorModel();
    }

    // EFFECTS: rebuild the global set of interfaces from all modules, for loading project from file
    public void rebuildGlobalInterfaceSet() {
        for (LayerModel layer : this.getLayers()) {
            for (String policyName : layer.getPolicyModules().keySet()) {
                PolicyModuleModel p = layer.getPolicyModule(policyName);
                for (InterfaceModel i : p.getInterfaceSet().getInterfaces()) {
                    this.globalInterfaceSet.addInterface(i);
                }
            }
        }
        System.out.println("Rebuilt the interface set for project " + this.name);
    }

    // EFFECT: getter for name
    public String getName() {
        return this.name;
    }

    public ArrayList<LayerModel> getLayers() {
        return layers;
    }

    // EFFECT: getter for access vectors
    public AccessVectorModel getAccessVectors() {
        return this.accessVectors;
    }

    // MODIFIES: this
    public void setAccessVectors(AccessVectorModel accessVectors) {
        this.accessVectors = accessVectors;
    }

    // EnumType for SELinux refpolicy's policy_capabilities
    public enum PolicyCapabilities {
        network_peer_controls,
        open_perms,
        always_check_network,
        extended_socket_class,
        cgroup_seclabel,
        nnp_nosuid_transition,
        genfs_seclabel_symlinks,
        ioctl_skip_cloexec
    }

    // EFFECT: getter for policy capability
    public Boolean checkCapability(PolicyCapabilities target) {
        return capabilities.getOrDefault(target, false);
    }

    // MODIFIES: this
    // EFFECT: setter for capabilities.
    public void setCapabilities(HashMap<PolicyCapabilities, Boolean> val) {
        this.capabilities = val;
    }

    // MODIFIES: this
    // EFFECTS: update a capability status with a boolean value
    public void updateCapability(PolicyCapabilities p, Boolean val) {
        this.capabilities.replace(p, val);
    }

    // EFFECTS: parse a string to the corresponding capability
    public static PolicyCapabilities strToCapability(String str) throws UnknownCapabilityException {
        switch (str) {
            case "network_peer_controls":
                return PolicyCapabilities.network_peer_controls;
            case "open_perms":
                return PolicyCapabilities.open_perms;
            case "always_check_network":
                return PolicyCapabilities.always_check_network;
            case "extended_socket_class":
                return PolicyCapabilities.extended_socket_class;
            case "cgroup_seclabel":
                return PolicyCapabilities.cgroup_seclabel;
            case "nnp_nosuid_transition":
                return PolicyCapabilities.nnp_nosuid_transition;
            case "genfs_seclabel_symlinks":
                return PolicyCapabilities.genfs_seclabel_symlinks;
            case "ioctl_skip_cloexec":
                return PolicyCapabilities.ioctl_skip_cloexec;
            default:
                throw new UnknownCapabilityException(str);
        }
    }

    // REQUIRE: policy_capability file after de-comment and strip (readAsWholeCode)
    // EFFECT: parse policy_capability file into HashMap
    // Switch-case used
    public static HashMap<PolicyCapabilities, Boolean> capabilitiesParser(String content)
            throws SyntaxParseException, UnknownCapabilityException {
        HashMap<PolicyCapabilities, Boolean> results = new HashMap<PolicyCapabilities, Boolean>();
        String[] tokenized = CommonUtil.basicTokenizer(content);
        for (int i = 0; i < tokenized.length; i++) {
            if (tokenized[i].equals("policycap")) {
                i = i + 1;
                // remove the ending ;
                PolicyCapabilities cap = strToCapability(tokenized[i].substring(0, tokenized[i].length() - 1));
                results.put(cap, true);
            } else {
                throw new SyntaxParseException("Unknown token when parsing capability: "
                        + tokenized[i] + ", at " + i);
            }
        }
        return results;
    }

    // MODIFIES: this
    // EFFECTS: add a layer with layerName, throw DuplicateException if a layer with that name already exists
    // Layer is not a set but an ArrayList because it is often in small size
    public void addLayer(String layerName) throws DuplicateException {
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).getName().equals(layerName)) {
                throw new DuplicateException(layerName);
            }
        }
        this.layers.add(new LayerModel(layerName));
    }

    // MODIFIES: this
    // EFFECTS: remove the layer with LayerName, throw NotFoundException if there is no layer with that name
    public void removeLayer(String layerName) throws NotFoundException {
        int index = -1;
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).getName().equals(layerName)) {
                index = i;
            }
        }
        if (index == -1) {
            throw new NotFoundException(layerName);
        } else {
            this.layers.remove(index);
        }
    }

    // EFFECTS: lookup and return the layer with layerName, throw NotFoundException if there is no layer with that name
    public LayerModel getLayer(String layerName) throws NotFoundException {
        for (LayerModel layer : layers) {
            if (layer.getName().equals(layerName)) {
                return layer;
            }
        }
        throw new NotFoundException(layerName);
    }

    // EFFECT: explain the project detail, override default toString
    public String toString() {
        String result = "";
        result = result.concat("Project name: " + getName() + "\nLayers: ");
        for (LayerModel layer : layers) {
            result = result.concat(layer.getName() + "\n");
        }
        return result;
    }

    // The following functions should have EFFECTS
    // corresponding to TempProjectModel, but none of they are currently used
    // as auto flush to fs is not planned to be supported now

    public void addInterface(String layerName, String moduleName, String interfaceName) {
//        this.getLayer(layerName).getPolicyModule(moduleName).addInterface(
//                new InterfaceModel(interfaceName, true));
    }

    public void removeInterface(String layerName, String moduleName, String interfaceName) {
//        this.getLayer(layerName).getPolicyModule(moduleName).removeInterface(interfaceName);
    }

    public void addModule(String layerName, String moduleName) {
//        this.getLayer(layerName).addPolicyModule(
//                new PolicyModuleModel(moduleName)
//        );
    }

    public void removeModule(String layerName, String moduleName) {
//        this.getLayer(layerName).removePolicyModule(moduleName);
    }
}
