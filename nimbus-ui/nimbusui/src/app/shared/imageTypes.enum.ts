import { Enum } from './command.enum';

export class ImageTypes extends Enum<string> {
    public static readonly svg = new Enum('SVG');
    public static readonly fa = new Enum('FA');

    static attributeList(): String[] {
      const keys = Object.keys(ImageTypes);
      return keys;
    }
}