export class Converter {

    static convert(jsonObj:any, target: any, options?: any): any {
        for (var propName in jsonObj) {
            if(!(jsonObj[propName] instanceof Object)) {
                if(typeof target[propName] === "number") {
                    target[propName] = +jsonObj[propName]
                }
                else {
                    target[propName] = jsonObj[propName];
                }
            }

            // Handle arrays, if necessary.
            if (options && options.includeArrays) {
                if(jsonObj[propName] instanceof Array) {
                    target[propName] = jsonObj[propName];
                }
            }
        }
        return target;
    }
}