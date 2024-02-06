package model.policy;

import model.CommonUtil;
import model.TrackerModel;
import model.exception.SyntaxParseException;
import model.exception.UnknownCapabilityException;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectModel {
    // A psychosis project. Basically the whole policy
    private String name;

    private String projectPath;

    private HashMap<PolicyCapabilities, Boolean>
            capabilities = new HashMap<PolicyCapabilities, Boolean>();

    private AccessVectorModel accessVectors;

    private TrackerModel tracker = new TrackerModel();
    protected ArrayList<LayerModel> layers = new ArrayList<LayerModel>();

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

    // MODIFIES: access vectors
    public void setAccessVectors(AccessVectorModel accessVectors) {
        this.accessVectors = accessVectors;
    }

    // EFFECT: getter for layers
    public ArrayList<LayerModel> getLayers() {
        return this.layers;
    }

    enum PolicyCapabilities {
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
        return capabilities.get(target);
    }

    // REQUIRE: policy_capability file after de-comment and strip (readAsWholeCode)
    // EFFECT: parse policy_capability file into HashMap
    @SuppressWarnings("methodlength")
    // Switch-case used
    public HashMap<PolicyCapabilities, Boolean> capabilitiesParser(String content)
            throws SyntaxParseException, UnknownCapabilityException {
        HashMap<PolicyCapabilities, Boolean> results = new HashMap<PolicyCapabilities, Boolean>();
        String[] tokenized = CommonUtil.basicTokenizer(content);
        // policycap
        for (int i = 0; i < tokenized.length; i++) {
            if (tokenized[i].equals("policycap")) {
                switch (tokenized[i]) {
                    case "network_peer_controls": results.put(PolicyCapabilities.network_peer_controls, true);
                        break;
                    case "open_perms": results.put(PolicyCapabilities.open_perms, true);
                        break;
                    case "always_check_network": results.put(PolicyCapabilities.always_check_network, true);
                        break;
                    case "extended_socket_class": results.put(PolicyCapabilities.extended_socket_class, true);
                        break;
                    case "cgroup_seclabel": results.put(PolicyCapabilities.cgroup_seclabel, true);
                        break;
                    case "nnp_nosuid_transition": results.put(PolicyCapabilities.nnp_nosuid_transition, true);
                        break;
                    case "genfs_seclabel_symlinks": results.put(PolicyCapabilities.genfs_seclabel_symlinks, true);
                        break;
                    case "ioctl_skip_cloexec": results.put(PolicyCapabilities.ioctl_skip_cloexec, true);
                        break;
                    default:
                        throw new UnknownCapabilityException(tokenized[i]);
                }
            } else {
                throw new SyntaxParseException("Unknown token when parsing capability: " + tokenized[i]);
            }
        }
        return results;
    }

    public LayerModel lookup(String layerName) {
        return null;
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
}
