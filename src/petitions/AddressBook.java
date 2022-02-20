package petitions;

import java.util.ArrayList;

class AddressBook extends ArrayList<String>{

    public AddressBook() {

    }

    public void addAddress(String toAdd) {
        this.add(toAdd);
    }

    public void removeAddress(String toDel) {
        this.remove( this.indexOf(toDel) );
    }

}
