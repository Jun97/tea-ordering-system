import {TeaSessionModel} from "./tea-session.model";
import {UserModel} from "./user.model";
import {OrderItemModel} from "./order-item.model";

export class OrderModel
{
  constructor(
  public orderId: number | null,
  public teaSessionOrder: TeaSessionModel | null,
  public userOrder: UserModel | null,
  public orderItems: Array<OrderItemModel> | null,
    ) { }
}