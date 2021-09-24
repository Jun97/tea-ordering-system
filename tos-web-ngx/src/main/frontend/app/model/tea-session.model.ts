import {MenuItemModel} from "./menu-item.model";
import {OrderModel} from "./order.model";
import {UserModel} from "./user.model";

export class TeaSessionModel
{
    constructor(
    public teaSessionId: number | null,
    public name: string,
    public description: string | null,
    public isPublic: boolean,
    public password: string | null,
    public treatDate: Date | string,
    public cutOffDate: Date | string,
    public imagePath: string | File | null,
    public menuItems: Array<MenuItemModel> | null,
    public orders: Array<OrderModel> | null,
    public userTeaSession: UserModel | null,
    ) { }
}