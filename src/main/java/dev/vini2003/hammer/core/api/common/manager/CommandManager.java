package dev.vini2003.hammer.core.api.common.manager;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.arguments.StringArgumentType.StringType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandManager {
	private static final Map<Class<?>, Function<CommandRegistryAccess, ? extends ArgumentType<?>>> ARGUMENT_TYPE_SUPPLIERS = new HashMap<>();
	private static final Map<Class<?>, BiFunction<CommandContext<ServerCommandSource>, String, ?>> ARGUMENT_TYPE_GETTERS = new HashMap<>();
	
	public enum EntityType {
		ENTITY,
		PLAYER,
		ENTITIES,
		PLAYERS
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Command {
		String path();
		
		int permissionLevel() default 2;
	}
	
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface DoubleRange {
		double min() default Double.MIN_VALUE;
		
		double max() default Double.MAX_VALUE;
	}
	
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FloatRange {
		float min() default Float.MIN_VALUE;
		
		float max() default Float.MAX_VALUE;
	}
	
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface IntegerRange {
		int min() default Integer.MIN_VALUE;
		
		int max() default Integer.MAX_VALUE;
	}
	
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface LongRange {
		long min() default Long.MIN_VALUE;
		
		long max() default Long.MAX_VALUE;
	}
	
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface StringKind {
		StringType value();
	}
	
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface EntityKind {
		EntityType value();
	}
	
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Type {
		Class<? extends ArgumentType<?>> value();
	}
	
	public static void registerArgumentType(Class<?> type, Function<CommandRegistryAccess, ? extends ArgumentType<?>> supplier) {
		ARGUMENT_TYPE_SUPPLIERS.put(type, supplier);
	}
	
	public static void registerArgumentType(Class<?> type, Supplier<? extends ArgumentType<?>> supplier) {
		ARGUMENT_TYPE_SUPPLIERS.put(type, registry -> supplier.get());
	}
	
	public static <C> void registerArgumentTypeGetter(Class<? extends ArgumentType<?>> type, BiFunction<CommandContext<ServerCommandSource>, String, C> function) {
		ARGUMENT_TYPE_GETTERS.put(type, function);
	}
	
	public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, Class<?> clazz) {
		var headNodes = new HashMap<String, LiteralArgumentBuilder<ServerCommandSource>>();
		var allNodes = new TreeMap<String, LiteralArgumentBuilder<ServerCommandSource>>();
		
		for (var method : clazz.getMethods()) {
			if (method.isAnnotationPresent(Command.class)) {
				var commandAnnotation = method.getAnnotation(Command.class);
				var commandNames = commandAnnotation.path().split(" ");
				
				var permissionLevel = commandAnnotation.permissionLevel();
				
				var headNode = allNodes.computeIfAbsent(commandNames[0], net.minecraft.server.command.CommandManager::literal);
				headNode.requires(source -> source.hasPermissionLevel(permissionLevel));
				
				headNodes.put(commandNames[0], headNode);
				
				for (var i = 0; i < commandNames.length; i++) {
					var commandPath = "";
					
					// Create this node's command path.
					// For example, presuming the command is "hg stage start", this will be:
					// "hg", then "hg stage", then "hg stage start".
					for (var k = 0; k < i + 1 && k < commandNames.length; ++k) {
						commandPath += (k == 0 ? "" : " ") + commandNames[k];
					}
					
					// Create the command path's node.
					// If it's the first node, it's the root node.
					// Otherwise, it's a literal node with the command name.
					// For example, presuming the command is "hg stage start", this will be:
					// - "hg" -> literal("hg");
					// - "hg stage" -> literal("stage");
					// - "hg stage start" -> literal("start");
					allNodes.put(commandPath, i == 0 ? headNode : literal(commandNames[i]));
				}
				
				// When building the nodes, we must start from the back.
				// Reason being; calling "#then" on a node will build the node that was passed in.
				// Any changes done after the build will not be reflected, such as adding a child node,
				// adding an execution handler, etc.
				
				// For example, presuming the command is "hg stage start", we start
				// at the "start" command. We then add its arguments and executor,
				// and chain "stage" into "start".
				// Afterwards, we chain "hg" into "stage".
				
				// We must now add all the argument nodes to the command node, if any.
				
				var tailNode = allNodes.get(commandAnnotation.path());
				
				var parameters = method.getParameters();
				
				ArgumentBuilder<ServerCommandSource, ?> lastArgumentBuilder = null;
				
				for (int i = parameters.length - 1; i >= 1; --i) {
					var argumentType = getArgumentType(registryAccess, parameters[i]);
					var argumentBuilder = argument(parameters[i].getName(), argumentType);
					
					if (lastArgumentBuilder != null) {
						argumentBuilder.then(lastArgumentBuilder);
					} else {
						argumentBuilder.executes(context -> {
							var arguments = new Object[parameters.length];
							arguments[0] = context.getSource();
							
							for (int j = 1; j < parameters.length; j++) {
								var argumentName = parameters[j].getName();
								
								// If the argument is an EntitySelector, we msut first get the selector,
								// then use it to get the players.
								
								// We must also check for the EntityKind annotation, since that
								// changes how we use the entity selector.
								
								if (parameters[j].isAnnotationPresent(EntityKind.class)) {
									var parameterAnnotation = parameters[j].getAnnotation(EntityKind.class);
									
									var entityKind = parameterAnnotation.value();
									
									arguments[j] = switch (entityKind) {
										case PLAYER -> context.getArgument(argumentName, EntitySelector.class).getPlayer(context.getSource());
										case ENTITY -> context.getArgument(argumentName, EntitySelector.class).getEntity(context.getSource());
										case PLAYERS -> context.getArgument(argumentName, EntitySelector.class).getPlayers(context.getSource());
										case ENTITIES -> context.getArgument(argumentName, EntitySelector.class).getEntities(context.getSource());
									};
								} else {
									try {
										// Try to get the argument with the parameter's type.
										arguments[j] = context.getArgument(argumentName, parameters[j].getType());
									} catch (Exception e) {
										// If that fails, check if it has a custom Type and use that.
										var parameterAnnotation = parameters[j].getAnnotation(Type.class);
										
										if (parameterAnnotation != null) {
											var type = parameterAnnotation.value();
											
											if (!ARGUMENT_TYPE_GETTERS.containsKey(type)) {
												arguments[j] = context.getArgument(argumentName, type);
											} else {
												arguments[j] = ARGUMENT_TYPE_GETTERS.get(type).apply(context, parameters[j].getName());
											}
										}
									}
								}
							}
							
							try {
								return (int) method.invoke(clazz, arguments);
							} catch (Exception e) {
								e.printStackTrace();
								return 0;
							}
						});
					}
					
					lastArgumentBuilder = argumentBuilder;
				}
				
				// If there is an argument node, chain it into the tail node,
				// since the execution is handled by the argument node.
				if (lastArgumentBuilder != null) {
					tailNode.then(lastArgumentBuilder);
				} else {
					// Otherwise, add the execution handler to the tail node.
					tailNode.executes(context -> {
						var arguments = new Object[parameters.length];
						arguments[0] = context.getSource();
						
						try {
							return (int) method.invoke(clazz, arguments);
						} catch (Exception e) {
							e.printStackTrace();
							return 0;
						}
					});
				}
				
				// Lastly, we chain all the literal together,
				// from tail to head.
				for (var i = commandNames.length - 1; i > 0; --i) {
					var commandPath = "";
					
					for (var k = 0; k < i && k < commandNames.length; ++k) {
						commandPath += (k == 0 ? "" : " ") + commandNames[k];
					}
					
					var node = allNodes.get(commandPath);
					node.then(tailNode);
					tailNode = node;
				}
			}
		}
		
		// Lastly, we register all the nodes.
		for (var node : headNodes.values()) {
			dispatcher.register(node);
		}
	}
	
	@Nullable
	private static ArgumentType<?> getArgumentType(CommandRegistryAccess registryAccess, Parameter parameter) {
		ArgumentType<?> argumentType = null;
		
		var parameterType = parameter.getType();
		
		if (parameterType == boolean.class) {
			argumentType = BoolArgumentType.bool();
		} else if (parameterType == double.class) {
			var range = parameter.getAnnotation(DoubleRange.class);
			var min = range != null ? range.min() : Double.MIN_VALUE;
			var max = range != null ? range.max() : Double.MAX_VALUE;
			argumentType = DoubleArgumentType.doubleArg(min, max);
		} else if (parameterType == float.class) {
			var range = parameter.getAnnotation(FloatRange.class);
			var min = range != null ? range.min() : Float.MIN_VALUE;
			var max = range != null ? range.max() : Float.MAX_VALUE;
			argumentType = FloatArgumentType.floatArg(min, max);
		} else if (parameterType == int.class) {
			var range = parameter.getAnnotation(IntegerRange.class);
			var min = range != null ? range.min() : Integer.MIN_VALUE;
			var max = range != null ? range.max() : Integer.MAX_VALUE;
			argumentType = IntegerArgumentType.integer(min, max);
		} else if (parameterType == long.class) {
			var range = parameter.getAnnotation(LongRange.class);
			var min = range != null ? range.min() : Long.MIN_VALUE;
			var max = range != null ? range.max() : Long.MAX_VALUE;
			argumentType = LongArgumentType.longArg(min, max);
		} else if (parameterType == String.class) {
			var kind = parameter.getAnnotation(StringKind.class);
			var type = kind.value();
			
			argumentType = switch (type) {
				case GREEDY_PHRASE -> StringArgumentType.greedyString();
				case QUOTABLE_PHRASE -> StringArgumentType.string();
				case SINGLE_WORD -> StringArgumentType.word();
			};
		} else if (parameterType == Entity.class || parameterType == PlayerEntity.class || parameterType == ServerPlayerEntity.class) {
			var kind = parameter.getAnnotation(EntityKind.class);
			var type = kind.value();
			
			argumentType = switch (type) {
				case ENTITY -> EntityArgumentType.entity();
				case PLAYER -> EntityArgumentType.player();
				default -> null;
			};
		} else if (parameterType == List.class || parameterType == Set.class || parameterType == Collection.class) {
			var kind = parameter.getAnnotation(EntityKind.class);
			var type = kind.value();
			
			var parameterizedType = (ParameterizedType) parameter.getParameterizedType();
			var typeArguments = parameterizedType.getActualTypeArguments();
			
			if (typeArguments.length == 1) {
				var typeArgument = typeArguments[0];
				
				if (typeArgument instanceof Class<?> typeArgumentClass) {
					if (typeArgumentClass == Entity.class || typeArgumentClass == PlayerEntity.class || typeArgumentClass == ServerPlayerEntity.class) {
						argumentType = switch (type) {
							case ENTITIES -> EntityArgumentType.entities();
							case PLAYERS -> EntityArgumentType.players();
							default -> null;
						};
					}
				}
			}
		} else {
			try {
				if (parameter.isAnnotationPresent(Type.class)) {
					parameterType = parameter.getAnnotation(Type.class).value();
					
					// Try to find a default supplier for this argument type.
					var supplier = ARGUMENT_TYPE_SUPPLIERS.get(parameterType);
					
					// If there is a supplier, we use it to create the argument type.
					if (supplier != null) {
						argumentType = supplier.apply(registryAccess);
					} else {
						// Otherwise, we attempt to instantiate the argument type directly.
						argumentType = (ArgumentType<?>) parameterType.getDeclaredConstructor().newInstance();
					}
				}
			} catch (Exception ignored) {
			}
		}
		
		return argumentType;
	}
}
