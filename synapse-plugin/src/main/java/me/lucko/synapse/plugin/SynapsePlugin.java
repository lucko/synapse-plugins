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

package me.lucko.synapse.plugin;

import me.lucko.synapse.permission.PermissionService;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SynapsePlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        RegisteredServiceProvider<PermissionService> provider = getServer().getServicesManager().getRegistration(PermissionService.class);
        if (provider == null) {
            getLogger().info("No PermissionService registration present.");
        } else {
            PermissionService service = provider.getProvider();
            getLogger().info(String.format("Found PermissionService: '%s' [%s]", service.getProviderName(), service.getClass().getName()));
        }

    }

    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent e) {
        RegisteredServiceProvider<?> provider = e.getProvider();
        if (provider.getService().equals(PermissionService.class)) {
            PermissionService service = (PermissionService) provider.getProvider();
            getLogger().info(String.format("PermissionService registered: '%s' [%s]", service.getProviderName(), service.getClass().getName()));
        }
    }

    @EventHandler
    public void onServiceUnregister(ServiceUnregisterEvent e) {
        RegisteredServiceProvider<?> provider = e.getProvider();
        if (provider.getService().equals(PermissionService.class)) {
            PermissionService service = (PermissionService) provider.getProvider();
            getLogger().info(String.format("PermissionService unregistered: '%s' [%s]", service, service.getClass().getName()));
        }
    }

}
