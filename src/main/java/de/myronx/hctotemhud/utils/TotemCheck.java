package de.myronx.hctotemhud.utils;

import de.myronx.hctotemhud.HcTotemHud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class TotemCheck {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Map<String, Integer> totemChargeCache = new HashMap<>();
    private static int lastKnownInvValue = -1;

    public static int countTotemCharges() {
        if (hasInventoryChanged()) {
            updateCache();
        }
        return totemChargeCache.getOrDefault("totalCharges", 0);
    }

    private static boolean hasInventoryChanged() {
        int currentInvValue = client.player.getInventory().getChangeCount();
        if (currentInvValue != lastKnownInvValue) {
            lastKnownInvValue = currentInvValue;
            return true;
        }
        return false;
    }

    private static void updateCache() {
        int charges = 0;

        for (ItemStack stack : client.player.getInventory().main) {
            if (stack.getItem() == Items.TOTEM_OF_UNDYING && stack.hasNbt()) {
                NbtCompound nbt = stack.getNbt();

                charges += parseTotemChargesFromNbt(nbt);

            } else if (stack.hasNbt()) {
                NbtCompound nbt = stack.getNbt();
                if (nbt.contains("__data")) {
                    charges += nbt.getCompound("__data").getInt("charge");
                }
            }
        }

        totemChargeCache.put("totalCharges", charges);
    }

    public static int parseTotemChargesFromNbt(NbtCompound nbt) {
        int charges = 0;

        if (nbt != null && nbt.contains("totem_uses")) {
            NbtList loreList = nbt.getCompound("display").getList("Lore", NbtElement.STRING_TYPE);
            for (int i = 0; i < loreList.size(); i++) {
                String loreString = loreList.getString(i);
                try {
                    JsonObject jsonObject = JsonParser.parseString(loreString).getAsJsonObject();
                    if (jsonObject.has("extra")) {
                        JsonArray extraArray = jsonObject.getAsJsonArray("extra");
                        if (extraArray.size() > 1) {
                            JsonObject extraObject = extraArray.get(1).getAsJsonObject();
                            if (extraObject.has("color") && extraObject.has("text")) {
                                if (extraObject.get("color").getAsString().equals("red")) {
                                    int totemCharges = Integer.parseInt(extraObject.get("text").getAsString());
                                    charges += totemCharges;
                                }
                            }
                        }
                    }
                } catch (JsonParseException e) {
                    HcTotemHud.LOGGER.error("Fehler beim Parsen der Totem-Ladungen: " + e.getMessage());
                } catch (NumberFormatException e) {
                    HcTotemHud.LOGGER.error("Fehler beim Konvertieren der Totem-Ladungen: " + e.getMessage());
                }
            }
        }
        return charges;
    }
}
