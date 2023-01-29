package maninthehouse.epicfight.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EpicFightItemGroup {


	public static final CreativeTabs ITEMS = new CreativeTabListed("epicfight");

	public static class CreativeTabSorted extends CreativeTabs {

		private ItemStack iconItem;
		private final Comparator<? super ItemStack> sorter;
		private final boolean searchable;

		public CreativeTabSorted(String label, Comparator<? super ItemStack> sorter){
			this(label, sorter, false);
		}

		public CreativeTabSorted(String label, Comparator<? super ItemStack> sorter, boolean searchable){
			super(label);
			this.sorter = sorter;
			this.searchable = searchable;
		}


		@SideOnly(Side.CLIENT)
		public ItemStack createIcon(){
			return iconItem;
		}
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.SKILLBOOK);
		}

		public void setIconItem(ItemStack iconItem){
			this.iconItem = iconItem;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void displayAllRelevantItems(NonNullList<ItemStack> items){
			super.displayAllRelevantItems(items);
			items.sort(sorter);
		}

		@Override
		public boolean hasSearchBar(){
			return searchable;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public String getBackgroundImageName(){
			return searchable ? "item_search.png" : super.getBackgroundImageName();
		}
	}

	public static class CreativeTabListed extends CreativeTabSorted {

		public final List<Item> order;

		public CreativeTabListed(String label){
			// Can't accomplish this in a single constructor... just a quirk of the java compiler!
			this(label, new ArrayList<>());
		}

		// Hey, maybe someone might want to keep a reference to the list, or pass in a different one.
		public CreativeTabListed(String label, List<Item> order){

			super(label, (stack1, stack2) -> {
				// Neither stack is in the creative tab
				if(!order.contains(stack1.getItem()) && !order.contains(stack2.getItem())) return 0;
				if(!order.contains(stack1.getItem())) return 1; // Only stack 2 is in the creative tab
				if(!order.contains(stack2.getItem())) return -1; // Only stack 1 is in the creative tab
				// Both stacks are in the creative tab
				return order.indexOf(stack1.getItem()) - order.indexOf(stack2.getItem());
			});

			this.order = order;
		}
	}
}