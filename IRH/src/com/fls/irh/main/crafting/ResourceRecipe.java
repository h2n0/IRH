package com.fls.irh.main.crafting;

import com.fls.irh.main.entity.Player;
import com.fls.irh.main.item.ResourceItem;
import com.fls.irh.main.item.resource.Resource;

public class ResourceRecipe extends Recipe {
	private Resource resource;

	public ResourceRecipe(Resource resource) {
		super(new ResourceItem(resource, 1));
		this.resource = resource;
	}

	public void craft(Player player) {
		player.inventory.add(0, new ResourceItem(resource, 1));
	}
}
