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

import me.lucko.synapse.permission.context.Context;
import me.lucko.synapse.permission.options.UnsetOptions;

import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;

public class PexUnsetOptions implements UnsetOptions {
    public static final PexUnsetOptions INSTANCE = new PexUnsetOptions();

    private final String world;
    private final boolean targetTemporary;

    private PexUnsetOptions() {
        this(null, false);
    }

    private PexUnsetOptions(String world, boolean targetTemporary) {
        this.world = world;
        this.targetTemporary = targetTemporary;
    }

    @Override
    public boolean supportsExpiry() {
        return true;
    }

    @Nonnull
    @Override
    public UnsetOptions withExpiry(long expiryTime) {
        return new PexUnsetOptions(world, true);
    }

    @Nonnull
    @Override
    public UnsetOptions matchAnyExpiry() {
        return new PexUnsetOptions(world, false);
    }

    @Nonnull
    @Override
    public UnsetOptions withContexts(@Nonnull Set<Context> contexts) {
        return new PexUnsetOptions(PexPermissionService.getWorld(contexts), targetTemporary);
    }

    @Nonnull
    @Override
    public UnsetOptions matchAnyContext() {
        return new PexUnsetOptions(null, targetTemporary);
    }

    @Override
    public boolean shouldMatchExpiry() {
        return targetTemporary;
    }

    @Override
    public long getExpiryTime() throws IllegalStateException {
        return -1;
    }

    @Override
    public boolean shouldMatchContexts() {
        return world != null;
    }

    @Override
    public Set<Context> getContexts() throws IllegalStateException {
        if (world == null) {
            throw new IllegalStateException();
        }

        return Collections.singleton(new PexWorldContext(world));
    }
}
