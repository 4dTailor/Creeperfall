package io.github.redstoneparadox.creeperfall.game.shop;

import io.github.redstoneparadox.creeperfall.game.CreeperfallActive;
import io.github.redstoneparadox.creeperfall.game.participant.CreeperfallParticipant;
import io.github.redstoneparadox.creeperfall.game.participant.Upgrade;
import io.github.redstoneparadox.creeperfall.game.util.OrderedTextReader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.*;
import xyz.nucleoid.plasmid.shop.Cost;
import xyz.nucleoid.plasmid.shop.ShopEntry;
import xyz.nucleoid.plasmid.shop.ShopUi;

import java.util.List;

public class CreeperfallShop {
	private static final OrderedTextReader READER = new OrderedTextReader();

	public static ShopUi create(CreeperfallParticipant participant, CreeperfallActive game, CreeperfallShopConfig shopConfig) {
		return ShopUi.create(new LiteralText("Shop"), shop -> {
			shop.add(upgrade(participant, participant.armorUpgrade));
			shop.add(summonGuardian(game, shopConfig));
		});
	}

	private static ShopEntry summonGuardian(CreeperfallActive game, CreeperfallShopConfig shopConfig) {
		ShopEntry entry = ShopEntry.
				ofIcon(Items.GUARDIAN_SPAWN_EGG)
				.withName(new LiteralText("Spawn Guardian"));

		List<OrderedText> wrapped = wrapText(new LiteralText("Summons a Guardian to shoot down creepers that get too close. Despawns after 30 seconds."));
		for (OrderedText orderedText : wrapped) {
			Text text = READER.read(orderedText);
			entry.addLore(text);
		}

		entry
				.withCost(Cost.ofEmeralds(shopConfig.guardianEggPrice))
				.onBuy(playerEntity -> game.spawnGuardian());

		return entry;
	}

	private static ShopEntry upgrade(CreeperfallParticipant participant, Upgrade upgrade) {
		ItemStack icon = upgrade.getIcon();

		return ShopEntry
				.ofIcon(icon)
				.withName(new LiteralText("Upgrade Armor"))
				.withCost(Cost.ofEmeralds(1))
				.onBuy(playerEntity -> upgrade.upgrade(participant));
	}

	private static List<OrderedText> wrapText(StringVisitable text) {
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		return textRenderer.wrapLines(text, 24 * 8);
	}
}
