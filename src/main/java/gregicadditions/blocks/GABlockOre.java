package gregicadditions.blocks;

import gregtech.api.unification.material.type.DustMaterial;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.ore.StoneType;
import gregtech.common.blocks.BlockOre;

public class GABlockOre extends BlockOre {

    private OrePrefix orePrefix;
    public DustMaterial material;

    public GABlockOre(DustMaterial material, StoneType[] allowedValues, OrePrefix orePrefix) {
        super(material, allowedValues);
        this.orePrefix = orePrefix;
        this.material = material;
    }

    public OrePrefix getOrePrefix() {
        return orePrefix;
    }
}
