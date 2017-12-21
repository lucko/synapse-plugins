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

package me.lucko.synapse.generic;

import me.lucko.synapse.permission.PermissionService;
import me.lucko.synapse.permission.subject.Group;
import me.lucko.synapse.permission.subject.MetadataSubject;
import me.lucko.synapse.permission.subject.PermissionSubject;
import me.lucko.synapse.util.FutureAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractSubject implements PermissionSubject, MetadataSubject {
    private final PermissionService service;

    public AbstractSubject(PermissionService service) {
        this.service = service;
    }

    @Nonnull
    public FutureAction setPermission(@Nonnull String permission) {
        return setPermission(permission, service.getNormalSetOptions());
    }

    @Nonnull
    public FutureAction unsetPermission(@Nonnull String permission) {
        return unsetPermission(permission, service.getNormalUnsetOptions());
    }

    @Nonnull
    public FutureAction addGroup(@Nonnull Group group) {
        return addGroup(group, service.getNormalSetOptions());
    }

    @Nonnull
    public FutureAction removeGroup(@Nonnull Group group) {
        return removeGroup(group, service.getNormalUnsetOptions());
    }

    @Nonnull
    public FutureAction setPrefix(@Nullable String prefix) {
        return setPrefix(prefix, service.getNormalSetOptions());
    }

    @Nonnull
    public FutureAction setSuffix(@Nullable String suffix) {
        return setSuffix(suffix, service.getNormalSetOptions());
    }

    @Nonnull
    public FutureAction setMetadata(@Nonnull String key, @Nullable String value) {
        return setMetadata(key, value, service.getNormalSetOptions());
    }

}
