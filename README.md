# Psychosis
Psychosis is an SELinux Policy Development Studio, for UBC's CPSC 210 term project.

SELinux is one of the most powerful and complex Linux Security Module(LSM) that works as an additional Mandatory Access Control(MAC) over Linux/\*UNIX's Discretionary access control (DAC), it is based on the idea of labeling - every resource is assigned with a label and SELinux control how those labels interact with others on the level of syscalls. SELinux would need a rule set, called a "policy" to actually have rules to enforce. Policy would provide file context - telling SELinux the way of assigning labels, the definition of security classes and access vectors - basically a duplicate of the Linux kernel's definition but necessary for Type reason, rules - a 4-tuple consist of the source label, the resource label, the resource class and the action, global_booleans and global_tunables - enable/disabling certain rule sets, and policy capabilities - some broader switches of the policy. Editing SELinux policy systematically could be hard since it is not a common thing to have SELinux enforced on desktops, therefore, this project aims to provide an editing environment for SELinux policies that provide functionalities like looking up interfaces to make the process of developing a SELinux policy easier.
## Functionalities:
### Auto-complete
Psychosis will provide auto-complete on
- Keywords: neverallow, mlsconstraint, constraint, etc.
- Type names
- Attribute names
- Interface names
- Template names
### Constraint check
Psychosis provides in-edit neverallow, and dontaudit violation checks.
Psychosis will also prompt you if a rule is not in effect due to factors like policy capability.
### Hinting
Psychosis will hint you on using interfaces. When you created a rule that can be allowed via interface, you will be prompted to use that interface.

### Comment
Psychosis allow you to live-view the documentations on modules, interfaces and templates. (Provided that your policy project structure is based on the reference poliicy)

## Persistent 
All Psychosis's persistentize files are in JSON, therefore an ASCII text file.

### Workspace files (.pcsj)
A file that could be used to restore a whole Psychosis project(.te, .if and .fc)
### Workspace files (.pcsw)
Workspace files store the workspace information, including all the projects and their filesystem path.

## User Stories

As a user, I want to be able to:
- Opening and performing editing on an arbitrary number of SELinux policy module files (including policy and file context files) in the current workspace
  - Open and parse .te files into a rule set
  - Rule set model
  - .te parser (very simple parser)
  - Project model
- Opening and performing editing on an arbitrary number of SELinux policy interface files (including policy and file context files) in the current workspace

- Use interface to compile .te rulesets into full rulesets

- Add new layers (a set of Modules) to projects
- Add new Modules(a .te, .if and a .fc file) to layers
- Check the overview of Project / Layer / Modules

- Looking up an interface or an attribute that would allow a specific operation. (Only first-order statements am I planning to support)
- Have a track on new interfaces or templates that was defined by the user. (Be able to add or remove interfaces or templates to "user-defined" list)
- Able to assign interfaces or templates with Tags for easier lookup (Be able to add or remove interfaces or templates to the list corresponding to the Tag via a HashMap)
- Lookup interfaces or templates via tags, name or description.


- (EXTRA FUNCTIONALITY) Full SELinux language parser for the functionality above (I only plan to support first-order statements with no advanced supports like Interface calls and a full language parser would be extremely hard, but I will try if it's possible)
	- First-order Interface calls are already supported!

- Save project independently to a .pcsj file
- Load .pcsj file to a project in the workspace
- Save the whole program state to a .pcsw workspace file
- Load from a .pcsw workspace file to recover the whole previous program state
## Instructions
