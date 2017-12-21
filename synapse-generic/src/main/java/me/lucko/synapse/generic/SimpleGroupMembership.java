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

import com.google.common.collect.ImmutableSet;

import me.lucko.synapse.permission.context.Context;
import me.lucko.synapse.permission.membership.GroupMembership;
import me.lucko.synapse.permission.node.PermissionNode;
import me.lucko.synapse.permission.subject.Group;

import java.util.Objects;
import java.util.Set;

public class SimpleGroupMembership implements GroupMembership {
    private final Group group;
    private final Set<Context> context;

    public SimpleGroupMembership(Group group, Set<Context> context) {
        Objects.requireNonNull(group, "group is null");
        Objects.requireNonNull(context, "context is null");
        this.group = group;
        this.context = ImmutableSet.copyOf(context);
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public Set<Context> getRequiredContext() {
        return context;
    }
}
