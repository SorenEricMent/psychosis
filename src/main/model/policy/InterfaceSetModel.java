package model.policy;

import model.Decodeable;
import model.Encodeable;
import model.FileObjectModel;
import model.Pair;
import model.exception.NotFoundException;

import java.io.File;
import java.util.ArrayList;

public class InterfaceSetModel extends FileObjectModel implements Encodeable, Decodeable {

    private ArrayList<InterfaceModel> interfaces = new ArrayList<>();

    public String toString() {
        String res = "";
        for (InterfaceModel i : interfaces) {
            res = res.concat(interfaces.toString() + "\n");
        }
        return res;
    }

    // EFFECTS: return number of interfaces
    public int lineCount() {
        return interfaces.size();
    }

    public void addInterface(InterfaceModel i) {
        this.interfaces.add(i);
    }

    public void removeinterface(String interfaceName) {
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
            interfaces.remove(position);
        }
    }

    public static InterfaceSetModel parser(String str) {
        return null;
    }
}
