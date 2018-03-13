export interface IGenericValue {
    value: any;
}

export interface IGenericType {
    [name: string]: IGenericValue;
}

export class GenericDomain {
    //domain: any = {};
    [name: string]: any;

    addAttribute(name: string, value: any) {
        //this.domain[name] = value;
        this[name] = value;
    }
}
