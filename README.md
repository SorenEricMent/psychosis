![大家好啊，我是說的Psychosis！](https://github.students.cs.ubc.ca/CPSC210-2023W-T2/project_x6a8w/blob/main/data/resources/splash.jpg "Psychosis Studio")
# Psychosis
Psychosis is an SELinux Policy Development Studio.

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

- Loading and showing access vector definition
- Loading and editing capabilities

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
  - Meta ref-based format
  - Compiled format (for phase 2)
- Load .pcsj file to a project in the workspace
- Save the whole program state to a .pcsw workspace file
    - For now, comments in the original files are discarded on read.
- Load from a .pcsw workspace file to recover the whole previous program state

## Instructions
### Basics
Please run Psychosis with the given JVM parameters.
If you want to build Psychosis to a binary with Gradle, you'll need to switch the GUI designer's working mode to source code, fix some broken ResourceBundle import (Bug in IDEA), rebuild the project with IDEA and finally use `gradle build`
### Language
Psychosis has i18n support with EO, FR and EN
Psychosis's language is by default Esperanto. Psychosis also support English/French and will switch to it if the current language is a variation of it. You can select the language at the top-right corner of toolbar, but it will require a reboot. 

### Appearance
Psychosis respect and use system's theme. However, Psychosis is designed and works best under dark themes, specifically GTK theme "Mojave-dark". Using light theme might cause some components to be hard to see.
### Top bar
##### Help
For the "Help" button in the top bar, clicking it will bring you a dialog containing about info and debug info.
Inputting yuuta will trigger an easter egg uwu :heart:  :heart:  :heart: 
##### Files
Contained some common file operations, like import/export project and workspace
##### Quit
Literally quit the application, warn the user if the workspace is not saved
##### SEStatus
There is an indicator after the Help button to indicate current system's SELinux status, on non-linux platforms it will be green and "SELinux Not Available", on Linux system, if SELinux is not in LSM, it will be displayed as "SELinux Disabled", And if SELinux is in LSM, it will judge and display SELinux enforcing status.
### Workspaces & Projects operations
After booting up Psychosis, you will be greeted with a "no project selected" page. At this page, you can click "Load Project" / "Load Workspace" to load a single project or recover from a previous working state. Alternatively, those options are also available on the "File" menu in the top toolbar. 
On the left side, you will see a tree, the structure of this tree is Root-Project-Layer-Module, clicking at the Root will bring you to the welcome page, clicking the rest will bring you to the corresponding editing page (Editing Project/Layer/Module). 

#### Status panel
On the bottom-right of the editor, you will have a status indicator showing if any unsaved changes have been made and if Psychosis is busy, this is still in development and might not capture some(most) specific edits.
### Project Editing
You can create new Projects with "Create Project" button in the greeting page, which can be navigated by clicking Projects in the Tree, Since no live saving is required and Psychosis didn't do diff-based editing, the create project option now only have the option to create a new empty project in memory.
You can add multiple layers to the project in the project editing page.

After you clicked a project in the tree, you will be greeted with its editing panel, it will show you the layer/module count for it, a button to add new layer - clicking it will give you a dialog to add new empty unique layer, the list of capabilities - clicking on it will switch the capability status of that row, Security Vector section - the list of security classes and vectors, clicking on a security class will display its vectors on the right side, buttons to add new class or access vector, a button to load external access vector definition (not finished on gui part), and a button to load builtin, newest refpolicy definition.

You can export a project solely in addition to exporting the whole workspace.
### Layer Editing
You can add multiple modules to a layer in the layer editing page
You can remove them with the popup menu poped on right click
### Module Editing
You can edit the three core aspects of a SELinux Module by clicking the module in the tree, 
after selected a module, you will be greeted with a three-tabbed panel
#### Type Enforcement
You can export the current .te file, or compiled and export, with the button at the bottom-right
##### Adding new rules
At the Type Enforcement panel, you can add new first order rules to the policy module, rules that are the same in the 4-tuple will be automatically merged, the way you add action is - you will need to have that class defined, and you will be able to search for its access vector definition for action by typing its name in the class section, and you can click "Add action" to add the action showed in the combo box, the search is debounced.
##### Adding new interface call
Adding new interface call is similar to adding a rule, you type in interface's name and you will be able to search interface defined project-wide, the way you add param is also similar to the way you add action to rules. You will be showed the preview of the function call(todo, not done on gui part)
#### Interface
In the interface tab, you will be showed the list of this module's interfaces, by click
You can import a SELinux .if file in the button below and it will overwrite all interfaces declared.
You can add new empty interface with the buttom below
Clicking on a interface will show you its rules at the right side, you will be able to add rules the same way you add rules in Type Enforcement
#### File Context
No relevant functionality for this stage


## Phase 4: Task 2
Example logs:
```
Tue Apr 02 23:14:26 PDT 2024
Initialized new Tracker
Tue Apr 02 23:14:26 PDT 2024
Created new projectTemp at null
Tue Apr 02 23:14:26 PDT 2024
The last project is created as temporary
Tue Apr 02 23:14:26 PDT 2024
Initialized new Tracker
Tue Apr 02 23:14:34 PDT 2024
Initialized new Tracker
Tue Apr 02 23:14:34 PDT 2024
Created new projectwinslow at null
Tue Apr 02 23:14:34 PDT 2024
The last project is created as temporary
Tue Apr 02 23:14:34 PDT 2024
Initialized new Tracker
Tue Apr 02 23:14:42 PDT 2024
Added action 123 to security class 123123 at Model1839330406
Tue Apr 02 23:14:53 PDT 2024
Added action abc123 to security class abc at Model1839330406
Tue Apr 02 23:15:23 PDT 2024
Adding statement allow winslow winslow:abc { abc123 }; to TypeEnf model test_module
Tue Apr 02 23:15:26 PDT 2024
Added interface yuuta to interface set 315427815
Tue Apr 02 23:15:26 PDT 2024
Added interface yuuta to interface set 1984165751
Tue Apr 02 23:15:40 PDT 2024
Added rule allow yuuta $1:abc { abc123 }; to interface yuuta
Tue Apr 02 23:15:50 PDT 2024
Adding interface call yuuta(2333)  to TypeEnf model test_module
Tue Apr 02 23:15:50 PDT 2024
Added new macro rule: replace$1 to 2333, for 574864900
Tue Apr 02 23:15:50 PDT 2024
Adding statement allow yuuta 2333:abc { abc123 }; to TypeEnf model _
Tue Apr 02 23:15:58 PDT 2024
Added layer testlayer to winslow
Tue Apr 02 23:16:01 PDT 2024
Enabled network_peer_controls capability for winslow
Tue Apr 02 23:16:01 PDT 2024
Enabled open_perms capability for winslow
```
## Phase 4: Task 3 : Refactor Todo:
1. Lots of actions involved an unique add action - adding a X to Y with a unique name, which should be generalized with a UniqueAdd interface so that the GUI components could utilize this and lots of corresponding dialogs can be merged.
2. As stated in the previous, lots of dialogs operates on unique add, I had started working on a superclass 
3. There is a heavy dependency on the GraphicalInterface object which should be replaced with more closures.
4. Everything operates on Project and Workspace model is not actually used as a model but as an intermediate form of storing states.
