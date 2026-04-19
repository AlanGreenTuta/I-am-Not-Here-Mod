/**
 *     I am Not Here - a Minecraft Mod
 *     Copyright (C) 2026  42
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ianh.ianh;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IanhItemGroups {
    public static final ItemGroup INAH_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(IanhItems.INAH_ITEM))
            .displayName(Text.translatable("itemGroup.ianh.ianh_group"))
            .entries((context, entries) -> {
                entries.add(IanhItems.INAH_ITEM);
            })
            .build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, new Identifier("ianh", "ianh_group"), INAH_GROUP);
    }
}
