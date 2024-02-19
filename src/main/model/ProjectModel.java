package model;

import model.exception.DuplicateException;
import model.exception.NotFoundException;
import model.exception.SyntaxParseException;
import model.exception.UnknownCapabilityException;
import model.policy.AccessVectorModel;
import model.policy.InterfaceSetModel;
import model.policy.LayerModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectModel {
    // A psychosis project. Basically the whole policy
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

    // Layers are often in small sizes
    // Therefore it is defined as an ArrayList

    public ProjectModel(String name, String projectPath) {
        this.name = name;
        this.projectPath = projectPath;
        // Initialize policy capabilities
        for (PolicyCapabilities cap : PolicyCapabilities.values()) {
            capabilities.put(cap, false);
        }
    }

    // EFFECT: getter for name
    public String getName() {
        return this.name;
    }

    // EFFECT: getter for access vectors
    public AccessVectorModel getAccessVectors() {
        return this.accessVectors;
    }

    // MODIFIES: this
    public void setAccessVectors(AccessVectorModel accessVectors) {
        this.accessVectors = accessVectors;
    }

    // EFFECT: getter for layers
    public ArrayList<LayerModel> getLayers() {
        return this.layers;
    }

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

    // REQUIRE: policy_capability file after de-comment and strip (readAsWholeCode)
    // EFFECT: parse policy_capability file into HashMap
    @SuppressWarnings("methodlength")
    // Switch-case used
    public static HashMap<PolicyCapabilities, Boolean> capabilitiesParser(String content)
            throws SyntaxParseException, UnknownCapabilityException {
        HashMap<PolicyCapabilities, Boolean> results = new HashMap<PolicyCapabilities, Boolean>();
        String[] tokenized = CommonUtil.basicTokenizer(content);
        for (int i = 0; i < tokenized.length; i++) {
            if (tokenized[i].equals("policycap")) {
                i = i + 1;
                switch (tokenized[i]) {
                    case "network_peer_controls;":
                        results.put(PolicyCapabilities.network_peer_controls, true);
                        break;
                    case "open_perms;":
                        results.put(PolicyCapabilities.open_perms, true);
                        break;
                    case "always_check_network;":
                        results.put(PolicyCapabilities.always_check_network, true);
                        break;
                    case "extended_socket_class;":
                        results.put(PolicyCapabilities.extended_socket_class, true);
                        break;
                    case "cgroup_seclabel;":
                        results.put(PolicyCapabilities.cgroup_seclabel, true);
                        break;
                    case "nnp_nosuid_transition;":
                        results.put(PolicyCapabilities.nnp_nosuid_transition, true);
                        break;
                    case "genfs_seclabel_symlinks;":
                        results.put(PolicyCapabilities.genfs_seclabel_symlinks, true);
                        break;
                    case "ioctl_skip_cloexec;":
                        results.put(PolicyCapabilities.ioctl_skip_cloexec, true);
                        break;
                    default:
                        throw new UnknownCapabilityException(tokenized[i]);
                }
            } else {
                throw new SyntaxParseException("Unknown token when parsing capability: "
                        + tokenized[i] + ", at " + i);
            }
        }
        return results;
    }

    public void addLayer(String layerName) throws DuplicateException {
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).getName().equals(layerName)) {
                throw new DuplicateException(layerName);
            }
        }
        this.layers.add(new LayerModel(layerName));
    }

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

    public void updateRule() {

    }

    public void removeRule() {

    }

    public void addInterface(String layerName, String moduleName, String interfaceName) {
//        this.getLayer(layerName).getPolicyModule(moduleName).addInterface(
//                new InterfaceModel(interfaceName, true));
    }

    public void updateInterface() {

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
