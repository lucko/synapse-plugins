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

import net.milkbowl.vault.permission.Permission;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class SynapseVaultChat extends UpgradedVaultChat {
    final PermissionService service;

    public SynapseVaultChat(PermissionService service, Permission perms) {
        super(perms);
        this.service = service;
    }

    private Set<Context> worldContext(String world) {
        if (world == null) {
            return Collections.emptySet();
        }
        return Collections.singleton(service.createContext("world", world));
    }

    private static String toString(Object o) {
        return o == null ? null : o.toString();
    }

    @Override
    public String getName() {
        return "synapse:" + service.getProviderName();
    }

    @Override
    public String getPlayerPrefix(String world, UUID uuid) {
        Objects.requireNonNull(uuid, "uuid");

        User user = service.getUser(uuid);
        if (user == null) return null;

        return user.getPrefix(worldContext(world));
    }

    @Override
    public String getPlayerSuffix(String world, UUID uuid) {
        Objects.requireNonNull(uuid, "uuid");

        User user = service.getUser(uuid);
        if (user == null) return null;

        return user.getSuffix(worldContext(world));
    }

    @Override
    public void setPlayerPrefix(String world, UUID uuid, String prefix) {
        Objects.requireNonNull(uuid, "uuid");

        User user = service.getUser(uuid);
        if (user == null) return;

        user.setPrefix(prefix, service.getNormalSetOptions().withContexts(worldContext(world)));
    }

    @Override
    public void setPlayerSuffix(String world, UUID uuid, String prefix) {
        Objects.requireNonNull(uuid, "uuid");

        User user = service.getUser(uuid);
        if (user == null) return;

        user.setSuffix(prefix, service.getNormalSetOptions().withContexts(worldContext(world)));
    }

    @Override
    public String getPlayerInfo(String world, UUID uuid, String key) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(key, "key");

        User user = service.getUser(uuid);
        if (user == null) return null;

        return user.getMetadata(key, worldContext(world));
    }

    @Override
    public void setPlayerInfo(String world, UUID uuid, String key, Object value) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(key, "key");

        User user = service.getUser(uuid);
        if (user == null) return;

        user.setMetadata(key, toString(value), service.getNormalSetOptions().withContexts(worldContext(world)));
    }

    @Override
    public String getGroupPrefix(String world, String group) {
        Objects.requireNonNull(group, "group");

        Group g = service.getGroup(group);
        if (g == null) return null;

        return g.getPrefix(worldContext(world));
    }

    @Override
    public String getGroupSuffix(String world, String group) {
        Objects.requireNonNull(group, "group");

        Group g = service.getGroup(group);
        if (g == null) return null;

        return g.getSuffix(worldContext(world));
    }

    @Override
    public void setGroupPrefix(String world, String group, String prefix) {
        Objects.requireNonNull(group, "group");

        Group g = service.getGroup(group);
        if (g == null) return;

        g.setPrefix(prefix, service.getNormalSetOptions().withContexts(worldContext(world)));
    }

    @Override
    public void setGroupSuffix(String world, String group, String prefix) {
        Objects.requireNonNull(group, "group");

        Group g = service.getGroup(group);
        if (g == null) return;

        g.setSuffix(prefix, service.getNormalSetOptions().withContexts(worldContext(world)));
    }

    @Override
    public String getGroupInfo(String world, String group, String key) {
        Objects.requireNonNull(group, "group");
        Objects.requireNonNull(key, "key");

        Group g = service.getGroup(group);
        if (g == null) return null;

        return g.getMetadata(key, worldContext(world));
    }

    @Override
    public void setGroupInfo(String world, String group, String key, Object value) {
        Objects.requireNonNull(group, "group");
        Objects.requireNonNull(key, "key");

        Group g = service.getGroup(group);
        if (g == null) return;

        g.setMetadata(key, toString(value), service.getNormalSetOptions().withContexts(worldContext(world)));
    }
}
