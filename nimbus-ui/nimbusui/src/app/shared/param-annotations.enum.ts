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
    public static readonly picklist = new Enum('ViewConfig.PickList');
    public static readonly selectedPicklist = new Enum('ViewConfig.PickListSelected');
    public static readonly printConfig = new Enum('ViewConfig.PrintConfig');
    public static readonly fonts = new Enum('ViewConfig.Fonts');
    public static readonly headings = new Enum('ViewConfig.Headings');

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
    public static readonly formElementGroup = new Enum('FormElementGroup');
    public static readonly link = new Enum('Link');
    public static readonly linkMenu = new Enum('LinkMenu');
    public static readonly grid = new Enum('Grid');
    public static readonly treeGrid = new Enum('TreeGrid');
    public static readonly treeGridChild = new Enum('TreeGridChild');
    public static readonly cardDetailsGrid = new Enum('CardDetailsGrid');
    public static readonly button = new Enum('Button');
    public static readonly buttongroup = new Enum('ButtonGroup');
    public static readonly gridRowBody = new Enum('GridRowBody');
    public static readonly gridcolumn = new Enum('GridColumn');
    public static readonly actiontray = new Enum('ActionTray');
    public static readonly modal = new Enum('Modal');
    public static readonly menulink = new Enum('MenuLink');
    public static readonly menupanel = new Enum('MenuPanel');
    public static readonly image = new Enum('Image');
    public static readonly tabInfo = new Enum('TabInfo');
    public static readonly picklist = new Enum('PickList');
    public static readonly selectedPicklist = new Enum('PickListSelected');
    public static readonly paragraph = new Enum('Paragraph');
    public static readonly header = new Enum('Header');
    public static readonly formGridFiller = new Enum('FormGridFiller');
    public static readonly chart = new Enum('Chart');

    static attributeList(): String[] {
      const keys = Object.keys(ViewConfig);
      return keys;
    }
}

export class ComponentTypes extends Enum<string> {
  public static readonly divider = new Enum('Divider');
  public static readonly field = new Enum('Field');
  public static readonly cardDetail = new Enum('CardDetail');
  public static readonly cardDetailsHeader = new Enum('CardDetailsHeader');
  public static readonly fieldValue = new Enum('FieldValue');
  public static readonly paragraph = new Enum('Paragraph');
  public static readonly buttonGroup = new Enum('ButtonGroup');
  public static readonly cardDetailsBody = new Enum('CardDetailsBody');
  public static readonly staticText = new Enum('StaticText');
  public static readonly fieldValueGroup = new Enum('FieldValueGroup');
  public static readonly header = new Enum('Header');
  public static readonly formGridFiller = new Enum('FormGridFiller');
  public static readonly text = new Enum('text');
  public static readonly mask = new Enum('InputMask');
  public static readonly signature = new Enum('signature');
  public static readonly textarea = new Enum('textarea');
  public static readonly date = new Enum('date');
  public static readonly calendar = new Enum('calendar');
  public static readonly comboBox = new Enum('ComboBox');
  public static readonly radio = new Enum('Radio');
  public static readonly checkBoxGroup = new Enum('CheckBoxGroup');
  public static readonly checkBox = new Enum('CheckBox');
  public static readonly multiSelect = new Enum('MultiSelect');
  public static readonly multiSelectCard = new Enum('MultiSelectCard');
  public static readonly pickList = new Enum('PickList');
  public static readonly fileUpload = new Enum('FileUpload');
  public static readonly grid = new Enum('Grid');
  public static readonly treeGrid = new Enum('TreeGrid');
  public static readonly dialog = new Enum('dialog');
  public static readonly cardDetailsGrid = new Enum('CardDetailsGrid');
  public static readonly menu = new Enum('Menu');
  public static readonly accordion = new Enum('Accordion');
  public static readonly textBox = new Enum('TextBox');
  public static readonly internal = new Enum('INTERNAL');
  public static readonly external = new Enum('EXTERNAL');
  public static readonly default = new Enum('DEFAULT');
  public static readonly inline = new Enum('INLINE');
  public static readonly growl = new Enum('GROWL');
  public static readonly primary = new Enum('PRIMARY');
  public static readonly secondary = new Enum('SECONDARY');
  public static readonly plain = new Enum('PLAIN');
  public static readonly destructive = new Enum('DESTRUCTIVE');
  public static readonly validation = new Enum('VALIDATION');
  public static readonly print = new Enum('PRINT');
  public static readonly toast = new Enum('TOAST');
  public static readonly inputSwitch = new Enum('InputSwitch');
  public static readonly link = new Enum('Link');
  public static readonly chart = new Enum('Chart');
  public static readonly richText = new Enum('RichText');
  
  static attributeList(): String[] {
    const keys = Object.keys(ComponentTypes);
    return keys;
  }
}