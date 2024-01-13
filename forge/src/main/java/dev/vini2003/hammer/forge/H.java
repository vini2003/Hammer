package dev.vini2003.hammer.forge;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.HCClient;
import dev.vini2003.hammer.core.HCDedicatedServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod(HC.ID)
public class H {
	public H() {
		HC.onInitialize(); // Common initialization
	}
	
	@EventBusSubscriber(modid = HC.ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
	public static class Client {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			HCClient.onInitializeClient();
		}
	}
	
	@EventBusSubscriber(modid = HC.ID, value = Dist.DEDICATED_SERVER, bus = EventBusSubscriber.Bus.MOD)
	public static class Server {
		@SubscribeEvent
		public static void onServerSetup(FMLDedicatedServerSetupEvent event) {
			HCDedicatedServer.onInitializeDedicatedServer();
		}
	}
}
