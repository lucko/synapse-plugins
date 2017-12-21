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

import me.lucko.synapse.permission.PermissionService;
import me.lucko.synapse.permission.context.Context;
import me.lucko.synapse.permission.options.SetOptions;
import me.lucko.synapse.permission.options.UnsetOptions;
import me.lucko.synapse.permission.subject.Group;
import me.lucko.synapse.permission.subject.User;
import me.lucko.synapse.util.FutureResult;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PexPermissionService implements PermissionService {

    @Nonnull
    @Override
    public String getProviderName() {
        return "PermissionsEx";
    }

    @Nonnull
    @Override
    public User getUser(@Nonnull Player player) {
        return null;
    }

    @Nullable
    @Override
    public User getUser(@Nonnull UUID uniqueId) {
        return null;
    }

    @Nonnull
    @Override
    public FutureResult<User> loadUser(@Nonnull UUID uniqueId) {
        return null;
    }

    @Nonnull
    @Override
    public Collection<Group> getGroups() {
        return null;
    }

    @Nullable
    @Override
    public Group getGroup(@Nonnull String name) {
        return null;
    }

    @Nonnull
    @Override
    public SetOptions getNormalSetOptions() {
        return PexSetOptions.INSTANCE;
    }

    @Nonnull
    @Override
    public UnsetOptions getNormalUnsetOptions() {
        return PexUnsetOptions.INSTANCE;
    }

    @Override
    public boolean supportsContextType(@Nonnull String key) {
        return key.equals("world");
    }

    @Nonnull
    @Override
    public Context createContext(@Nonnull String key, @Nonnull String value) throws IllegalArgumentException {
        if (!key.equals("world")) {
            throw new IllegalArgumentException("Key not supported: " + key);
        }

        return new PexWorldContext(value);
    }

    public static String getWorld(Set<Context> contexts) {
        String world = null;
        for (Context context : contexts) {
            if (!(context instanceof PexWorldContext)) {
                throw new IllegalArgumentException("Invalid context type: " + context.getClass());
            }
            if (world != null) {
                throw new IllegalArgumentException("Duplicate world key: " + context.getValue() + " (already have: " + world + ")");
            }
            world = context.getValue();
        }
        return world;
    }

    public static Set<Context> worldContext(String world) {
        if (world == null) {
            return Collections.emptySet();
        }
        return Collections.singleton(new PexWorldContext(world));
    }

}
