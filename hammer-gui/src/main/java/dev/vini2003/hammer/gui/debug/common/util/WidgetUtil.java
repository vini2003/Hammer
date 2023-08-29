package dev.vini2003.hammer.gui.debug.common.util;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.arrow.ArrowWidget;
import dev.vini2003.hammer.gui.api.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.bar.ImageBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.bar.SpriteBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.button.ButtonWidget;
import dev.vini2003.hammer.gui.api.common.widget.image.ImageWidget;
import dev.vini2003.hammer.gui.api.common.widget.item.ItemStackWidget;
import dev.vini2003.hammer.gui.api.common.widget.item.ItemWidget;
import dev.vini2003.hammer.gui.api.common.widget.list.ListWidget;
import dev.vini2003.hammer.gui.api.common.widget.list.slot.SlotListWidget;
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import dev.vini2003.hammer.gui.api.common.widget.tab.TabWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextAreaWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextFieldWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextWidget;
import dev.vini2003.hammer.gui.api.common.widget.toggle.ToggleWidget;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class WidgetUtil {
	public static List<Widget> createAll() {
		var list = new ArrayList<Widget>();
		list.addAll(createArrows());
		list.addAll(createFluidBars());
		list.addAll(createImageBars());
		list.addAll(createSpriteBars());
		list.addAll(createButtons());
		list.addAll(createImages());
//		list.addAll(createItemStacks());
//		list.addAll(createItems());
		list.addAll(createLists());
		list.addAll(createPanels());
		list.addAll(createSlots());
		list.addAll(createSlotLists());
		list.addAll(createTabs());
//		list.addAll(createTextAreas());
//		list.addAll(createTextFields());
//		list.addAll(createTexts());
		list.addAll(createToggles());
		return list;
	}
	
	public static List<Widget> createArrows() {
		var horizontalArrow = new ArrowWidget();
		horizontalArrow.setHorizontal(true);
		horizontalArrow.setMaximum(() -> 100.0D);
		horizontalArrow.setCurrent(() -> System.currentTimeMillis() % 1000 / 10.0D);
		
		var verticalArrow = new ArrowWidget();
		verticalArrow.setVertical(true);
		verticalArrow.setMaximum(() -> 100.0D);
		verticalArrow.setCurrent(() -> System.currentTimeMillis() % 1000 / 10.0D);
		
		return List.of(horizontalArrow, verticalArrow);
	}
	
	public static List<Widget> createFluidBars() {
		var storageView = SingleFluidStorage.withFixedCapacity(1000, () -> {});
		try (var transaction = Transaction.openOuter()) {
			storageView.insert(FluidVariant.of(Fluids.WATER), 1000, transaction);
			transaction.commit();
		}
		
		var horizontalFluidBar = new FluidBarWidget();
		horizontalFluidBar.setHorizontal(true);
		horizontalFluidBar.setStorageView(() -> storageView);
		
		var verticalFluidBar = new FluidBarWidget();
		verticalFluidBar.setVertical(true);
		verticalFluidBar.setStorageView(() -> storageView);
		
		return List.of(horizontalFluidBar, verticalFluidBar);
	}
	
	public static List<Widget> createImageBars() {
		var horizontalImageBar = new ImageBarWidget();
		horizontalImageBar.setHorizontal(true);
		
		var verticalImageBar = new ImageBarWidget();
		verticalImageBar.setVertical(true);
		
		return List.of(horizontalImageBar, verticalImageBar);
	}
	
	public static List<Widget> createSpriteBars() {
		var horizontalSpriteBar = new SpriteBarWidget();
		horizontalSpriteBar.setHorizontal(true);
		
		var verticalSpriteBar = new SpriteBarWidget();
		verticalSpriteBar.setVertical(true);
		
		if (InstanceUtil.isClient()) {
			var client = InstanceUtil.getClient();
			
			var stoneSprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/stone"));
			var dirtSprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/dirt"));
			
			horizontalSpriteBar.setBackgroundSprite(() -> stoneSprite);
			horizontalSpriteBar.setForegroundSprite(() -> dirtSprite);
			
			
			verticalSpriteBar.setBackgroundSprite(() -> stoneSprite);
			verticalSpriteBar.setForegroundSprite(() -> dirtSprite);
		}
		
		return List.of(horizontalSpriteBar, verticalSpriteBar);
	}
	
	public static List<Widget> createButtons() {
		var button = new ButtonWidget();
		
		var labelledButton = new ButtonWidget();
		labelledButton.setLabel(() -> Text.literal("Labelled Button"));
		
		return List.of(button, labelledButton);
	}
	
	public static List<Widget> createImages() {
		var image = new ImageWidget();
		
		return List.of(image);
	}
	
	public static List<Widget> createItemStacks() {
		var itemStack = new ItemStackWidget();
		itemStack.setItemStack(() -> Items.EMERALD.getDefaultStack());
		
		return List.of(itemStack);
	}
	
	public static List<Widget> createItems() {
		var item = new ItemWidget();
		item.setItem(() -> Items.EMERALD);
		
		return List.of(item);
	}
	
	public static List<Widget> createSlotLists() {
		var inventory = new SimpleInventory(9 * 6);
		for (var i = 0; i < inventory.size(); ++i) {
			// get random from registry item and set stack
			var randomItem = Registries.ITEM.getRandom(Random.create()).orElse(Items.AIR.getRegistryEntry()).value();
			inventory.setStack(i, new ItemStack(randomItem, (int) (Math.random() * 64)));
		}
		
		var slotList = new SlotListWidget(inventory, 6, 3, 0);
		
		return List.of(slotList);
	}
	
	public static List<Widget> createLists() {
		var list = new ListWidget();
		
		for (var i = 0; i < 16; ++i) {
			list.add(new ImageWidget());
		}
		
		return List.of(list);
	}
	
	public static List<Widget> createPanels() {
		var panel = new PanelWidget();
		
		return List.of(panel);
	}
	
	public static List<Widget> createSlots() {
		var inventory = new SimpleInventory(1);
		
		var slot = new SlotWidget(inventory, 0, Slot::new);
		
		return List.of(slot);
	}
	
	public static List<TabWidget> createTabs() {
		var tab = new TabWidget();
		tab.addTab(() -> Items.COPPER_INGOT.getDefaultStack());
		tab.addTab(() -> Items.IRON_INGOT.getDefaultStack());
		tab.addTab(() -> Items.GOLD_INGOT.getDefaultStack(), () -> List.of(Text.literal("Gold")));
		tab.addTab(() -> Items.EMERALD.getDefaultStack(), () -> List.of(Text.literal("Emerald")));
		
		return List.of(tab);
	}
	
	public static List<Widget> createTextAreas() {
		var textArea = new TextAreaWidget();
		textArea.setText("Hello, world!");
		
		return List.of(textArea);
	}
	
	public static List<Widget> createTextFields() {
		var textField = new TextFieldWidget();
		textField.setText("Hello, world!");
		
		return List.of(textField);
	}
	
	public static List<Widget> createTexts() {
		var text = new TextWidget();
		text.setText(Text.literal("Hello, world!"));
		
		return List.of(text);
	}
	
	public static List<Widget> createToggles() {
		var toggle = new ToggleWidget();
		
		return List.of(toggle);
	}
}
