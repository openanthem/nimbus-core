export class Converter {

    static convert(jsonObj:any, target: any): any {
        for (var propName in jsonObj) {
            if(!(jsonObj[propName] instanceof Object)) {
                if(typeof target[propName] === "number") {
                    target[propName] = +jsonObj[propName]
                }
                else {
                    target[propName] = jsonObj[propName];
                }
            }
        }
        return target;
    }
}