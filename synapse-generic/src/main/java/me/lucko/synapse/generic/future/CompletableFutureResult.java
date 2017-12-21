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

package me.lucko.synapse.generic.future;


import me.lucko.synapse.util.FutureCallback;
import me.lucko.synapse.util.FutureResult;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

public class CompletableFutureResult<T> implements FutureResult<T> {
    private final CompletableFuture<T> future;

    public CompletableFutureResult(CompletableFuture<T> future) {
        this.future = future;
    }

    @Nonnull
    @Override
    public FutureResult whenComplete(@Nonnull Plugin plugin, @Nonnull FutureCallback<T> callback) {
        future.thenAcceptAsync(callback::accept, r -> Bukkit.getScheduler().runTask(plugin, r));
        return this;
    }

    @Override
    public T join() {
        return future.join();
    }

    @Nonnull
    @Override
    public CompletableFuture<T> asFuture() {
        return future;
    }
}
