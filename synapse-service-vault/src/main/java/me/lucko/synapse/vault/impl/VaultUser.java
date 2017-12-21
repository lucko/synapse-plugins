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

package me.lucko.synapse.vault.impl;

import com.google.common.collect.ImmutableList;

import me.lucko.synapse.generic.AbstractSubject;
import me.lucko.synapse.generic.SimpleGroupMembership;
import me.lucko.synapse.generic.SimplePermissionNode;
import me.lucko.synapse.generic.future.CompletedFutureAction;
import me.lucko.synapse.permission.context.Context;
import me.lucko.synapse.permission.membership.GroupMembership;
import me.lucko.synapse.permission.node.PermissionNode;
import me.lucko.synapse.permission.options.SetOptions;
import me.lucko.synapse.permission.options.UnsetOptions;
import me.lucko.synapse.permission.subject.Group;
import me.lucko.synapse.permission.subject.User;
import me.lucko.synapse.util.FutureAction;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VaultUser extends AbstractSubject implements User {
    private final VaultPermissionService service;
    private final UUID uuid;

    public VaultUser(VaultPermissionService service, UUID uuid) {
        super(service);
        this.service = service;
        this.uuid = uuid;
    }

    private OfflinePlayer player() {
        return Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid), "player is null");
    }

    private String currentWorld() {
        OfflinePlayer player = player();
        if (!player.isOnline()) {
            return null;
        } else {
            return player.getPlayer().getWorld().getName();
        }
    }

    @Nonnull
    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Nullable
    @Override
    public String getUsername() {
        return player().getName();
    }

    @Nullable
    @Override
    public Group getPrimaryGroup() {
        return service.getGroup(service.vaultPerms.getPrimaryGroup(null, player()));
    }

    @Nullable
    @Override
    public Group getPrimaryGroup(Set<Context> contexts) {
        return service.getGroup(service.vaultPerms.getPrimaryGroup(VaultPermissionService.getWorld(contexts), player()));
    }

    @Nonnull
    @Override
    public Collection<PermissionNode> getPermissions() {
        OfflinePlayer player = player();
        if (player == null || !player.isOnline()) {
            return Collections.emptyList();
        }

        Player p = player.getPlayer();
        ImmutableList.Builder<PermissionNode> nodes = ImmutableList.builder();
        for (PermissionAttachmentInfo pai : p.getEffectivePermissions()) {
            nodes.add(new SimplePermissionNode(pai.getPermission(), pai.getValue(), Collections.emptySet()));
        }

        return nodes.build();
    }

    @Nonnull
    @Override
    public Collection<GroupMembership> getGroups() {
        String[] groups = service.vaultPerms.getPlayerGroups(null, player());
        return Arrays.stream(groups)
                .map(service::getGroup)
                .filter(Objects::nonNull)
                .map(g -> new SimpleGroupMembership(g, Collections.emptySet()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean checkPermission(@Nonnull String permission) {
        return service.vaultPerms.playerHas(null, player(), permission);
    }

    @Override
    public boolean checkPermission(@Nonnull String permission, @Nonnull Set<Context> contexts) {
        return service.vaultPerms.playerHas(VaultPermissionService.getWorld(contexts), player(), permission);
    }

    @Nonnull
    @Override
    public FutureAction setPermission(@Nonnull String permission, @Nonnull SetOptions options) {
        VaultSetOptions opts = (VaultSetOptions) options;
        service.vaultPerms.playerAdd(opts.getWorld(), player(), permission);
        return CompletedFutureAction.INSTANCE;
    }

    @Nonnull
    @Override
    public FutureAction unsetPermission(@Nonnull String permission, @Nonnull UnsetOptions options) {
        VaultSetOptions opts = (VaultSetOptions) options;
        service.vaultPerms.playerRemove(opts.getWorld(), player(), permission);
        return CompletedFutureAction.INSTANCE;
    }

    @Nonnull
    @Override
    public FutureAction addGroup(@Nonnull Group group, @Nonnull SetOptions options) {
        VaultSetOptions opts = (VaultSetOptions) options;
        service.vaultPerms.playerAddGroup(opts.getWorld(), player(), group.getName());
        return CompletedFutureAction.INSTANCE;
    }

    @Nonnull
    @Override
    public FutureAction removeGroup(@Nonnull Group group, @Nonnull UnsetOptions options) {
        VaultSetOptions opts = (VaultSetOptions) options;
        service.vaultPerms.playerRemoveGroup(opts.getWorld(), player(), group.getName());
        return CompletedFutureAction.INSTANCE;
    }

    @Nullable
    @Override
    public String getPrefix() {
        return service.vaultChat.getPlayerPrefix(currentWorld(), player());
    }

    @Nullable
    @Override
    public String getPrefix(@Nonnull Set<Context> contexts) {
        return service.vaultChat.getPlayerPrefix(VaultPermissionService.getWorld(contexts), player());
    }

    @Nullable
    @Override
    public String getSuffix() {
        return service.vaultChat.getPlayerSuffix(currentWorld(), player());
    }

    @Nullable
    @Override
    public String getSuffix(@Nonnull Set<Context> contexts) {
        return service.vaultChat.getPlayerSuffix(VaultPermissionService.getWorld(contexts), player());
    }

    @Nullable
    @Override
    public String getMetadata(@Nonnull String key) {
        return service.vaultChat.getPlayerInfoString(currentWorld(), player(), key, null);
    }

    @Nullable
    @Override
    public String getMetadata(@Nonnull String key, @Nonnull Set<Context> contexts) {
        return service.vaultChat.getPlayerInfoString(VaultPermissionService.getWorld(contexts), player(), key, null);
    }

    @Nonnull
    @Override
    public FutureAction setPrefix(@Nullable String prefix, @Nonnull SetOptions options) {
        VaultSetOptions opts = (VaultSetOptions) options;
        service.vaultChat.setPlayerPrefix(opts.getWorld(), player(), prefix);
        return CompletedFutureAction.INSTANCE;
    }

    @Nonnull
    @Override
    public FutureAction setSuffix(@Nullable String suffix, @Nonnull SetOptions options) {
        VaultSetOptions opts = (VaultSetOptions) options;
        service.vaultChat.setPlayerSuffix(opts.getWorld(), player(), suffix);
        return CompletedFutureAction.INSTANCE;
    }

    @Nonnull
    @Override
    public FutureAction setMetadata(@Nonnull String key, @Nullable String value, @Nonnull SetOptions options) {
        VaultSetOptions opts = (VaultSetOptions) options;
        service.vaultChat.setPlayerInfoString(opts.getWorld(), player(), key, value);
        return CompletedFutureAction.INSTANCE;
    }

}
