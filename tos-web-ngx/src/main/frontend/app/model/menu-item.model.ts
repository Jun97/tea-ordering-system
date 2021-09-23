import {OrderItemModel} from "./order-item.model";
import {TeaSessionModel} from "./tea-session.model";

export class MenuItemModel
{
  constructor(
  public menuItemId: number | null,
  public name: string,
  public imagePath: string | File |null,
  public teaSessionMenuItem: TeaSessionModel | null,
  public orderItems: Array<OrderItemModel> | null,
    ) { }
}