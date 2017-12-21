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

import me.lucko.synapse.generic.AbstractSubject;
import me.lucko.synapse.generic.future.CompletedFutureAction;
import me.lucko.synapse.permission.context.Context;
import me.lucko.synapse.permission.membership.GroupMembership;
import me.lucko.synapse.permission.node.PermissionNode;
import me.lucko.synapse.permission.options.SetOptions;
import me.lucko.synapse.permission.options.UnsetOptions;
import me.lucko.synapse.permission.subject.Group;
import me.lucko.synapse.util.FutureAction;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VaultGroup extends AbstractSubject implements Group {
    private final VaultPermissionService service;
    private final String name;

    public VaultGroup(VaultPermissionService service, String name) {
        super(service);
        this.service = service;
        this.name = name;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public Collection<PermissionNode> getPermissions() {
        throw new UnsupportedOperationException("Group#getPermissions is not supported by the Vault implementation");
    }

    @Nonnull
    @Override
    public Collection<GroupMembership> getGroups() {
        throw new UnsupportedOperationException("Group#getGroups is not supported by the Vault implementation");
    }

    @Override
    public boolean checkPermission(@Nonnull String permission) {
        return service.vaultPerms.groupHas((String) null, name, permission);
    }

    @Override
    public boolean checkPermission(@Nonnull String permission, @Nonnull Set<Context> contexts) {
        return service.vaultPerms.groupHas(VaultPermissionService.getWorld(contexts), name, permission);
    }

    @Nonnull
    @Override
    public FutureAction setPermission(@Nonnull String permission, @Nonnull SetOptions options) {
        VaultSetOptions opts = (VaultSetOptions) options;
        service.vaultPerms.groupAdd(opts.getWorld(), name, permission);
        return CompletedFutureAction.INSTANCE;
    }

    @Nonnull
    @Override
    public FutureAction unsetPermission(@Nonnull String permission, @Nonnull UnsetOptions options) {
        VaultSetOptions opts = (VaultSetOptions) options;
        service.vaultPerms.groupRemove(opts.getWorld(), name, permission);
        return CompletedFutureAction.INSTANCE;
    }

    @Nonnull
    @Override
    public FutureAction addGroup(@Nonnull Group group, @Nonnull SetOptions options) {
        throw new UnsupportedOperationException("Group#addGroup is not supported by the Vault implementation");
    }

    @Nonnull
    @Override
    public FutureAction removeGroup(@Nonnull Group group, @Nonnull UnsetOptions options) {
        throw new UnsupportedOperationException("Group#removeGroup is not supported by the Vault implementation");
    }

    @Nullable
    @Override
    public String getPrefix() {
        return service.vaultChat.getGroupPrefix((String) null, name);
    }

    @Nullable
    @Override
    public String getPrefix(@Nonnull Set<Context> contexts) {
        return service.vaultChat.getGroupPrefix(VaultPermissionService.getWorld(contexts), name);
    }

    @Nullable
    @Override
    public String getSuffix() {
        return service.vaultChat.getGroupSuffix((String) null, name);
    }

    @Nullable
    @Override
    public String getSuffix(@Nonnull Set<Context> contexts) {
        return service.vaultChat.getGroupSuffix(VaultPermissionService.getWorld(contexts), name);
    }

    @Nullable
    @Override
    public String getMetadata(@Nonnull String key) {
        return service.vaultChat.getGroupInfoString((String) null, name, key, null);
    }

    @Nullable
    @Override
    public String getMetadata(@Nonnull String key, @Nonnull Set<Context> contexts) {
        return service.vaultChat.getGroupInfoString(VaultPermissionService.getWorld(contexts), name, key, null);
    }

    @Nonnull
    @Override
    public FutureAction setPrefix(@Nullable String prefix, @Nonnull SetOptions options) {
        VaultSetOptions opts = (VaultSetOptions) options;
        service.vaultChat.setGroupPrefix(opts.getWorld(), name, prefix);
        return CompletedFutureAction.INSTANCE;
    }

    @Nonnull
    @Override
    public FutureAction setSuffix(@Nullable String suffix, @Nonnull SetOptions options) {
        VaultSetOptions opts = (VaultSetOptions) options;
        service.vaultChat.setGroupSuffix(opts.getWorld(), name, suffix);
        return CompletedFutureAction.INSTANCE;
    }

    @Nonnull
    @Override
    public FutureAction setMetadata(@Nonnull String key, @Nullable String value, @Nonnull SetOptions options) {
        VaultSetOptions opts = (VaultSetOptions) options;
        service.vaultChat.setGroupInfoString(opts.getWorld(), name, key, value);
        return CompletedFutureAction.INSTANCE;
    }
}
