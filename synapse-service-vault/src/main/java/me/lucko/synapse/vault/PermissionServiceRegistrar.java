/*
 * This file is part of synapse, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.synapse.vault;

import me.lucko.synapse.permission.PermissionService;
import me.lucko.synapse.vault.impl.VaultPermissionService;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class PermissionServiceRegistrar implements Listener {

    private final SynapseVaultPlugin plugin;
    private VaultPermissionService service = null;

    public PermissionServiceRegistrar(SynapseVaultPlugin plugin) {
        this.plugin = plugin;
    }

    public void update() {
        Permission permission = plugin.getServer().getServicesManager().load(Permission.class);
        Chat chat = plugin.getServer().getServicesManager().load(Chat.class);

        if (permission == null || chat == null) {
            // unregister our service
            unregister();
            return;
        }

        // both are non-null, try to refresh
        register(permission, chat);
    }

    private void unregister() {
        if (service != null) {
            plugin.getServer().getServicesManager().unregister(PermissionService.class, service);
            service = null;
        }
    }

    private void register(Permission permission, Chat chat) {
        if (service != null && service.getVaultPerms() == permission && service.getVaultChat() == chat) {
            return; // no change
        }

        if (permission.getName().startsWith("synapse") || chat.getName().startsWith("synapse")) {
            return;
        }

        // change
        unregister();
        service = new VaultPermissionService(permission, chat);
        plugin.getServer().getServicesManager().register(PermissionService.class, service, plugin, ServicePriority.Low);
    }

    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent e) {
        RegisteredServiceProvider<?> provider = e.getProvider();
        if (provider.getService().equals(Permission.class) || provider.getService().equals(Chat.class)) {
            update();
        }
    }

    @EventHandler
    public void onServiceUnregister(ServiceUnregisterEvent e) {
        RegisteredServiceProvider<?> provider = e.getProvider();
        if (provider.getService().equals(Permission.class) || provider.getService().equals(Chat.class)) {
            update();
        }
    }

}
