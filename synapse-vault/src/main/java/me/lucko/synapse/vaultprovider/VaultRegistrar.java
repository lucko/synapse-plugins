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

package me.lucko.synapse.vaultprovider;

import me.lucko.synapse.permission.PermissionService;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class VaultRegistrar implements Listener {

    private final VaultPlugin plugin;
    private SynapseVaultPermission permission = null;
    private SynapseVaultChat chat = null;

    public VaultRegistrar(VaultPlugin plugin) {
        this.plugin = plugin;
    }

    public void update() {
        PermissionService service = plugin.getServer().getServicesManager().load(PermissionService.class);

        if (service == null) {
            // unregister our services
            unregister(true);
            return;
        }

        // try to refresh
        register(service);
    }

    private void unregister(boolean notify) {
        if (permission != null) {
            if (notify) {
                plugin.getLogger().info("Unregistering synapse Vault Permission due to PermissionService provider change.");
            }
            plugin.getServer().getServicesManager().unregister(Permission.class, permission);
            permission = null;
        }
        if (chat != null) {
            if (notify) {
                plugin.getLogger().info("Unregistering synapse Vault Chat due to PermissionService provider change.");
            }
            plugin.getServer().getServicesManager().unregister(Chat.class, chat);
            chat = null;
        }
    }

    private void register(PermissionService service) {
        if (permission != null && permission.service == service && chat != null && chat.service == service) {
            return; // no change
        }

        if (service.getProviderName().startsWith("synapse")) {
            return;
        }

        // change
        unregister(false);
        plugin.getLogger().info("Providing Vault Permission/Chat using synapse: " + service.getProviderName());
        permission = new SynapseVaultPermission(service);
        chat = new SynapseVaultChat(service, permission);
        plugin.getServer().getServicesManager().register(Permission.class, permission, plugin, ServicePriority.High);
        plugin.getServer().getServicesManager().register(Chat.class, chat, plugin, ServicePriority.High);
    }

    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent e) {
        RegisteredServiceProvider<?> provider = e.getProvider();
        if (provider.getService().equals(PermissionService.class)) {
            update();
        }
    }

    @EventHandler
    public void onServiceUnregister(ServiceUnregisterEvent e) {
        RegisteredServiceProvider<?> provider = e.getProvider();
        if (provider.getService().equals(PermissionService.class)) {
            update();
        }
    }
}
