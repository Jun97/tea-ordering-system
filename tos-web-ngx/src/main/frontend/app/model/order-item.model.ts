import {OrderModel} from "./order.model";
import {MenuItemModel} from "./menu-item.model";

export class OrderItemModel
{
  constructor(
  public orderItemId: number | null,
  public quantity: number,
  public orderOrderItem: OrderModel | null,
  public menuItemOrderItem: MenuItemModel | null,
    ) { }
}