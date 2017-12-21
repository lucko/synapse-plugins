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
import me.lucko.synapse.permission.context.Context;
import me.lucko.synapse.permission.subject.Group;
import me.lucko.synapse.permission.subject.User;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class SynapseVaultPermission extends UpgradedVaultPermission {
    final PermissionService service;

    public SynapseVaultPermission(PermissionService service) {
        this.service = service;
    }

    private Set<Context> worldContext(String world) {
        if (world == null) {
            return Collections.emptySet();
        }
        return Collections.singleton(service.createContext("world", world));
    }

    @Override
    public String getName() {
        return "synapse:" + service.getProviderName();
    }

    @Override
    public boolean hasPermission(String world, UUID uuid, String permission) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(permission, "permission");

        User user = service.getUser(uuid);
        if (user == null) return false;

        return user.checkPermission(permission, worldContext(world));
    }

    @Override
    public boolean playerAddPermission(String world, UUID uuid, String permission) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(permission, "permission");

        User user = service.getUser(uuid);
        if (user == null) return false;

        user.setPermission(permission, service.getNormalSetOptions().withContexts(worldContext(world)));
        return true;
    }

    @Override
    public boolean playerRemovePermission(String world, UUID uuid, String permission) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(permission, "permission");

        User user = service.getUser(uuid);
        if (user == null) return false;

        user.unsetPermission(permission, service.getNormalUnsetOptions().withContexts(worldContext(world)));
        return true;
    }

    @Override
    public boolean playerInGroup(String world, UUID uuid, String group) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(group, "group");

        User user = service.getUser(uuid);
        if (user == null) return false;

        return user.getGroups().stream().anyMatch(m -> m.getGroup().getName().equalsIgnoreCase(group));
    }

    @Override
    public boolean playerAddGroup(String world, UUID uuid, String group) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(group, "group");

        User user = service.getUser(uuid);
        if (user == null) return false;

        Group g = service.getGroup(group);
        if (g == null) return false;

        user.addGroup(g, service.getNormalSetOptions().withContexts(worldContext(world)));
        return true;
    }

    @Override
    public boolean playerRemoveGroup(String world, UUID uuid, String group) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(group, "group");

        User user = service.getUser(uuid);
        if (user == null) return false;

        Group g = service.getGroup(group);
        if (g == null) return false;

        user.removeGroup(g, service.getNormalUnsetOptions().withContexts(worldContext(world)));
        return true;
    }

    @Override
    public String[] playerGetGroups(String world, UUID uuid) {
        Objects.requireNonNull(uuid, "uuid");

        User user = service.getUser(uuid);
        if (user == null) return new String[0];

        return user.getGroups().stream().map(m -> m.getGroup().getName()).toArray(String[]::new);
    }

    @Override
    public String playerPrimaryGroup(String world, UUID uuid) {
        Objects.requireNonNull(uuid, "uuid");

        User user = service.getUser(uuid);
        if (user == null) return null;

        Group primaryGroup = user.getPrimaryGroup(worldContext(world));
        if (primaryGroup == null) return null;

        return primaryGroup.getName();
    }

    @Override
    public boolean groupHasPermission(String world, String name, String permission) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(permission, "permission");

        Group group = service.getGroup(name);
        if (group == null) return false;

        return group.checkPermission(permission, worldContext(world));
    }

    @Override
    public boolean groupAddPermission(String world, String name, String permission) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(permission, "permission");

        Group group = service.getGroup(name);
        if (group == null) return false;

        group.setPermission(permission, service.getNormalSetOptions().withContexts(worldContext(world)));
        return true;
    }

    @Override
    public boolean groupRemovePermission(String world, String name, String permission) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(permission, "permission");

        Group group = service.getGroup(name);
        if (group == null) return false;

        group.unsetPermission(permission, service.getNormalUnsetOptions().withContexts(worldContext(world)));
        return true;
    }

    @Override
    public String[] getGroups() {
        return service.getGroups().stream().map(Group::getName).toArray(String[]::new);
    }
}
