package net.skypoufy.amulets.item;

public class AmuletCreateItem extends AmuletBaseItem {

    public AmuletCreateItem(Properties settings) {
        super(settings);
        this.allowedMod = "create";
        addModToModList();
    }
}
