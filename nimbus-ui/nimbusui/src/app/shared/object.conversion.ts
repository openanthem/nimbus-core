export class Converter {

    static convert(jsonObj:any, target: any): any {
        for (var propName in jsonObj) {
            if(!(jsonObj[propName] instanceof Object)) {
                target[propName] = jsonObj[propName];
            }
        }
        return target;
    }
}