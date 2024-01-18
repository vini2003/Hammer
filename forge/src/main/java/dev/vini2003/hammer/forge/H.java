package dev.vini2003.hammer.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.HCClient;
import dev.vini2003.hammer.core.HCDedicatedServer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HC.ID)
public class H {
	private final IEventBus eventBus;
	
	public H() {
		eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		EventBuses.registerModEventBus(HC.ID, eventBus);
		
		eventBus.addListener(this::onCommonSetup);
		eventBus.addListener(this::onClientSetup);
		eventBus.addListener(this::onServerSetup);
	}
	
	private void onCommonSetup(FMLCommonSetupEvent event) {
		HC.onInitialize();
	}
	
	private void onClientSetup(FMLClientSetupEvent event) {
		HCClient.onInitializeClient();
	}
	
	private void onServerSetup(FMLDedicatedServerSetupEvent event) {
		HCDedicatedServer.onInitializeDedicatedServer();
	}
}
