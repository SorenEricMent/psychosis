package model.policy;

import java.util.HashMap;

public class PolicyModel {
    // A policy contain:
    // An access vector definition and corresponding security class definition
    // A set of FileContext
    // A set of Interface
    // A set of Attributes
    // A set of constraint rules (Psychosis don't support MLSConstrain or constrain, only dontaudit)
    // Other misc parameters;

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

    private HashMap<PolicyCapabilities, Boolean>
            capabilities = new HashMap<PolicyCapabilities, Boolean>();

    public Boolean checkCapability(PolicyCapabilities target) {
        return capabilities.get(target);
    }

    public void capabilitiesParser(){

    }


}
