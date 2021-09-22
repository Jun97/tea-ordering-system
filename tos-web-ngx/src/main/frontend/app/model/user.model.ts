import {TeaSessionModel} from "./tea-session.model";
import {OrderModel} from "./order.model";

export class UserModel
{
  constructor(
  public userId: number | null,
  public email: string,
  public password: string,
  public isEnabled: boolean,
  public lastLoginDate: Date | null,
  public isAdmin: boolean,
  public teaSessions: Array<TeaSessionModel> | null,
  public orders: Array<OrderModel> | null
    ) { }
}