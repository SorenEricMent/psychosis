package model.policy;

import model.*;
import model.exception.NotFoundException;
import model.exception.SyntaxParseException;

import java.util.ArrayList;
import java.util.Arrays;

// A set of interfaces as a module could contain multiple interfaces

public class InterfaceSetModel extends FileObjectModel implements Encodeable, Decodeable {

    private final ArrayList<InterfaceModel> interfaces = new ArrayList<>();

    // EFFECTS: override default object toString, concatenate all toString result of interfaces in the set.
    @Override
    public String toString() {
        String res = "";
        for (InterfaceModel i : interfaces) {
            res = res.concat(i.toString() + "\n");
        }
        return res;
    }

    // EFFECTS: return number of interfaces
    public int lineCount() {
        return interfaces.size();
    }

    // EFFECTS: add an interface to the set (notice this model store references)
    // MODIFIES: this
    public void addInterface(InterfaceModel i) {
        this.interfaces.add(i);
        EventLog.getInstance().logEvent(new Event(
                "Added interface " + i.getName() + " to interface set " + hashCode()));
    }

    public ArrayList<InterfaceModel> getInterfaces() {
        return interfaces;
    }

    // EFFECTS: lookup interface with name, throw NotFoundException if none found
    public InterfaceModel getInterface(String name) {
        for (InterfaceModel i : this.interfaces) {
            if (i.getName().equals(name)) {
                return i;
            }
        }
        throw new NotFoundException("Interface not found: " + name);
    }

    // EFFECTS: update all interfaces' owner
    // MODIFIES: this
    public void setOwners(String owner) {
        EventLog.getInstance().logEvent(new Event(
                "Set owner for all interfaces in " + hashCode() + " to " + owner));
        for (InterfaceModel i : interfaces) {
            i.setOwner(owner);
        }
    }

    // EFFECTS: remove the interface with a specific name from the set
    public void removeInterface(String interfaceName) {
        int position = -1;
        for (int i = 0; i < interfaces.size(); i++) {
            if (interfaces.get(i).getName().equals(interfaceName)) {
                position = i;
                break;
            }
        }
        if (position == -1) {
            throw new NotFoundException("Interface not found.");
        } else {
            EventLog.getInstance().logEvent(new Event(
                    "Removed interface " + interfaces.get(position).getName() + " from interface set" + hashCode()));
            interfaces.remove(position);
        }
    }

    // EFFECTS: parser for SELinux .if file
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public static InterfaceSetModel parser(String str) throws SyntaxParseException {
        // AN interface file could contain multiple interface definitions
        // In the design of psychosis, there is no difference between Interface
        // and templates as syntactically speaking template is just interface with more statement types
        InterfaceSetModel res = new InterfaceSetModel();
        String[] tokenized = CommonUtil.strongTokenizer(str);
        for (int i = 0; i < tokenized.length; i++) {
            if (tokenized[i].equals("interface") || tokenized[i].equals("template")) {
                // Expect next three token to resemble interface name
                // Assume the syntax inside the interface definition is correct to
                // use Balancer to find the end, as incorrectly ended prev interface will cause exception later
                if (!tokenized[i + 1].equals("(") || !tokenized[i + 2].equals("`")
                        || !tokenized[i + 4].equals("'") || !tokenized[i + 5].equals(",")) {
                    throw new SyntaxParseException("Broken interface/template define syntax.");
                }
                InterfaceModel interfaceToAdd = new InterfaceModel(tokenized[i + 3], "_placehold", false);
                int endDefinition = -1;
                CommonUtil.Balancer findEnd = new CommonUtil.Balancer(); // When this is balanced, the end is found
                for (int j = i + 6; j < tokenized.length; j++) {
                    findEnd.push(tokenized[j]);
                    if (findEnd.isSyntaxError()) {
                        throw new SyntaxParseException("Unbalanced parentheses.");
                    }
                    if (findEnd.check()) {
                        // The definition is closed
                        endDefinition = j;
                        break;
                    }
                    if (RuleSetModel.isProcessed(tokenized[j])) {
                        int statementEnd = getStatementEnd(j, tokenized);
                        interfaceToAdd.addStatement(
                                RuleSetModel.ruleSetParser(Arrays.copyOfRange(tokenized, j, statementEnd)));
                        j = statementEnd - 1;
                    }
                }
                res.addInterface(interfaceToAdd);
                if (endDefinition == -1) {
                    throw new SyntaxParseException("Cannot find the end of the definition after exhaustion.");
                }
                i = endDefinition;
            }
        }
        return res;
    }

    // EFFECTS: return the first occurrence of element ; from j in array tokenized
    private static int getStatementEnd(int j, String[] tokenized) throws SyntaxParseException {
        int statementEnd = -1;
        // Found a statement, try to find the ending ;
        for (int k = j + 1; k < tokenized.length; k++) {
            if (tokenized[k].equals(";")) {
                statementEnd = k;
                break;
            }
        }
        if (statementEnd == -1) {
            throw new SyntaxParseException("Cannot find the end of the statement after exhaustion.");
        }
        return statementEnd + 1;
    }
}
