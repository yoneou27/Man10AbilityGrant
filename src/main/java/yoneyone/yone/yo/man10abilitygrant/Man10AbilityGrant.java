package yoneyone.yone.yo.man10abilitygrant;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Man10AbilityGrant extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equals("mag")){
            sender.sendMessage("そのコマンドはないはずです、製作者に問い合わせてください");
            return true;
        }
        if (args.length == 0){
            onCommandSub(sender,command,"mag",args);
            return true;
        }
        String[] args2 = new String[args.length - 1];
        System.arraycopy(args, 1, args2, 0, args.length - 1);
        switch (args[0]){
            case "go":
                onCommandSub(sender,command,"magg",args2);
                break;
            case "player":
                onCommandSub(sender,command,"magp",args2);
                break;
            case "id":
                onCommandSub(sender,command,"magi",args2);
                break;
            default:
                onCommandSub(sender,command,"mag",args2);
        }
        return true;
    }

    private void onCommandSub(CommandSender sender, Command command, String label, String[] args) {
        //事前準備終了
        if (!sender.hasPermission("yone.mag.op")){
            sender.sendMessage("§4You do not have permission");
            return;
        }
        switch (label) {
            case "magg":
                if (!(sender instanceof Player)){
                    sender.sendMessage("§4Execute this command from the player");
                    return;
                }
                Player player = (Player) sender;
                PlayerInventory inventory = player.getInventory();
                int mainHand = inventory.getHeldItemSlot();
                ItemStack itemStack = inventory.getItem(mainHand);
                if (itemStack == null) {
                    player.sendMessage("§4You don't have item in hand");
                    return;
                }
                net.minecraft.server.v1_12_R1.ItemStack itemStack2 = CraftItemStack.asNMSCopy(itemStack);
                NBTTagCompound compound = (itemStack2.hasTag()) ? itemStack2.getTag() : new NBTTagCompound();
                if (compound == null){
                    player.sendMessage("§7There was an error, please try again");
                    return;
                }
                int i = 0;
                boolean e = true;
                boolean a = true;
                boolean b = true;
                for (String type:args){
                    switch (type){
                        case "e":
                            if (e) {
                                i += 1;
                                e = false;
                                player.sendMessage("§7Hid the enchantment");
                            }
                            break;
                        case "a":
                            if (a) {
                                i += 2;
                                a = false;
                                player.sendMessage("§7Hid ability");
                            }
                            break;
                        case "b":
                            if (b) {
                                i += 4;
                                b = false;
                                player.sendMessage("§7granted unbreakable");
                            }
                            break;
                        default:
                            player.sendMessage("§7Invalid type:"+ type);
                            break;
                    }
                }
                if (!b) {//隠されているならば不可懐を付与
                    compound.set("Unbreakable", new NBTTagByte((byte) 1));
                }
                compound.set("HideFlags", new NBTTagInt(i));
                itemStack2.setTag(compound);
                itemStack = CraftItemStack.asBukkitCopy(itemStack2);
                inventory.setItem(mainHand, itemStack);
                break;
            case "mag":
                sender.sendMessage("§e§l/magコマンドの使い方");
                //ここに付与効果の説明の詳細
                sender.sendMessage("§eメインコマンド/mag go [引数]");
                sender.sendMessage("§e・不可懐と不可懐隠し→b");
                sender.sendMessage("§e・エンチャ隠し→e");
                sender.sendMessage("§e・能力隠し→a");
                sender.sendMessage("§e引数を何も書かないと、全て表示されるようになります");
                sender.sendMessage("§eコマンドの実行二回目以降は上書きです");
                sender.sendMessage("§e同じ引数を2回以上使った場合一つ目のみ動作します");
                sender.sendMessage("§e/mag player <プレイヤー名> [mag goコマンドの引数]… で遠隔実行ができます");
                sender.sendMessage("§e/mag id <プレイヤー名> <耐久値> [mag goコマンドの引数]…");
                sender.sendMessage("§eで対象のアイテム(複数個あれば複数)に付与できます");
                break;
            case "magp":
                if (args.length == 0){
                    sender.sendMessage("§7Command usage: /magp <player name> [magg command argument]…");
                    return;
                }
                Player goPlayer = Bukkit.getServer().getPlayer(args[0]);
                if (goPlayer == null){
                    sender.sendMessage("§7No target player found");
                    return;
                }
                String[] args2 = new String[args.length - 1];
                System.arraycopy(args, 1, args2, 0, args.length - 1);
                onCommand(goPlayer,command,"magg",args2);
                sender.sendMessage("§7magp command executed");
                break;
            case "magi":///magi <プレイヤー> <耐久値> [引数abe]
                if (args.length <= 1){
                    sender.sendMessage("§7Command usage: /magi <player name> <durable value> [argument a b e]");
                    return;
                }
                Player executePlayer = Bukkit.getServer().getPlayer(args[0]);
                if (executePlayer == null){
                    sender.sendMessage("§7No target player found");
                    return;
                }
                int durableInt = int1562(Integer.parseInt(args[1]));
                durableInt -= 1;
                String[] args3 = new String[args.length - 2];
                System.arraycopy(args, 2, args3, 0, args.length - 2);
                PlayerInventory getInv = executePlayer.getInventory();
                List<ItemStack> diamondHoes = new ArrayList<>();
                List<Integer> nums = new ArrayList<>();
                for (int l = 0;l < 36;l++){
                    ItemStack item = getInv.getItem(l);
                    if (item == null){
                        continue;
                    }
                    if (item.getType().equals(Material.DIAMOND_HOE)){
                        if (item.getDurability() == durableInt){
                            diamondHoes.add(item);
                            nums.add(l);
                        }
                    }
                }
                if (diamondHoes.isEmpty()){
                    sender.sendMessage("§7No targets found");
                    return;
                }
                //ここから作業：diamondHoesがitemStackList
                int i2 = 0;
                boolean e2 = true;
                boolean a2 = true;
                boolean b2 = true;
                for (String type : args3) {
                    switch (type) {
                        case "e":
                            if (e2) {
                                i2 += 1;
                                e2 = false;
                                sender.sendMessage("§7Hid the enchantment");
                            }
                            break;
                        case "a":
                            if (a2) {
                                i2 += 2;
                                a2 = false;
                                sender.sendMessage("§7Hid ability");
                            }
                            break;
                        case "b":
                            if (b2) {
                                i2 += 4;
                                b2 = false;
                                sender.sendMessage("§7granted unbreakable");
                            }
                            break;
                        default:
                            sender.sendMessage("§7Invalid type:" + type);
                            break;
                    }
                }
                for (int l = 0;l < diamondHoes.size();l++) {
                    ItemStack diamondHoe = diamondHoes.get(l);
                    net.minecraft.server.v1_12_R1.ItemStack itemStack3 = CraftItemStack.asNMSCopy(diamondHoe);
                    NBTTagCompound compound2 = (itemStack3.hasTag()) ? itemStack3.getTag() : new NBTTagCompound();
                    if (compound2 == null) {
                        sender.sendMessage("§7There was an error");
                        continue;
                    }
                    if (!b2) {//隠されているならば不可懐を付与
                        compound2.set("Unbreakable", new NBTTagByte((byte) 1));
                    }
                    compound2.set("HideFlags", new NBTTagInt(i2));
                    itemStack3.setTag(compound2);
                    diamondHoe = CraftItemStack.asBukkitCopy(itemStack3);
                    getInv.setItem(nums.get(l), diamondHoe);
                }
                sender.sendMessage("§7All done! "+ diamondHoes.size() +" completed");
                break;
        }
    }
    private int int1562(int i){
        return 1562 - i;
    }
}
