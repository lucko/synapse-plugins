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

import me.lucko.synapse.permission.context.Context;
import me.lucko.synapse.permission.options.SetOptions;

import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;

public class VaultSetOptions implements SetOptions {
    public static final VaultSetOptions INSTANCE = new VaultSetOptions();

    private final boolean negated;
    private final String world;

    private VaultSetOptions() {
        this(false, null);
    }

    private VaultSetOptions(boolean negated, String world) {
        this.negated = negated;
        this.world = world;
    }

    public String getWorld() {
        return world;
    }

    @Override
    public boolean supportsNegation() {
        return true;
    }

    @Override
    public boolean supportsExpiry() {
        return false;
    }

    @Nonnull
    @Override
    public SetOptions negated() {
        return new VaultSetOptions(true, world);
    }

    @Nonnull
    @Override
    public SetOptions notNegated() {
        return new VaultSetOptions(false, world);
    }

    @Nonnull
    @Override
    public SetOptions withExpiry(long expiryTime) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public SetOptions noExpiry() {
        return this;
    }

    @Nonnull
    @Override
    public SetOptions withContexts(@Nonnull Set<Context> contexts) {
        return new VaultSetOptions(negated, VaultPermissionService.getWorld(contexts));
    }

    @Override
    public boolean shouldNegate() {
        return negated;
    }

    @Override
    public boolean shouldExpire() {
        return false;
    }

    @Override
    public long getExpiryTime() throws IllegalStateException {
        throw new IllegalStateException();
    }

    @Nonnull
    @Override
    public Set<Context> getContexts() {
        if (world == null) {
            return Collections.emptySet();
        } else {
            return Collections.singleton(new VaultWorldContext(world));
        }
    }
}
