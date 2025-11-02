package stev6.ConfigurableShields;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Commands {

  public Commands(ConfigurableShields plugin) {
    LiteralCommandNode<CommandSourceStack> root =
        io.papermc.paper.command.brigadier.Commands.literal("configurableshields").requires(s -> s.getSender().hasPermission("configurableshields.admin"))
            .then(
                io.papermc.paper.command.brigadier.Commands.literal("reload")
                    .executes(
                        ctx -> {
                          plugin.reload();
                          ctx.getSource()
                              .getSender()
                              .sendMessage(
                                  MiniMessage.miniMessage()
                                      .deserialize(
                                          plugin
                                              .getConfig()
                                              .getString(
                                                  "messages.reload",
                                                  "<dark_green>Reloaded successfully!</dark_green>")));
                          return Command.SINGLE_SUCCESS;
                        }))
            .build();
    plugin
        .getLifecycleManager()
        .registerEventHandler(
            LifecycleEvents.COMMANDS, commands -> commands.registrar().register(root));
  }
}
