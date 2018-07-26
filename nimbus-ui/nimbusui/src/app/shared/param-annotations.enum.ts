import { Enum } from './command.enum';
export class ViewConfig extends Enum<string> {
    public static readonly page = new Enum('ViewConfig.Page');
    public static readonly pageheader = new Enum('ViewConfig.PageHeader');
    public static readonly gridrowbody = new Enum('ViewConfig.GridRowBody');
    public static readonly linkmenu = new Enum('ViewConfig.LinkMenu');
    public static readonly link = new Enum('ViewConfig.Link');
    public static readonly menulink = new Enum('ViewConfig.MenuLink');
    public static readonly footerproperty = new Enum('ViewConfig.FooterProperty');
    public static readonly hidden = new Enum('ViewConfig.Hidden');
    public static readonly initialize = new Enum('initialize');
    public static readonly button = new Enum('ViewConfig.Button');
    public static readonly gridcolumn = new Enum('ViewConfig.GridColumn');
    public static readonly actiontray = new Enum('ViewConfig.ActionTray');

    static attributeList(): String[] {
      const keys = Object.keys(ViewConfig);
      return keys;
    }
}

export class ViewComponent extends Enum<string> {
    public static readonly page = new Enum('Page');
    public static readonly tile = new Enum('Tile');
    public static readonly section = new Enum('Section');
    public static readonly form = new Enum('Form');
    public static readonly link = new Enum('Link');
    public static readonly linkMenu = new Enum('LinkMenu');
    public static readonly grid = new Enum('Grid');
    public static readonly button = new Enum('Button');
    public static readonly buttongroup = new Enum('ButtonGroup');
    public static readonly gridRowBody = new Enum('GridRowBody');
    public static readonly gridcolumn = new Enum('GridColumn');
    public static readonly actiontray = new Enum('ActionTray');
    public static readonly modal = new Enum('Modal');
    public static readonly menulink = new Enum('MenuLink');
    public static readonly menupanel = new Enum('MENUPANEL');
    public static readonly image = new Enum('Image');
    public static readonly tabInfo = new Enum('TabInfo');

    static attributeList(): String[] {
      const keys = Object.keys(ViewConfig);
      return keys;
    }
}