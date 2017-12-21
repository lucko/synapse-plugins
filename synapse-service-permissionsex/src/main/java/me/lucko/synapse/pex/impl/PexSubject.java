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

package me.lucko.synapse.pex.impl;

import com.google.common.collect.ImmutableList;

import me.lucko.synapse.generic.AbstractSubject;
import me.lucko.synapse.generic.SimpleGroupMembership;
import me.lucko.synapse.generic.SimplePermissionNode;
import me.lucko.synapse.permission.context.Context;
import me.lucko.synapse.permission.membership.GroupMembership;
import me.lucko.synapse.permission.node.PermissionNode;
import me.lucko.synapse.permission.options.SetOptions;
import me.lucko.synapse.permission.options.UnsetOptions;
import me.lucko.synapse.permission.subject.Group;
import me.lucko.synapse.util.FutureAction;

import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.PermissionGroup;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class PexSubject extends AbstractSubject {
    protected final PexPermissionService service;
    private final PermissionEntity entity;

    public PexSubject(PexPermissionService service, PermissionEntity entity) {
        super(service);
        this.service = service;
        this.entity = entity;
    }

    @Nonnull
    @Override
    public Collection<PermissionNode> getPermissions() {
        Map<String, List<String>> permissions = entity.getAllPermissions();
        ImmutableList.Builder<PermissionNode> ret = ImmutableList.builder();

        for (Map.Entry<String, List<String>> world : permissions.entrySet()) {
            Set<Context> contexts = PexPermissionService.worldContext(world.getKey());
            for (String perm : world.getValue()) {
                if (perm.startsWith("-")) {
                    ret.add(new SimplePermissionNode(perm.substring(1), false, contexts));
                } else {
                    ret.add(new SimplePermissionNode(perm, true, contexts));
                }
            }
        }

        return ret.build();
    }

    @Nonnull
    @Override
    public Collection<GroupMembership> getGroups() {
        Map<String, List<PermissionGroup>> parents = entity.getAllParents();
        ImmutableList.Builder<GroupMembership> ret = ImmutableList.builder();

        for (Map.Entry<String, List<PermissionGroup>> world : parents.entrySet()) {
            Set<Context> contexts = PexPermissionService.worldContext(world.getKey());
            for (PermissionGroup parent : world.getValue()) {
                ret.add(new SimpleGroupMembership(new PexGroup(service, parent), contexts));
            }
        }

        return ret.build();
    }

    @Override
    public boolean checkPermission(@Nonnull String permission) {
        return entity.has(permission);
    }

    @Override
    public boolean checkPermission(@Nonnull String permission, @Nonnull Set<Context> contexts) {
        return entity.has(permission, PexPermissionService.getWorld(contexts));
    }

    @Nonnull
    @Override
    public FutureAction setPermission(@Nonnull String permission, @Nonnull SetOptions options) {

        return null;
    }

    @Nonnull
    @Override
    public FutureAction unsetPermission(@Nonnull String permission, @Nonnull UnsetOptions options) {
        return null;
    }

    @Nonnull
    @Override
    public FutureAction addGroup(@Nonnull Group group, @Nonnull SetOptions options) {
        return null;
    }

    @Nonnull
    @Override
    public FutureAction removeGroup(@Nonnull Group group, @Nonnull UnsetOptions options) {
        return null;
    }

    @Nullable
    @Override
    public String getPrefix() {
        return null;
    }

    @Nullable
    @Override
    public String getPrefix(@Nonnull Set<Context> contexts) {
        return null;
    }

    @Nullable
    @Override
    public String getSuffix() {
        return null;
    }

    @Nullable
    @Override
    public String getSuffix(@Nonnull Set<Context> contexts) {
        return null;
    }

    @Nullable
    @Override
    public String getMetadata(@Nonnull String key) {
        return null;
    }

    @Nullable
    @Override
    public String getMetadata(@Nonnull String key, @Nonnull Set<Context> contexts) {
        return null;
    }

    @Nonnull
    @Override
    public FutureAction setPrefix(@Nullable String prefix, @Nonnull SetOptions options) {
        return null;
    }

    @Nonnull
    @Override
    public FutureAction setSuffix(@Nullable String suffix, @Nonnull SetOptions options) {
        return null;
    }

    @Nonnull
    @Override
    public FutureAction setMetadata(@Nonnull String key, @Nullable String value, @Nonnull SetOptions options) {
        return null;
    }
}
