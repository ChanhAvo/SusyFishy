package Controls;
import Entity.NPC_Merchant;
import Object.OBJ_Shop;
public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }
    public void setObject(){
        gp.obj[0] = new OBJ_Shop();
        gp.obj[0].worldX = 13 * gp.tileSize;
        gp.obj[0].worldY = 5 * gp.tileSize;
    }
    public void setNPC(){
        gp.npc[0] = new NPC_Merchant(gp);
        gp.npc[0].worldX = 13 * gp.tileSize;
        gp.npc[0].worldY = 4 * gp.tileSize;
    }
}
